import com.blue.identity.common.BlueIdentityParser;
import com.blue.identity.model.IdentityElement;

public class ElementTest {

    public static void main(String[] args) {

        IdentityElement parse = BlueIdentityParser.parse(43721902711111681L);

        System.err.println(parse);

    }

}
