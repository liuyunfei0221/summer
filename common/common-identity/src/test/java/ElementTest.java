import com.blue.identity.api.common.BlueIdentityParser;
import com.blue.identity.model.IdentityElement;

public class ElementTest {

    public static void main(String[] args) {

        IdentityElement parse = BlueIdentityParser.parse(32128188251340801L);

        System.err.println(parse);

    }

}
