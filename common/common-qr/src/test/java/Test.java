import com.blue.base.model.exps.BlueException;
import com.blue.qr.api.generator.BlueQrCoderGenerator;
import com.blue.qr.component.QrCoder;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.blue.base.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;

@SuppressWarnings("JavaDoc")
public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {

        QrCoder qrCoder = BlueQrCoderGenerator.generateQrCoder();

        int threads = Runtime.getRuntime().availableProcessors();
        int blockingQueueSize = 2048;

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threads >>> 1, threads,
                64L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(blockingQueueSize), Thread::new, (r, executor) -> {
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "线程池任务堆积: " + r.toString());
        });

        for (int i = 0, end = blockingQueueSize >>> 1; i <= end; i++) {
            final int c = i;
            threadPoolExecutor.submit(() -> test1(c, qrCoder));
            threadPoolExecutor.submit(() -> test2(c, qrCoder));
        }

    }

    private static void testParse(String filePath, QrCoder qrCoder) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
            byte[] qrData = bufferedInputStream.readAllBytes();

            String result = qrCoder.parseCode(qrData);
            System.err.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test1(int c, QrCoder qrCoder) {
        String content = "看看这个无图二维码里面装的是啥 -> //www.baidu度度度+" + c + ".com//";
        String qrDesc = "E:\\tempTests\\qrcode\\a" + c + ".jpg";
        try (FileOutputStream fileOutputStream = new FileOutputStream(qrDesc);
             final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {
            byte[] qrData = qrCoder.generateCodeWithoutLogo(content);
            bufferedOutputStream.write(qrData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        testParse(qrDesc, qrCoder);

    }

    private static void test2(int c, QrCoder qrCoder) {
        String content = "看看这个有图二维码里面装的是啥 -> //www.baidu度度度+" + c + ".com//";
        String logoPath = "E:\\tempTests\\logo.png";
        String qrDesc = "E:\\tempTests\\qrcode\\b" + c + ".jpg";
        try (FileInputStream fileInputStream = new FileInputStream(logoPath);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(qrDesc);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {

            byte[] logoData = bufferedInputStream.readAllBytes();
            byte[] qrData = qrCoder.generateCodeWithLogo(content, logoData);
            bufferedOutputStream.write(qrData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        testParse(qrDesc, qrCoder);

    }

}
