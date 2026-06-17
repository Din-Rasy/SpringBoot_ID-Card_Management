package net.orderzone.idcard.service;

import net.orderzone.idcard.exception.StorageException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class PhotoStorageServiceTest {

    @Autowired
    private PhotoStorageService photoStorageService;

    @Test
    void testStoreValidJpg() {
        MockMultipartFile file = new MockMultipartFile(
                "photoFile",
                "test-image.jpg",
                "image/jpeg",
                "fake image content".getBytes()
        );

        String storedName = photoStorageService.store(file);
        assertNotNull(storedName);
        assertTrue(storedName.endsWith(".jpg") || storedName.endsWith(".jpeg"));
        
        // Cleanup file
        photoStorageService.delete(storedName);
    }

    @Test
    void testStoreInvalidContentType() {
        MockMultipartFile file = new MockMultipartFile(
                "photoFile",
                "test.txt",
                "text/plain",
                "fake text content".getBytes()
        );

        StorageException exception = assertThrows(StorageException.class, () -> {
            photoStorageService.store(file);
        });
        
        assertTrue(exception.getMessage().contains("Only JPEG and PNG images are allowed"));
    }

    @Test
    void testStoreExceedingSize() {
        // Size: 2.1 MB
        byte[] oversized = new byte[2 * 1024 * 1024 + 1024];
        MockMultipartFile file = new MockMultipartFile(
                "photoFile",
                "large.png",
                "image/png",
                oversized
        );

        StorageException exception = assertThrows(StorageException.class, () -> {
            photoStorageService.store(file);
        });

        assertTrue(exception.getMessage().contains("exceeds the maximum limit of 2MB"));
    }
}
