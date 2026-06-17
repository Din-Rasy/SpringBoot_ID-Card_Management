package net.orderzone.idcard.controller;

import net.orderzone.idcard.model.*;
import net.orderzone.idcard.repository.ProfileRepository;
import net.orderzone.idcard.repository.TemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class IdCardWebIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TemplateRepository templateRepository;

    private Profile testProfile;
    private Template testTemplate;

    @BeforeEach
    void setUp() {
        profileRepository.deleteAll();

        // Retrieve seeded template or create one
        testTemplate = templateRepository.findByCode("STUDENT_BLUE")
                .orElseGet(() -> templateRepository.save(
                        Template.builder()
                                .code("STUDENT_BLUE")
                                .name("Student Blue")
                                .organizationName("National Uni")
                                .layout("VERTICAL")
                                .primaryColor("#1d4ed8")
                                .secondaryColor("#e0e7ff")
                                .textColor("#111827")
                                .tagline("Learn")
                                .build()
                ));

        // Register a test profile
        Profile profile = new Profile();
        profile.setUuid("test-uuid-12345");
        profile.setFullName("Automated Tester");
        profile.setType(ProfileType.STUDENT);
        profile.setDepartment("QA");
        profile.setTitle("SDET");
        profile.setEmail("tester@test.com");
        profile.setPhone("099999999");
        profile.setBloodGroup("O+");
        profile.setRegistrationNumber("2026-QA-001");
        profile.setTemplate(testTemplate);
        profile.setBarcodeType(BarcodeType.CODE_128);
        testProfile = profileRepository.save(profile);
    }

    @Test
    void testDashboardRoute() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Dashboard")))
                .andExpect(content().string(containsString("Total Profiles")));
    }

    @Test
    void testProfilesListRoute() throws Exception {
        mockMvc.perform(get("/profiles"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Profiles")))
                .andExpect(content().string(containsString("Automated Tester")));
    }

    @Test
    void testProfileCreateFormRoute() throws Exception {
        mockMvc.perform(get("/profiles/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Register Profile")));
    }

    @Test
    void testProfileCreationPostRoute() throws Exception {
        mockMvc.perform(post("/profiles")
                        .param("fullName", "New User")
                        .param("type", "EMPLOYEE")
                        .param("department", "HR")
                        .param("title", "Manager")
                        .param("email", "manager@test.com")
                        .param("phone", "987654321")
                        .param("templateId", testTemplate.getId().toString())
                        .param("barcodeType", "CODE_128"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles"));
    }

    @Test
    void testProfileDetailRoute() throws Exception {
        mockMvc.perform(get("/profiles/" + testProfile.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Automated Tester")))
                .andExpect(content().string(containsString("2026-QA-001")))
                .andExpect(content().string(containsString("data:image/png;base64")));
    }

    @Test
    void testProfileCardPreviewRoute() throws Exception {
        mockMvc.perform(get("/profiles/" + testProfile.getId() + "/card"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("2026-QA-001")));
    }

    @Test
    void testProfilePdfDownloadRoute() throws Exception {
        mockMvc.perform(get("/profiles/" + testProfile.getId() + "/pdf"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("attachment")))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    void testProfileVerificationRoute() throws Exception {
        mockMvc.perform(get("/verify/" + testProfile.getUuid()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Authenticity Confirmed")))
                .andExpect(content().string(containsString("Automated Tester")));
    }

    @Test
    void testTemplatesListRoute() throws Exception {
        mockMvc.perform(get("/templates"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Card Templates")))
                .andExpect(content().string(containsString("STUDENT_BLUE")));
    }

    @Test
    void testTemplateCreatePostRoute() throws Exception {
        mockMvc.perform(post("/templates")
                        .param("code", "GOLD_HORIZONTAL")
                        .param("name", "Gold Theme")
                        .param("organizationName", "Gold Inc")
                        .param("layout", "HORIZONTAL")
                        .param("primaryColor", "#d4af37")
                        .param("secondaryColor", "#fef3c7")
                        .param("textColor", "#451a03"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/templates"));
    }

    @Test
    void testBatchPdfExportRoute() throws Exception {
        mockMvc.perform(post("/batch/pdf")
                        .param("batchMode", "SELECTED")
                        .param("selectedIds", testProfile.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("attachment")))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }
}
