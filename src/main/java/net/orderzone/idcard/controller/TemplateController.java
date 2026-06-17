package net.orderzone.idcard.controller;

import jakarta.validation.Valid;
import net.orderzone.idcard.dto.TemplateForm;
import net.orderzone.idcard.model.Template;
import net.orderzone.idcard.service.TemplateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/templates")
    public String listTemplates(Model model) {
        List<Template> templates = templateService.listAll();
        model.addAttribute("templates", templates);
        return "templates/list";
    }

    @GetMapping("/templates/new")
    public String createForm(Model model) {
        TemplateForm form = new TemplateForm();
        model.addAttribute("form", form);
        return "templates/form";
    }

    @PostMapping("/templates")
    public String saveTemplate(@Valid @ModelAttribute("form") TemplateForm form,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "templates/form";
        }

        try {
            Template template = Template.builder()
                    .code(form.getCode())
                    .name(form.getName())
                    .organizationName(form.getOrganizationName())
                    .layout(form.getLayout())
                    .primaryColor(form.getPrimaryColor())
                    .secondaryColor(form.getSecondaryColor())
                    .textColor(form.getTextColor())
                    .tagline(form.getTagline())
                    .build();

            templateService.create(template);
            return "redirect:/templates";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "templates/form";
        }
    }

    @GetMapping("/templates/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Template template = templateService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));

        TemplateForm form = new TemplateForm();
        form.setId(template.getId());
        form.setCode(template.getCode());
        form.setName(template.getName());
        form.setOrganizationName(template.getOrganizationName());
        form.setLayout(template.getLayout());
        form.setPrimaryColor(template.getPrimaryColor());
        form.setSecondaryColor(template.getSecondaryColor());
        form.setTextColor(template.getTextColor());
        form.setTagline(template.getTagline());

        model.addAttribute("form", form);
        return "templates/form";
    }

    @PostMapping("/templates/{id}")
    public String updateTemplate(@PathVariable Long id,
                                 @Valid @ModelAttribute("form") TemplateForm form,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "templates/form";
        }

        try {
            Template updated = Template.builder()
                    .code(form.getCode())
                    .name(form.getName())
                    .organizationName(form.getOrganizationName())
                    .layout(form.getLayout())
                    .primaryColor(form.getPrimaryColor())
                    .secondaryColor(form.getSecondaryColor())
                    .textColor(form.getTextColor())
                    .tagline(form.getTagline())
                    .build();

            templateService.update(id, updated);
            return "redirect:/templates";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "templates/form";
        }
    }

    @PostMapping("/templates/{id}/delete")
    public String deleteTemplate(@PathVariable Long id) {
        templateService.delete(id);
        return "redirect:/templates";
    }
}
