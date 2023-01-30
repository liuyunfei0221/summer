package com.blue.process.common.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.nio.charset.StandardCharsets;

public class Test2 {

    //    private static final Logger LOGGER = getLogger(BlueHbaseGenerator.class);

    public static void main(String[] args) {

        String quorum = "172.16.207.11";
        String clientPort = "2181";

        try {
//            LOGGER.info("connect to zookeeper {}:{}", quorum, clientPort);

            Configuration config = HBaseConfiguration.create();

            config.set("hbase.zookeeper.quorum", quorum);
            config.set("hbase.zookeeper.property.clientPort", clientPort);

            Connection connection = ConnectionFactory.createConnection(config);

            Admin admin = connection.getAdmin();

            TableName tableName = TableName.valueOf("test");

            TableDescriptorBuilder descriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);

            ColumnFamilyDescriptor columnFamilyDescriptor = ColumnFamilyDescriptorBuilder.newBuilder("col1".getBytes(StandardCharsets.UTF_8)).build();

            TableDescriptorBuilder tableDescriptorBuilder = descriptorBuilder.setColumnFamily(columnFamilyDescriptor);

            admin.createTable(tableDescriptorBuilder.build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
