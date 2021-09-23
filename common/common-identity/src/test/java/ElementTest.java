import com.blue.identity.api.common.BlueIdentityParser;
import com.blue.identity.model.IdentityElement;

public class ElementTest {

    public static void main(String[] args) {

        IdentityElement parse = BlueIdentityParser.parse(11330128021815299L);

        System.err.println(parse);

    }

}
