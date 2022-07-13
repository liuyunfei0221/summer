import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {

        createHtmlFromModel();
        createHtmlFromString();

    }

    /**
     * 使用模板生成HTML代码
     */
    public static void createHtmlFromModel() {
        FileWriter out = null;
        try {
            // 通过FreeMarker的Confuguration读取相应的模板文件
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
            // 设置模板路径
            configuration.setClassForTemplateLoading(Test.class, "/ftl");
            // 设置默认字体
            configuration.setDefaultEncoding("utf-8");

            // 获取模板
            Template template = configuration.getTemplate("test1.ftl");

            //设置模型
            Map<String, Object> data = new HashMap<>();
            data.put("username", "liu");
            data.put("password", "pwd");
            data.put("age", 32);
            data.put("address", "beijing");

            //设置输出文件
            File file = new File("E:\\tempFile\\html\\test1.html");
            if (!file.exists()) {
                file.createNewFile();
            }
            //设置输出流
            out = new FileWriter(file);
            //模板输出静态文件
            template.process(data, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 把模板读入到String中，然后根据String构造FreeMarker模板
     */
    public static void createHtmlFromString() {
        BufferedInputStream in = null;
        FileWriter out = null;
        try {
            String content = "<!DOCTYPE html>\n" +
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

            //构造Configuration
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
            //构造StringTemplateLoader
            StringTemplateLoader loader = new StringTemplateLoader();
            //添加String模板
            loader.putTemplate("test", content);
            //把StringTemplateLoader添加到Configuration中
            configuration.setTemplateLoader(loader);

            //构造Model
            Map<String, Object> data = new HashMap<>();

            data.put("username", "liu");
            data.put("password", "pwd");
            data.put("age", 32);
            data.put("address", "beijing");

            //获取模板
            Template template = configuration.getTemplate("test");
            //构造输出路
            out = new FileWriter("E:\\tempFile\\html\\test2.html");
            //生成HTML
            template.process(data, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
