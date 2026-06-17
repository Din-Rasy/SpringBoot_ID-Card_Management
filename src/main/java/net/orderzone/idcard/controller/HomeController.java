package net.orderzone.idcard.controller;

import net.orderzone.idcard.model.ProfileType;
import net.orderzone.idcard.repository.ProfileRepository;
import net.orderzone.idcard.repository.TemplateRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProfileRepository profileRepository;
    private final TemplateRepository templateRepository;

    public HomeController(ProfileRepository profileRepository, TemplateRepository templateRepository) {
        this.profileRepository = profileRepository;
        this.templateRepository = templateRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalProfiles", profileRepository.count());
        model.addAttribute("studentCount", profileRepository.findByType(ProfileType.STUDENT).size());
        model.addAttribute("employeeCount", profileRepository.findByType(ProfileType.EMPLOYEE).size());
        model.addAttribute("userCount", profileRepository.findByType(ProfileType.USER).size());
        model.addAttribute("totalTemplates", templateRepository.count());
        return "index";
    }
}
