package net.orderzone.idcard.controller;

import jakarta.servlet.http.HttpServletRequest;
import net.orderzone.idcard.dto.BatchRequest;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.ProfileType;
import net.orderzone.idcard.repository.ProfileRepository;
import net.orderzone.idcard.service.PdfExportService;
import net.orderzone.idcard.service.ProfileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BatchController {

    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final PdfExportService pdfExportService;

    public BatchController(ProfileService profileService,
                           ProfileRepository profileRepository,
                           PdfExportService pdfExportService) {
        this.profileService = profileService;
        this.profileRepository = profileRepository;
        this.pdfExportService = pdfExportService;
    }

    @GetMapping("/batch")
    public String batchPage(Model model) {
        model.addAttribute("profiles", profileService.listAll());
        model.addAttribute("profileTypes", ProfileType.values());
        
        List<String> departments = profileRepository.findAll()
                .stream()
                .map(Profile::getDepartment)
                .filter(d -> d != null && !d.isBlank())
                .distinct()
                .toList();
        model.addAttribute("departments", departments);
        model.addAttribute("batchRequest", new BatchRequest());
        return "batch/index";
    }

    @PostMapping("/batch/pdf")
    public ResponseEntity<byte[]> generateBatchPdf(@ModelAttribute("batchRequest") BatchRequest batchRequest,
                                                    HttpServletRequest request) {
        List<Profile> profiles = new ArrayList<>();

        if ("SELECTED".equals(batchRequest.getBatchMode())) {
            if (batchRequest.getSelectedIds() != null) {
                for (Long id : batchRequest.getSelectedIds()) {
                    profileService.getById(id).ifPresent(profiles::add);
                }
            }
        } else if ("TYPE".equals(batchRequest.getBatchMode())) {
            if (batchRequest.getType() != null) {
                profiles = profileService.findByType(batchRequest.getType());
            }
        } else if ("DEPARTMENT".equals(batchRequest.getBatchMode())) {
            if (batchRequest.getDepartment() != null && !batchRequest.getDepartment().isBlank()) {
                profiles = profileService.findByDepartment(batchRequest.getDepartment());
            }
        }

        if (profiles.isEmpty()) {
            return ResponseEntity.badRequest().body("No profiles matched the selection criteria.".getBytes());
        }

        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath());
        byte[] pdfBytes = pdfExportService.generateBatchPdfCards(profiles, baseUrl);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"batch-idcards.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
