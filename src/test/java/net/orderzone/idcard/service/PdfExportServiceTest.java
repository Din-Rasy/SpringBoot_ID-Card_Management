package net.orderzone.idcard.service;

import net.orderzone.idcard.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PdfExportServiceTest {

    @Autowired
    private PdfExportService pdfExportService;

    @Autowired
    private ProfileService profileService;

    @Test
    void testGeneratePdfCard() {
        Profile profile = ProfileBuilder.builder(ProfileType.STUDENT)
                .fullName("John Doe")
                .department("IT")
                .build();
        
        Profile saved = profileService.create(profile);
        byte[] pdfBytes = pdfExportService.generatePdfCard(saved, "http://localhost:8080");
        
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void testGenerateBatchPdfCards() {
        Profile p1 = ProfileBuilder.builder(ProfileType.STUDENT).fullName("John Doe").department("IT").build();
        Profile p2 = ProfileBuilder.builder(ProfileType.EMPLOYEE).fullName("Jane Doe").department("HR").build();
        
        Profile saved1 = profileService.create(p1);
        Profile saved2 = profileService.create(p2);
        
        byte[] pdfBytes = pdfExportService.generateBatchPdfCards(List.of(saved1, saved2), "http://localhost:8080");
        
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }
}
