package net.orderzone.idcard.service;

import net.orderzone.idcard.model.*;
import net.orderzone.idcard.repository.ProfileRepository;
import net.orderzone.idcard.repository.TemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final TemplateRepository templateRepository;
    private final PhotoStorageService photoStorageService;

    public ProfileService(ProfileRepository profileRepository,
                          TemplateRepository templateRepository,
                          PhotoStorageService photoStorageService) {
        this.profileRepository = profileRepository;
        this.templateRepository = templateRepository;
        this.photoStorageService = photoStorageService;
    }

    @Transactional
    public Profile create(Profile profile) {
        if (profile.getUuid() == null || profile.getUuid().isBlank()) {
            profile.setUuid(generateUuid());
        }

        if (profileRepository.existsByUuid(profile.getUuid())) {
            throw new IllegalArgumentException("Profile with UUID '" + profile.getUuid() + "' already exists.");
        }

        if (profile.getTitle() == null || profile.getTitle().isBlank()) {
            switch (profile.getType()) {
                case STUDENT:
                    profile.setTitle("Student");
                    break;
                case EMPLOYEE:
                    profile.setTitle("Employee");
                    break;
                case USER:
                    profile.setTitle("User");
                    break;
            }
        }

        if (profile.getIssueDate() == null) {
            profile.setIssueDate(LocalDate.now());
        }

        if (profile.getExpiryDate() == null) {
            profile.setExpiryDate(LocalDate.now().plusYears(1));
        }

        if (profile.getBarcodeType() == null) {
            profile.setBarcodeType(BarcodeType.CODE_128);
        }

        if (profile.getTemplate() == null) {
            String defaultTemplateCode = "USER_GRAY";
            if (profile.getType() == ProfileType.STUDENT) {
                defaultTemplateCode = "STUDENT_BLUE";
            } else if (profile.getType() == ProfileType.EMPLOYEE) {
                defaultTemplateCode = "EMPLOYEE_GREEN";
            }
            Optional<Template> defaultTemplate = templateRepository.findByCode(defaultTemplateCode);
            defaultTemplate.ifPresent(profile::setTemplate);
        }

        if (profile.getRegistrationNumber() == null || profile.getRegistrationNumber().isBlank()) {
            profile.setRegistrationNumber(generateRegistrationNumber(profile.getDepartment()));
        }

        if (profileRepository.existsByRegistrationNumber(profile.getRegistrationNumber())) {
            throw new IllegalArgumentException("Registration number '" + profile.getRegistrationNumber() + "' is already in use.");
        }

        return profileRepository.save(profile);
    }

    @Transactional
    public Profile update(Long id, Profile updated) {
        Profile existing = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with ID: " + id));

        if (!existing.getRegistrationNumber().equals(updated.getRegistrationNumber()) &&
            profileRepository.existsByRegistrationNumber(updated.getRegistrationNumber())) {
            throw new IllegalArgumentException("Registration number '" + updated.getRegistrationNumber() + "' is already in use.");
        }

        existing.setFullName(updated.getFullName());
        existing.setType(updated.getType());
        existing.setDepartment(updated.getDepartment());
        existing.setTitle(updated.getTitle());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setBloodGroup(updated.getBloodGroup());
        existing.setDateOfBirth(updated.getDateOfBirth());
        existing.setIssueDate(updated.getIssueDate());
        existing.setExpiryDate(updated.getExpiryDate());
        existing.setBarcodeType(updated.getBarcodeType());
        existing.setRegistrationNumber(updated.getRegistrationNumber());

        if (updated.getPhotoFileName() != null && !updated.getPhotoFileName().isBlank()) {
            if (existing.getPhotoFileName() != null && !existing.getPhotoFileName().equals(updated.getPhotoFileName())) {
                photoStorageService.delete(existing.getPhotoFileName());
            }
            existing.setPhotoFileName(updated.getPhotoFileName());
            existing.setPhotoContentType(updated.getPhotoContentType());
        }

        if (updated.getTemplate() != null) {
            existing.setTemplate(updated.getTemplate());
        }

        return profileRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        Profile existing = profileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with ID: " + id));
        
        if (existing.getPhotoFileName() != null) {
            photoStorageService.delete(existing.getPhotoFileName());
        }
        
        profileRepository.delete(existing);
    }

    public Optional<Profile> getById(Long id) {
        return profileRepository.findById(id);
    }

    public Optional<Profile> getByUuid(String uuid) {
        return profileRepository.findByUuid(uuid);
    }

    public List<Profile> listAll() {
        return profileRepository.findAll();
    }

    public List<Profile> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return listAll();
        }
        return profileRepository.search(keyword);
    }

    public List<Profile> findByType(ProfileType type) {
        return profileRepository.findByType(type);
    }

    public List<Profile> findByDepartment(String department) {
        return profileRepository.findByDepartment(department);
    }

    public String generateUuid() {
        return java.util.UUID.randomUUID().toString();
    }

    public synchronized String generateRegistrationNumber(String department) {
        String dept = (department == null || department.trim().isEmpty()) ? "GEN" : department.trim().toUpperCase();
        int year = LocalDate.now().getYear();
        String prefix = year + "-" + dept + "-";

        Optional<Profile> latestProfile = profileRepository.findFirstByRegistrationNumberStartingWithOrderByRegistrationNumberDesc(prefix);
        int nextSeq = 1;
        if (latestProfile.isPresent()) {
            String regNum = latestProfile.get().getRegistrationNumber();
            try {
                String seqStr = regNum.substring(prefix.length());
                nextSeq = Integer.parseInt(seqStr) + 1;
            } catch (Exception e) {
                // Ignore, fallback uses next sequence increment attempts
            }
        }
        String regNum = String.format("%s%03d", prefix, nextSeq);

        int attempts = 0;
        while (profileRepository.existsByRegistrationNumber(regNum) && attempts < 100) {
            nextSeq++;
            regNum = String.format("%s%03d", prefix, nextSeq);
            attempts++;
        }
        return regNum;
    }
}
