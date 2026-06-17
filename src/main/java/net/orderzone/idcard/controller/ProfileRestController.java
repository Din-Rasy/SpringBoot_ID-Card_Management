package net.orderzone.idcard.controller;

import jakarta.validation.Valid;
import net.orderzone.idcard.dto.ProfileForm;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.service.PhotoStorageService;
import net.orderzone.idcard.service.ProfileService;
import net.orderzone.idcard.service.TemplateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles")
public class ProfileRestController {

    private final ProfileService profileService;
    private final TemplateService templateService;
    private final PhotoStorageService photoStorageService;

    public ProfileRestController(ProfileService profileService,
                                 TemplateService templateService,
                                 PhotoStorageService photoStorageService) {
        this.profileService = profileService;
        this.templateService = templateService;
        this.photoStorageService = photoStorageService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createProfile(@Valid @ModelAttribute ProfileForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        try {
            Profile profile = new Profile();
            profile.setFullName(form.getFullName());
            profile.setType(form.getType());
            profile.setDepartment(form.getDepartment());
            profile.setTitle(form.getTitle());
            profile.setEmail(form.getEmail());
            profile.setPhone(form.getPhone());
            profile.setBloodGroup(form.getBloodGroup());
            profile.setDateOfBirth(form.getDateOfBirth());
            profile.setIssueDate(form.getIssueDate());
            profile.setExpiryDate(form.getExpiryDate());
            profile.setBarcodeType(form.getBarcodeType());
            profile.setRegistrationNumber(form.getRegistrationNumber());

            if (form.getTemplateId() != null) {
                templateService.getById(form.getTemplateId()).ifPresent(profile::setTemplate);
            }

            if (form.getPhotoFile() != null && !form.getPhotoFile().isEmpty()) {
                String fileName = photoStorageService.store(form.getPhotoFile());
                profile.setPhotoFileName(fileName);
                profile.setPhotoContentType(form.getPhotoFile().getContentType());
            }

            Profile created = profileService.create(profile);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
