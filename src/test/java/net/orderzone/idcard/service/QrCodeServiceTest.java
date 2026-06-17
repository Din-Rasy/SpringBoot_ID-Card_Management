package net.orderzone.idcard.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class QrCodeServiceTest {

    @Autowired
    private QrCodeService qrCodeService;

    @Test
    void testGenerateQrCode() {
        byte[] qrBytes = qrCodeService.generateQrCodeImage("http://localhost:8080/verify/some-uuid", 150, 150);
        assertNotNull(qrBytes);
        assertTrue(qrBytes.length > 0);
    }
}
