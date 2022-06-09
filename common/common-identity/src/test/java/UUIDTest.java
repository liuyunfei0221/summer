import com.blue.identity.api.common.BlueUuidGenerator;

public class UUIDTest {

    public static void main(String[] args) {

        String uuid = BlueUuidGenerator.generate();

        System.err.println(uuid);
        System.err.println(uuid.length());

    }

}
