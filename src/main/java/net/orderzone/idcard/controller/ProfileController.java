package net.orderzone.idcard.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import net.orderzone.idcard.dto.ProfileForm;
import net.orderzone.idcard.model.BarcodeType;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.ProfileType;
import net.orderzone.idcard.service.IdCardRenderService;
import net.orderzone.idcard.service.PdfExportService;
import net.orderzone.idcard.service.PhotoStorageService;
import net.orderzone.idcard.service.ProfileService;
import net.orderzone.idcard.service.TemplateService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {

    private final ProfileService profileService;
    private final TemplateService templateService;
    private final PhotoStorageService photoStorageService;
    private final IdCardRenderService idCardRenderService;
    private final PdfExportService pdfExportService;

    public ProfileController(ProfileService profileService,
                             TemplateService templateService,
                             PhotoStorageService photoStorageService,
                             IdCardRenderService idCardRenderService,
                             PdfExportService pdfExportService) {
        this.profileService = profileService;
        this.templateService = templateService;
        this.photoStorageService = photoStorageService;
        this.idCardRenderService = idCardRenderService;
        this.pdfExportService = pdfExportService;
    }

    @GetMapping("/profiles")
    public String listProfiles(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Profile> profiles = profileService.search(keyword);
        model.addAttribute("profiles", profiles);
        model.addAttribute("keyword", keyword);
        return "profiles/list";
    }

    @GetMapping("/profiles/new")
    public String createForm(Model model) {
        ProfileForm form = new ProfileForm();
        model.addAttribute("form", form);
        model.addAttribute("templates", templateService.listAll());
        model.addAttribute("barcodeTypes", BarcodeType.values());
        model.addAttribute("profileTypes", ProfileType.values());
        return "profiles/form";
    }

    @PostMapping("/profiles")
    public String saveProfile(@Valid @ModelAttribute("form") ProfileForm form,
                              BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("templates", templateService.listAll());
            model.addAttribute("barcodeTypes", BarcodeType.values());
            model.addAttribute("profileTypes", ProfileType.values());
            return "profiles/form";
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

            profileService.create(profile);
            return "redirect:/profiles";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("templates", templateService.listAll());
            model.addAttribute("barcodeTypes", BarcodeType.values());
            model.addAttribute("profileTypes", ProfileType.values());
            return "profiles/form";
        }
    }

    @GetMapping("/profiles/{id}")
    public String viewProfile(@PathVariable Long id, Model model, HttpServletRequest request) {
        Profile profile = profileService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found: " + id));

        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        Map<String, Object> cardData = idCardRenderService.prepareCardData(profile, baseUrl);

        model.addAttribute("profile", profile);
        model.addAllAttributes(cardData);
        return "profiles/detail";
    }

    @GetMapping("/profiles/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Profile profile = profileService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found: " + id));

        ProfileForm form = new ProfileForm();
        form.setId(profile.getId());
        form.setFullName(profile.getFullName());
        form.setType(profile.getType());
        form.setDepartment(profile.getDepartment());
        form.setTitle(profile.getTitle());
        form.setEmail(profile.getEmail());
        form.setPhone(profile.getPhone());
        form.setBloodGroup(profile.getBloodGroup());
        form.setDateOfBirth(profile.getDateOfBirth());
        form.setIssueDate(profile.getIssueDate());
        form.setExpiryDate(profile.getExpiryDate());
        form.setBarcodeType(profile.getBarcodeType());
        form.setRegistrationNumber(profile.getRegistrationNumber());
        if (profile.getTemplate() != null) {
            form.setTemplateId(profile.getTemplate().getId());
        }

        model.addAttribute("form", form);
        model.addAttribute("templates", templateService.listAll());
        model.addAttribute("barcodeTypes", BarcodeType.values());
        model.addAttribute("profileTypes", ProfileType.values());
        return "profiles/form";
    }

    @PostMapping("/profiles/{id}")
    public String updateProfile(@PathVariable Long id,
                                @Valid @ModelAttribute("form") ProfileForm form,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("templates", templateService.listAll());
            model.addAttribute("barcodeTypes", BarcodeType.values());
            model.addAttribute("profileTypes", ProfileType.values());
            return "profiles/form";
        }

        try {
            Profile existing = profileService.getById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Profile not found: " + id));

            existing.setFullName(form.getFullName());
            existing.setType(form.getType());
            existing.setDepartment(form.getDepartment());
            existing.setTitle(form.getTitle());
            existing.setEmail(form.getEmail());
            existing.setPhone(form.getPhone());
            existing.setBloodGroup(form.getBloodGroup());
            existing.setDateOfBirth(form.getDateOfBirth());
            existing.setIssueDate(form.getIssueDate());
            existing.setExpiryDate(form.getExpiryDate());
            existing.setBarcodeType(form.getBarcodeType());
            existing.setRegistrationNumber(form.getRegistrationNumber());

            if (form.getTemplateId() != null) {
                templateService.getById(form.getTemplateId()).ifPresent(existing::setTemplate);
            } else {
                existing.setTemplate(null);
            }

            if (form.getPhotoFile() != null && !form.getPhotoFile().isEmpty()) {
                String fileName = photoStorageService.store(form.getPhotoFile());
                existing.setPhotoFileName(fileName);
                existing.setPhotoContentType(form.getPhotoFile().getContentType());
            }

            profileService.update(id, existing);
            return "redirect:/profiles/" + id;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("templates", templateService.listAll());
            model.addAttribute("barcodeTypes", BarcodeType.values());
            model.addAttribute("profileTypes", ProfileType.values());
            return "profiles/form";
        }
    }

    @PostMapping("/profiles/{id}/delete")
    public String deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
        return "redirect:/profiles";
    }

    @GetMapping("/profiles/{id}/card")
    public String previewCard(@PathVariable Long id, Model model, HttpServletRequest request) {
        Profile profile = profileService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found: " + id));

        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        Map<String, Object> cardData = idCardRenderService.prepareCardData(profile, baseUrl);

        model.addAttribute("profile", profile);
        model.addAllAttributes(cardData);
        return "profiles/card-preview";
    }

    @GetMapping("/profiles/{id}/pdf")
    @ResponseBody
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id, HttpServletRequest request) {
        Profile profile = profileService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found: " + id));

        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        byte[] pdfBytes = pdfExportService.generatePdfCard(profile, baseUrl);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"idcard-" + profile.getRegistrationNumber() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/verify/{uuid}")
    public String verifyProfile(@PathVariable String uuid, Model model, HttpServletRequest request) {
        Profile profile = profileService.getByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification code. Profile not found."));

        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        Map<String, Object> cardData = idCardRenderService.prepareCardData(profile, baseUrl);

        model.addAttribute("profile", profile);
        model.addAllAttributes(cardData);
        return "verify";
    }
}
