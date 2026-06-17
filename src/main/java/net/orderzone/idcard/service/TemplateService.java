package net.orderzone.idcard.service;

import net.orderzone.idcard.model.Template;
import net.orderzone.idcard.repository.TemplateRepository;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public Template create(Template template) {
        if (templateRepository.existsByCode(template.getCode())) {
            throw new IllegalArgumentException("Template with code '" + template.getCode() + "' already exists.");
        }
        return templateRepository.save(template);
    }

    public Template update(Long id, Template updated) {
        Template existing = templateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found with ID: " + id));

        if (!existing.getCode().equals(updated.getCode()) && templateRepository.existsByCode(updated.getCode())) {
            throw new IllegalArgumentException("Template with code '" + updated.getCode() + "' already exists.");
        }

        existing.setCode(updated.getCode());
        existing.setName(updated.getName());
        existing.setOrganizationName(updated.getOrganizationName());
        existing.setLayout(updated.getLayout());
        existing.setPrimaryColor(updated.getPrimaryColor());
        existing.setSecondaryColor(updated.getSecondaryColor());
        existing.setTextColor(updated.getTextColor());
        existing.setTagline(updated.getTagline());

        return templateRepository.save(existing);
    }

    public void delete(Long id) {
        if (!templateRepository.existsById(id)) {
            throw new IllegalArgumentException("Template not found with ID: " + id);
        }
        templateRepository.deleteById(id);
    }

    public List<Template> listAll() {
        return templateRepository.findAll();
    }

    public List<Template> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return listAll();
        }
        return templateRepository.search(keyword);
    }

    public Optional<Template> getById(Long id) {
        return templateRepository.findById(id);
    }

    public Optional<Template> getByCode(String code) {
        return templateRepository.findByCode(code);
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seedDefaultTemplates() {
        if (templateRepository.count() == 0) {
            templateRepository.save(Template.builder()
                    .code("STUDENT_BLUE")
                    .name("Student Blue Theme")
                    .organizationName("National University")
                    .layout("VERTICAL")
                    .primaryColor("#1d4ed8")
                    .secondaryColor("#e0e7ff")
                    .textColor("#111827")
                    .tagline("Excellence in Education")
                    .build());

            templateRepository.save(Template.builder()
                    .code("EMPLOYEE_GREEN")
                    .name("Employee Green Theme")
                    .organizationName("Enterprise Corp")
                    .layout("HORIZONTAL")
                    .primaryColor("#047857")
                    .secondaryColor("#d1fae5")
                    .textColor("#111827")
                    .tagline("Innovating for the Future")
                    .build());

            templateRepository.save(Template.builder()
                    .code("USER_GRAY")
                    .name("User Gray Theme")
                    .organizationName("Community Hub")
                    .layout("VERTICAL")
                    .primaryColor("#374151")
                    .secondaryColor("#f3f4f6")
                    .textColor("#111827")
                    .tagline("Connect & Collaborate")
                    .build());
        }
    }
}
