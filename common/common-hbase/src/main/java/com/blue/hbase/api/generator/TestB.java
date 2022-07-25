package com.blue.hbase.api.generator;

public class TestB {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static HBaseUtils instance;
    private static final byte[] INFO = Bytes.toBytes("info");
    private static final byte[] RID = Bytes.toBytes("rid");
    private static final byte[] SITE_NAME = Bytes.toBytes("sitename");
    private static final byte[] RID_COUNT = Bytes.toBytes("id_count");
    private static final byte[] SITE_NAME_COUNT = Bytes.toBytes("sitename_count");
    private Table table;

    private HBaseUtils() {
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", Config.getString("hbase.zookeeper.quorum"));
        try {
            Connection conn = ConnectionFactory.createConnection(configuration);
            table = conn.getTable(TableName.valueOf(Config.getString("hbase.tableName")));
        } catch (Exception e) {
            LOGGER.info("HBase链接失败", e);
        }

    }

    public static HBaseUtils getInstance() {
        if (instance == null) {
            synchronized (HBaseUtils.class) {
                if (instance == null) {
                    instance = new HBaseUtils();
                }
            }
        }
        return instance;
    }

    public Map<String, entModel> getBatchRow(String... rowKeys) throws IOException {
        List<Get> getList = new ArrayList<>();
        Arrays.stream(rowKeys).forEach(x -> getList.add(new Get(x.getBytes())));
        Result[] result = table.get(getList);

        Map<String, entModel> existData = new HashMap<>();
        for (Result res : result) {
            if (!res.isEmpty()) {
                existData.put(Bytes.toString(res.getRow()), new entModel() {{
                    setAllRid(Bytes.toString(res.getValue(INFO, RID)));
                    setAllSiteName(Bytes.toString(res.getValue(INFO, SITE_NAME)));
                }});
            }
        }
        return existData;
    }

    public void putBatchRow(Map<String, entModel> batchData) throws IOException {
        List<Put> putList = new ArrayList<>();
        for (Map.Entry<String, entModel> items : batchData.entrySet()) {
            Set<String> ridSet = items.getValue().getRid();
            Set<String> siteName = items.getValue().getSiteName();

            Put put = new Put(items.getKey().getBytes());
            put.addColumn(INFO, RID, String.join(",", ridSet).getBytes());
            put.addColumn(INFO, SITE_NAME, String.join(",", siteName).getBytes());
            put.addColumn(INFO, RID_COUNT, String.valueOf(ridSet.size()).getBytes());
            put.addColumn(INFO, SITE_NAME_COUNT, String.valueOf(siteName.size()).getBytes());
            putList.add(put);

            System.out.println(putList.size());
            if (putList.size() > Integer.parseInt(Config.getString("hbase.put.batch"))) {
                table.put(putList);
                putList.clear();
            }
        }
        table.put(putList);
        putList.clear();
    }

    public static void main(String[] args) {

    }

}
