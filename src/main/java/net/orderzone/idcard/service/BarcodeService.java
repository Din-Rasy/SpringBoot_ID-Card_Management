package net.orderzone.idcard.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import net.orderzone.idcard.model.BarcodeType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class BarcodeService {

    public byte[] generateBarcode(String text, BarcodeType type, int width, int height) {
        if (type == BarcodeType.EAN_13) {
            // EAN-13 requires exactly 12 or 13 digits.
            String digits = text.replaceAll("\\D", "");
            if (digits.length() < 12) {
                // If invalid numeric data for EAN-13, fallback to CODE_128
                return generateBarcode(text, BarcodeType.CODE_128, width, height);
            }
            String ean12 = digits.substring(0, 12);
            int checkDigit = calculateEan13CheckDigit(ean12);
            String ean13 = ean12 + checkDigit;
            try {
                return generateBarcodeImage(ean13, BarcodeFormat.EAN_13, width, height);
            } catch (Exception e) {
                // Fallback to CODE_128 on error
                return generateBarcode(text, BarcodeType.CODE_128, width, height);
            }
        } else {
            try {
                return generateBarcodeImage(text, BarcodeFormat.CODE_128, width, height);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate CODE_128 barcode", e);
            }
        }
    }

    private byte[] generateBarcodeImage(String text, BarcodeFormat format, int width, int height) throws Exception {
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(text, format, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    private int calculateEan13CheckDigit(String ean12) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(ean12.charAt(i));
            if (i % 2 == 0) {
                sum += digit;
            } else {
                sum += digit * 3;
            }
        }
        int mod = sum % 10;
        return (mod == 0) ? 0 : (10 - mod);
    }
}
