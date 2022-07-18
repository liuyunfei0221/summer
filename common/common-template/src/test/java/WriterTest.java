import com.blue.template.api.generator.TemplateWriterGenerator;
import com.blue.template.component.PackageFileTemplateWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class WriterTest {

    public static void main(String[] args) {
        testPackageFileWriter();
    }

    private static void testPackageFileWriter() {
        String basePackagePath = "/ftl";
        PackageFileTemplateWriter packageFileTemplateWriter = TemplateWriterGenerator.generatePackageFileTemplateWriter(() -> basePackagePath);

        FileWriter writer = null;
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("username", "liu");
            data.put("password", "pwd");
            data.put("age", 32);
            data.put("address", "beijing");

            File file = new File("E:\\tempFile\\html\\testPackageFileWriter.html");

            writer = new FileWriter(file);

            packageFileTemplateWriter.write("test1.ftl", data, writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
