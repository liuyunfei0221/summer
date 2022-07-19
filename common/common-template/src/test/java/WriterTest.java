import com.blue.template.api.conf.StringContentAttr;
import com.blue.template.api.generator.TemplateWriterGenerator;
import com.blue.template.component.UriFileTemplateWriter;
import com.blue.template.component.StringContentTemplateWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;

public class WriterTest {

    public static void main(String[] args) {
        testPackageFileWriter();
        testStringContentWriter();
    }

    private static void testPackageFileWriter() {
        String basePackagePath = "classpath:ftl";
        UriFileTemplateWriter uriFileTemplateWriter = TemplateWriterGenerator.generateUriFileTemplateWriter(() -> basePackagePath);

        FileWriter writer = null;
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("username", "liu");
            data.put("password", "pwd");
            data.put("age", 32);
            data.put("address", "beijing");

            File file = new File("E:\\tempFile\\html\\testPackageFileWriter.html");

            writer = new FileWriter(file);

            uriFileTemplateWriter.write("test1.ftl", data, writer);
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

    private static void testStringContentWriter() {
        String templateName = "temp1";
        String templateContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>用户信息</title>\n" +
                "    <!-- 新 Bootstrap 核心 CSS 文件 -->\n" +
                "    <link rel=\"stylesheet\"\n" +
                "          href=\"http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css\" />\n" +
                "</head>\n" +
                "<body style=\"font-family:'Courier New'\">\n" +
                "<h3 class=\"text-center\">这是用户${username}的信息页！</h3>\n" +
                "<div class=\"col-md-6 column\">\n" +
                "    <table class=\"table table-bordered\">\n" +
                "        <tr>\n" +
                "            <th>用户名</th>\n" +
                "            <th>密码</th>\n" +
                "            <th>年龄</th>\n" +
                "            <th>地址</th>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>${username}</td>\n" +
                "            <td>${password}</td>\n" +
                "            <td>${age}</td>\n" +
                "            <td>${address}</td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        StringContentTemplateWriter stringContentTemplateWriter = TemplateWriterGenerator.generateStringContentTemplateWriter(() ->
                singletonList(new StringContentAttr(templateName,templateContent)));

        FileWriter writer = null;
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("username", "liu");
            data.put("password", "pwd");
            data.put("age", 32);
            data.put("address", "beijing");

            File file = new File("E:\\tempFile\\html\\testStringContentWriter.html");

            writer = new FileWriter(file);

            stringContentTemplateWriter.write("temp1", data, writer);
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
