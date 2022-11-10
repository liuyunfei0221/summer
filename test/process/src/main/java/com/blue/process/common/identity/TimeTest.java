package com.blue.process.common.identity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeTest {

    public static void main(String[] args) {
        String s = "2021-04-19 08:00:00";

        long bootSeconds = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .atZone(ZoneId.of("Asia/Shanghai")).toInstant().getEpochSecond();

        //1618790400
        System.err.println(bootSeconds);
    }

}
