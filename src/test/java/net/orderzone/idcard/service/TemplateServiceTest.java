package net.orderzone.idcard.service;

import net.orderzone.idcard.model.Template;
import net.orderzone.idcard.repository.TemplateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TemplateServiceTest {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplateRepository templateRepository;

    @Test
    void testDefaultSeedingOnStartup() {
        // Since ApplicationReadyEvent triggers on boot, seed templates should already exist.
        assertTrue(templateRepository.count() >= 3);

        Optional<Template> studentBlue = templateService.getByCode("STUDENT_BLUE");
        assertTrue(studentBlue.isPresent());
        assertEquals("Student Blue Theme", studentBlue.get().getName());
        assertEquals("#1d4ed8", studentBlue.get().getPrimaryColor());

        Optional<Template> employeeGreen = templateService.getByCode("EMPLOYEE_GREEN");
        assertTrue(employeeGreen.isPresent());
        assertEquals("Employee Green Theme", employeeGreen.get().getName());
        assertEquals("#047857", employeeGreen.get().getPrimaryColor());

        Optional<Template> userGray = templateService.getByCode("USER_GRAY");
        assertTrue(userGray.isPresent());
        assertEquals("User Gray Theme", userGray.get().getName());
        assertEquals("#374151", userGray.get().getPrimaryColor());
    }

    @Test
    void testCreateTemplate() {
        Template custom = Template.builder()
                .code("CUSTOM_RED")
                .name("Custom Red Theme")
                .organizationName("Red Agency")
                .layout("HORIZONTAL")
                .primaryColor("#ef4444")
                .secondaryColor("#fee2e2")
                .textColor("#1f2937")
                .build();

        Template saved = templateService.create(custom);
        assertNotNull(saved.getId());
        assertEquals("CUSTOM_RED", saved.getCode());

        // Test search
        List<Template> results = templateService.search("Red");
        assertEquals(1, results.size());
        assertEquals("CUSTOM_RED", results.get(0).getCode());
    }
}
