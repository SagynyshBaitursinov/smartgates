package utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by sagynysh on 4/26/17.
 */
public class QRGenerator {

    public static String generate(String text) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        BitMatrix byteMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 150, 150, hintMap);
        BufferedImage image = new BufferedImage(byteMatrix.getHeight(), byteMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, byteMatrix.getHeight(), byteMatrix.getHeight());
        graphics.setColor(Color.BLACK);
        for (int vert = 0; vert < byteMatrix.getHeight(); vert++) {
            for (int hor = 0; hor < byteMatrix.getHeight(); hor++) {
                if (byteMatrix.get(vert, hor)) {
                    graphics.fillRect(vert, hor, 1, 1);
                }
            }
        }
        File file = new File("/opt/smartgates/" + new Date().getTime() + ".png");
        file.createNewFile();
        ImageIO.write(image, "png", file);
        return file.getName();
    }
}
