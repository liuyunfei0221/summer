package test;

import java.util.Base64;

public class JwtTest {

    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        String jwt = "eyJraWQiOiI4OTg3NDNlYTA4MjcwNTZkOWMwNjFiNTUzNDY0MDlkZDViYTJjMmEwYTI4NDU3NjVmNGYxNWIwMjY2M2U3YWQxOTc3M2E4NzUyNDQxY2U4OWNjMmE5ODVlNzFhOGU5MDEzODc2MDIxZmI1NTljYWUxMTBiZWQ1ZjU3NmQ0NjQyYiIsImN0eSI6ImFwcGxpY2F0aW9uL2p3dCIsInR5cCI6IkpXVCIsImFsZyI6IkhTNTEyIn0.eyJzdWIiOiJIZWxsbyIsImciOiJNIiwiaCI6IkJfU046NDU3NDkwNjYwNzE1NDQwMzFfQ0xJX00iLCJpc3MiOiJCbHVlIiwiaSI6IjQ1NzQ5MDY2MDcxNTQ0MDMxIiwibiI6IlBWQVIiLCJhdWQiOiJCbHVlciIsInMiOiIxNjYxMzk5ODMyIiwibmJmIjoxNjYxMzk5ODMyLCJ0IjoicFBHckhpaE1TbGMiLCJlYXMiOjE2NjkxNzU4MzI3ODcsImV4cCI6MTY2OTE3NTgzMiwiaWF0IjoxNjYxMzk5ODMyLCJqdGkiOiJUUHNuRnNTMVVaZlp3T1lHdlRWMWFOVm53RzJwM2xXVCJ9.bTMZ4RkxGm5Zdi7SuT7j_8yFD9IpTHuNfIVjMHUggCqb3inkfgJ0NOJvGPkUZkx35AottO5TVSSfu_1i7utk9Q";

        String[] split = jwt.split("\\.");

        System.err.println(new String(Base64.getDecoder().decode(split[0])));
        System.err.println(new String(Base64.getDecoder().decode(split[1])));
        System.err.println(split[2]);
    }

}
