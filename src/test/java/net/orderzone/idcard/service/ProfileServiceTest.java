package net.orderzone.idcard.service;

import net.orderzone.idcard.model.*;
import net.orderzone.idcard.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class ProfileServiceTest {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TemplateService templateService;

    @BeforeEach
    void setUp() {
        profileRepository.deleteAll();
    }

    @Test
    void testCreateProfileWithDefaults() {
        Profile profile = ProfileBuilder.builder(ProfileType.STUDENT)
                .fullName("Alice Smith")
                .department("IT")
                .build();

        Profile saved = profileService.create(profile);

        assertNotNull(saved.getId());
        assertNotNull(saved.getUuid());
        assertNotNull(saved.getRegistrationNumber());
        assertEquals("Student", saved.getTitle()); // Assert default title
        assertEquals(ProfileType.STUDENT, saved.getType());
        assertEquals("IT", saved.getDepartment());
        assertNotNull(saved.getTemplate());
        assertEquals("STUDENT_BLUE", saved.getTemplate().getCode()); // Default student template
    }

    @Test
    void testUpdateProfile() {
        Profile profile = ProfileBuilder.builder(ProfileType.EMPLOYEE)
                .fullName("Bob Jones")
                .department("HR")
                .build();
        Profile saved = profileService.create(profile);

        saved.setFullName("Bobby Jones");
        saved.setDepartment("Finance");
        Profile updated = profileService.update(saved.getId(), saved);

        assertEquals("Bobby Jones", updated.getFullName());
        assertEquals("Finance", updated.getDepartment());
    }

    @Test
    void testDeleteProfile() {
        Profile profile = ProfileBuilder.builder(ProfileType.USER)
                .fullName("Charlie Brown")
                .build();
        Profile saved = profileService.create(profile);
        Long id = saved.getId();

        profileService.deleteProfile(id);

        Optional<Profile> found = profileService.getById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void testSearchProfiles() {
        Profile p1 = ProfileBuilder.builder(ProfileType.STUDENT).fullName("Michael Jordan").department("Sports").build();
        Profile p2 = ProfileBuilder.builder(ProfileType.EMPLOYEE).fullName("Kobe Bryant").department("Management").build();
        profileService.create(p1);
        profileService.create(p2);

        List<Profile> searchName = profileService.search("Kobe");
        assertEquals(1, searchName.size());
        assertEquals("Kobe Bryant", searchName.get(0).getFullName());

        List<Profile> searchDept = profileService.search("Sports");
        assertEquals(1, searchDept.size());
        assertEquals("Michael Jordan", searchDept.get(0).getFullName());
    }

    @Test
    void testRegistrationNumberUniqueness() {
        Profile p1 = ProfileBuilder.builder(ProfileType.STUDENT).fullName("Student A").department("IT").build();
        Profile p2 = ProfileBuilder.builder(ProfileType.STUDENT).fullName("Student B").department("IT").build();

        Profile saved1 = profileService.create(p1);
        Profile saved2 = profileService.create(p2);

        assertNotNull(saved1.getRegistrationNumber());
        assertNotNull(saved2.getRegistrationNumber());
        assertNotEquals(saved1.getRegistrationNumber(), saved2.getRegistrationNumber());

        // Verifying sequence increment (e.g. 2026-IT-001 vs 2026-IT-002)
        String reg1 = saved1.getRegistrationNumber();
        String reg2 = saved2.getRegistrationNumber();

        assertTrue(reg1.endsWith("001") || reg1.endsWith("002"));
        assertTrue(reg2.endsWith("001") || reg2.endsWith("002"));
    }
}
