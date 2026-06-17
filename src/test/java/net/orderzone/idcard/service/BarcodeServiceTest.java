package net.orderzone.idcard.service;

import net.orderzone.idcard.model.BarcodeType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BarcodeServiceTest {

    @Autowired
    private BarcodeService barcodeService;

    @Test
    void testGenerateCode128() {
        byte[] bytes = barcodeService.generateBarcode("2026-IT-001", BarcodeType.CODE_128, 200, 50);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test
    void testGenerateEan13WithValidNumeric() {
        byte[] bytes = barcodeService.generateBarcode("123456789012", BarcodeType.EAN_13, 200, 50);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test
    void testGenerateEan13WithNonNumericFallback() {
        // "2026-IT-001" has only 7 digits (2026001), which is < 12 digits, and thus falls back to CODE_128.
        byte[] bytes = barcodeService.generateBarcode("2026-IT-001", BarcodeType.EAN_13, 200, 50);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }
}
