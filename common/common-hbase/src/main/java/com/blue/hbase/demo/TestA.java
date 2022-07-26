package com.blue.hbase.demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TestA {

    //    public static Configuration conf;
//    public static Connection conn;
//
//    static {
//        conf = HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.property.clientPort", "2181");
//        try {
//            conn = ConnectionFactory.createConnection(conf);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) throws IOException {
        // 获得连接对象
        Connection connection = initHbase();
        // 获得table对象
        Table table = connection.getTable(TableName.valueOf("test:gp"));
        // 获得admin对象
        Admin admin = connection.getAdmin();

        System.out.println("对table的操作");
        // 获得rowkey的所有cell
        Map<String, String> res = getOneRow(table, "rk001");
        System.out.println(res);
        System.out.println(res.get("name"));

        // 指定rowkey， 列和限定符
        String res2 = getOneRowColumn(table, "rk001", "info", "age");
        System.out.println(res2);

        // 多个rowkey批量查询
        List<String> res3 = getMultiRowColumn(table, "info", "name", "rk001", "rk002", "rk003");
        System.out.println(res3);

        // 插入数据， 指定rowkey，列簇，列，值
        insertDataTest(table, "rk004", "info", "name", "xgp");

        // 指定rowkey插入多个列
        insertDataTest2(table, "rk003", "info");

        // 指定batch批量插入
        insertBatchTest(table);

        // 删除数据， 指定rowkey删除数据
        deleteRowkey(table, "rk004");

        // 删除指定的cell
        deleteRowkeyCell(table, "rk003", "info", "age");

        // rowkey前缀搜索  PrefixFilter
        System.out.println("filter scan");
        PrefixFilter filter = new PrefixFilter("rk".getBytes());
        ResultScanner resultScanner = fuzzyScan(table, filter);
        for (Result result : resultScanner) {
            System.out.println(Bytes.toString(result.getRow()));
            System.out.println(Bytes.toString(result.getValue("info".getBytes(), "name".getBytes())));
        }

        // 根据rowkey范围查询
        // 大于等于rk001， 小于rk003
        rowkeyRangeScanTest(table,"rk001", "rk003");

        System.out.println("对admin的操作");
        // 建表
        createTable(admin, "test:gp2", "info1", "info2");
        // 删除表
        deleteTable(admin, "test:gp2");

    }

    /**
     * 连接hbase
     */
    public static Connection initHbase() throws IOException {
        Configuration configuration = HBaseConfiguration.create();


        configuration.set("hbase.zookeeper.quorum", "cloudera01,cloudera02,cloudera03");
        return ConnectionFactory.createConnection(configuration);
    }

    /**
     * 通过rowkey查询一条数据的所有单元格
     */
    public static Map<String, String> getOneRow(Table table, String rowKey) throws IOException {
//        Get get = new Get(rowKey.getBytes());
        Get get = new Get(Bytes.toBytes(rowKey));
        Result result = table.get(get);
        Map<String, String> map = new HashMap<>();
        for (Cell cell : result.listCells()) {
            String colName = Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength());
            String value = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
            map.put(colName, value);
        };
        return map;
    }

    /**
     * 根据rowkeu查询指定的cell内容
     */
    public static String getOneRowColumn(Table table, String rowkey, String columnFamily, String column) throws IOException {
        Get get = new Get(rowkey.getBytes());
        get.addColumn(columnFamily.getBytes(), column.getBytes());
        Result result = table.get(get);
        String res = Bytes.toString(result.getValue(columnFamily.getBytes(), column.getBytes()));
        return res;
    }

    /**
     * 查询多个rowkey
     */
    public static List<String> getMultiRowColumn(Table table, String columnFamily, String column, String... rowkeys) throws IOException {
        List<Get> getList = new ArrayList<>();
        Arrays.stream(rowkeys).forEach(x -> getList.add(new Get(x.getBytes())));
        Result[] result = table.get(getList);

        // 直接获取值的List
        List<String> resList = new ArrayList<>();
        for (Result res : result) {
            resList.add(Bytes.toString(res.getValue(columnFamily.getBytes(), column.getBytes())));
        }

        // 获取kv对
        Map<String, Result> resMap = new HashMap<>();
        for (Result res : result) {
            resMap.put(Bytes.toString(res.getRow()), res);
        }
        System.out.println(resMap);
        System.out.println(Bytes.toString(resMap.get("rk001").getValue("info".getBytes(), "name".getBytes())));

        return resList;
    }

    /**
     * scan的filter
     */
    public static ResultScanner fuzzyScan(Table table, Filter... filters) throws IOException {
        Scan s = new Scan();
        for (Filter filter : filters) {
            s.setFilter(filter);
        }
        return table.getScanner(s);
    }

    /**
     * 根据rowkey范围查询
     */
    public static void rowkeyRangeScanTest(Table table, String startRow, String endRow) throws IOException {
        Scan s = new Scan(startRow.getBytes(), endRow.getBytes());
        ResultScanner res = table.getScanner(s);

        for (Result result : res) {
            System.out.println(Bytes.toString(result.getRow()));
            System.out.println(Bytes.toString(result.getValue("info".getBytes(), "name".getBytes())));
            System.out.println(result.size());  // size返回cell数
        }
    }

    /**
     * 插入数据
     */
    public static void insertDataTest(Table table, String rowkey, String columnFamily, String column, String value) throws IOException {
        Put put = new Put(rowkey.getBytes());
        put.addColumn(columnFamily.getBytes(), column.getBytes(), value.getBytes());
        table.put(put);
    }

    /**
     * 一个rowkey插入多个列
     */
    public static void insertDataTest2(Table table, String rowkey, String columnFamily) throws IOException {
        Map<String, String> data = new HashMap() {{
            put("name", "gp");
            put("age", "12");
            put("city", "suz");
        }};
        System.out.println(data);

        Put put = new Put(rowkey.getBytes());
        for (Map.Entry<String, String> items : data.entrySet()) {
            put.addColumn(columnFamily.getBytes(), items.getKey().getBytes(), items.getValue().getBytes());
            table.put(put);
        }
    }

    /**
     * 批量插入
     */
    public static void insertBatchTest(Table table) throws IOException {
        List<String> data = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "a", "6", "8", "10", "e"));
        List<Put> putList = new ArrayList<>();

        for (String item : data) {
            Put put = new Put(("rk_" + item).getBytes());
            put.addColumn("info".getBytes(), "name".getBytes(), ("name_" + item).getBytes());
            put.addColumn("info".getBytes(), "age".getBytes(), ("age_" + item).getBytes());
            putList.add(put);

            if (putList.size() > 3) {
                table.put(putList);
                putList.clear();
            }
        }
        table.put(putList);
        putList.clear();
    }

    /**
     * 指定rowkey删除rowkey的所有cell数据
     */
    public static void deleteRowkey(Table table, String rowkey) throws IOException {
        Delete delete = new Delete(rowkey.getBytes());
        table.delete(delete);
    }

    /**
     * 删除指定rowkey的某个cell
     */
    public static void deleteRowkeyCell(Table table, String rowkey, String columnFamily, String column) throws IOException {
        Delete delete = new Delete(rowkey.getBytes());
        delete.addColumn(columnFamily.getBytes(), column.getBytes());
        table.delete(delete);
    }

    /**
     * 使用admin建表， 指定表名和列簇名
     */
    public static void createTable(Admin admin, String tableName, String... cols) throws IOException {
        if (admin.tableExists(TableName.valueOf(tableName))) {
            System.out.println("表已存在！");
        } else {

            TableDescriptorBuilder tableDescBuilder = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName));
            for (String col : cols) {
                ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder(col.getBytes(StandardCharsets.UTF_8)).build();
                tableDescBuilder.setColumnFamily(columnFamilyDescriptor);
            }

            admin.createTable(tableDescBuilder.build());
            System.out.println(tableName + "创建成功");
        }
    }

    /**
     * 删除表
     */
    public static void deleteTable(Admin admin, String tableName) throws IOException {
        admin.disableTable(TableName.valueOf(tableName));
        admin.deleteTable(TableName.valueOf(tableName));
    }

}
