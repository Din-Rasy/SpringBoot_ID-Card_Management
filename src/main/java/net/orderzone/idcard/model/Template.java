package net.orderzone.idcard.model;

import jakarta.persistence.*;

/**
 * A reusable ID-card theme. Colours drive both the live HTML preview and the
 * iText PDF rendering so the two stay visually consistent.
 */
@Entity
@Table(name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String code;

    @Column(nullable = false, length = 80)
    private String name;

    /** Organisation / institution name printed on the card header. */
    @Column(length = 120)
    private String organizationName;

    /** Layout key: VERTICAL or HORIZONTAL. */
    @Column(nullable = false, length = 20)
    private String layout = "VERTICAL";

    /** Primary brand colour as hex, e.g. #1d4ed8. */
    @Column(nullable = false, length = 7)
    private String primaryColor = "#1d4ed8";

    @Column(nullable = false, length = 7)
    private String secondaryColor = "#e0e7ff";

    @Column(nullable = false, length = 7)
    private String textColor = "#111827";

    @Column(length = 255)
    private String tagline;

    // Constructors
    public Template() {
    }

    public Template(Long id, String code, String name, String organizationName, String layout, String primaryColor, String secondaryColor, String textColor, String tagline) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.organizationName = organizationName;
        this.layout = layout != null ? layout : "VERTICAL";
        this.primaryColor = primaryColor != null ? primaryColor : "#1d4ed8";
        this.secondaryColor = secondaryColor != null ? secondaryColor : "#e0e7ff";
        this.textColor = textColor != null ? textColor : "#111827";
        this.tagline = tagline;
    }

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

    // Builder pattern
    public static TemplateBuilder builder() {
        return new TemplateBuilder();
    }

    public static class TemplateBuilder {
        private Long id;
        private String code;
        private String name;
        private String organizationName;
        private String layout = "VERTICAL";
        private String primaryColor = "#1d4ed8";
        private String secondaryColor = "#e0e7ff";
        private String textColor = "#111827";
        private String tagline;

        public TemplateBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TemplateBuilder code(String code) {
            this.code = code;
            return this;
        }

        public TemplateBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TemplateBuilder organizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public TemplateBuilder layout(String layout) {
            this.layout = layout;
            return this;
        }

        public TemplateBuilder primaryColor(String primaryColor) {
            this.primaryColor = primaryColor;
            return this;
        }

        public TemplateBuilder secondaryColor(String secondaryColor) {
            this.secondaryColor = secondaryColor;
            return this;
        }

        public TemplateBuilder textColor(String textColor) {
            this.textColor = textColor;
            return this;
        }

        public TemplateBuilder tagline(String tagline) {
            this.tagline = tagline;
            return this;
        }

        public Template build() {
            return new Template(id, code, name, organizationName, layout, primaryColor, secondaryColor, textColor, tagline);
        }
    }
}
