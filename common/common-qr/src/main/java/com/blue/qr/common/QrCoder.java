package com.blue.qr.common;


import com.blue.base.model.exps.BlueException;
import com.blue.qr.api.conf.QrConf;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import org.slf4j.Logger;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.google.zxing.BarcodeFormat.QR_CODE;
import static com.google.zxing.DecodeHintType.PURE_BARCODE;
import static com.google.zxing.EncodeHintType.ERROR_CORRECTION;
import static com.google.zxing.EncodeHintType.MARGIN;
import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;
import static com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.H;
import static java.awt.BasicStroke.CAP_ROUND;
import static java.awt.BasicStroke.JOIN_ROUND;
import static javax.imageio.ImageIO.read;
import static javax.imageio.ImageIO.write;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * qrcoder
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "WeakerAccess", "PlaceholderCountMatchesArgumentCount", "AliControlFlowStatementWithoutBraces", "SpellCheckingInspection"})
public final class QrCoder {

    private static final Logger LOGGER = getLogger(QrCoder.class);

    private static final String CHARSET = "UTF-8";
    private static final int LOGO_AND_FRAME_ROUND_ARCW = 20;
    private static final int LOGO_AND_FRAME_ROUND_ARCH = 20;
    private static final String CODE_FORMAT = "jpg";
    private static final MultiFormatWriter MULTI_FORMAT_WRITER = new MultiFormatWriter();

    private static final Map<EncodeHintType, Object> ENCODE_CODE_PARAMS = new HashMap<>(4);
    private static final Map<DecodeHintType, Object> DECODE_CODE_PARAMS = new HashMap<>(4);

    static {
        ENCODE_CODE_PARAMS.put(EncodeHintType.CHARACTER_SET, CHARSET);
        ENCODE_CODE_PARAMS.put(ERROR_CORRECTION, H);
        ENCODE_CODE_PARAMS.put(MARGIN, 1);
        DECODE_CODE_PARAMS.put(DecodeHintType.CHARACTER_SET, CHARSET);
        DECODE_CODE_PARAMS.put(PURE_BARCODE, true);
    }


    private final int width;
    private final int height;
    private final int logoRoundX;
    private final int logoRoundY;
    private final int logoRoundWidth;
    private final int logoRoundHeight;
    private final RoundRectangle2D.Float logoRound;
    private final MatrixToImageConfig matrixToImageConfig;
    private final RoundRectangle2D.Float frameRound;
    private final BasicStroke stroke;
    private final Color logoFrameColor;
    private final Color frameColor;


    public QrCoder(QrConf qrConf) {
        this.width = qrConf.getWidth();
        this.height = qrConf.getHeight();

        this.logoRoundX = width / 5 * 2;
        this.logoRoundY = height / 5 * 2;
        this.logoRoundWidth = width / 5;
        this.logoRoundHeight = height / 5;

        this.logoRound = new RoundRectangle2D.Float(logoRoundX, logoRoundY, logoRoundWidth, logoRoundHeight, LOGO_AND_FRAME_ROUND_ARCW, LOGO_AND_FRAME_ROUND_ARCH);
        this.matrixToImageConfig = new MatrixToImageConfig(qrConf.getOnColor(), qrConf.getOffColor());
        this.frameRound =
                new RoundRectangle2D.Float(logoRoundX + 2, logoRoundY + 2, logoRoundWidth - 4, logoRoundHeight - 4, LOGO_AND_FRAME_ROUND_ARCW, LOGO_AND_FRAME_ROUND_ARCH);
        this.frameColor = qrConf.getFrameColor();
        this.stroke = new BasicStroke(qrConf.getStrokesWidth(), CAP_ROUND, JOIN_ROUND);
        this.logoFrameColor = qrConf.getLogoFrameColor();
    }

    /**
     * parse qrcode
     *
     * @param qrData
     * @return
     */
    public static String parseCode(byte[] qrData) {
        if (qrData == null || qrData.length < 1)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(qrData);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream)) {

            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(read(bufferedInputStream))));
            Result result = new MultiFormatReader().decode(binaryBitmap, DECODE_CODE_PARAMS);

            return result.getText();
        } catch (Exception e) {
            LOGGER.error("parseCode(byte[] qrData), e = {}", e);
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "parse data failed");
        }
    }

    /**
     * generate code without logo
     *
     * @param content
     * @return
     */
    public byte[] generateCodeWithoutLogo(String content) {
        if (content == null || "".equals(content))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream)) {

            BitMatrix bitMatrix = MULTI_FORMAT_WRITER.encode(content, QR_CODE, width, height, ENCODE_CODE_PARAMS);
            MatrixToImageWriter.writeToStream(bitMatrix, CODE_FORMAT, bufferedOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            LOGGER.error("generateCodeWithoutLogo(String content), e = {}", e);
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "parse data failed");
        }

    }

    /**
     * generate code with logo
     *
     * @param content
     * @param logoData
     * @return
     */
    public byte[] generateCodeWithLogo(String content, byte[] logoData) {
        if (content == null || "".equals(content))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message);

        if (logoData == null || logoData.length < 1)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, BAD_REQUEST.message);

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(logoData);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream)) {

            BitMatrix bitMatrix = MULTI_FORMAT_WRITER.encode(content, QR_CODE, width, height, ENCODE_CODE_PARAMS);
            BufferedImage qrImage = toBufferedImage(bitMatrix, matrixToImageConfig);
            Graphics2D qrGraphics = qrImage.createGraphics();

            BufferedImage logoImage = read(bufferedInputStream);

            qrGraphics.drawImage(logoImage, logoRoundX, logoRoundY, logoRoundWidth, logoRoundHeight, null);
            qrGraphics.setStroke(stroke);
            qrGraphics.setColor(logoFrameColor);

            qrGraphics.draw(logoRound);

            qrGraphics.setStroke(stroke);
            qrGraphics.setColor(frameColor);

            qrGraphics.draw(frameRound);

            qrGraphics.dispose();
            qrImage.flush();

            write(qrImage, CODE_FORMAT, bufferedOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            LOGGER.error("generateCodeWithLogo(String content, byte[] logoData), e = ", e);
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "parse data failed");
        }

    }

}
