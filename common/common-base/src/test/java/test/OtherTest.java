package test;

import com.blue.base.common.metadata.MetadataProcessor;

import static java.util.Collections.emptyMap;

public class OtherTest {

    public static void main(String[] args) {

        System.err.println(MetadataProcessor.metadataToJson(emptyMap()));

    }

}
