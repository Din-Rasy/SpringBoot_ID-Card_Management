package net.orderzone.idcard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class TemplateForm {

    private Long id;

    @NotBlank(message = "Template code is required")
    private String code;

    @NotBlank(message = "Template name is required")
    private String name;

    @NotBlank(message = "Organization name is required")
    private String organizationName;

    @NotBlank(message = "Layout must be either VERTICAL or HORIZONTAL")
    private String layout = "VERTICAL";

    @NotBlank(message = "Primary color is required")
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Primary color must be a valid hex code (e.g. #1d4ed8)")
    private String primaryColor = "#1d4ed8";

    @NotBlank(message = "Secondary color is required")
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Secondary color must be a valid hex code (e.g. #e0e7ff)")
    private String secondaryColor = "#e0e7ff";

    @NotBlank(message = "Text color is required")
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "Text color must be a valid hex code (e.g. #111827)")
    private String textColor = "#111827";

    private String tagline;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }
}
