package test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeTest {

    public static void main(String[] args) {

        Instant instant = Instant.ofEpochSecond(1664369017 + 1200);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        System.err.println(localDateTime);

    }

}
