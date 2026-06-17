package net.orderzone.idcard.model;

import java.time.LocalDate;
import java.util.UUID;

public class ProfileBuilder {
    private ProfileType type;
    private String fullName;
    private String department;
    private String title;
    private String email;
    private String phone;
    private String bloodGroup;
    private LocalDate dateOfBirth;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Template template;
    private BarcodeType barcodeType;

    private ProfileBuilder(ProfileType type) {
        this.type = type;
        // Default title based on type
        if (type != null) {
            switch (type) {
                case STUDENT:
                    this.title = "Student";
                    break;
                case EMPLOYEE:
                    this.title = "Employee";
                    break;
                case USER:
                    this.title = "User";
                    break;
            }
        }
        this.issueDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusYears(1); // Default 1 year validity
        this.barcodeType = BarcodeType.CODE_128;
    }

    public static ProfileBuilder builder(ProfileType type) {
        return new ProfileBuilder(type);
    }

    public ProfileBuilder fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public ProfileBuilder department(String department) {
        this.department = department;
        return this;
    }

    public ProfileBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ProfileBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ProfileBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public ProfileBuilder bloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
        return this;
    }

    public ProfileBuilder dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public ProfileBuilder issueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
        return this;
    }

    public ProfileBuilder expiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public ProfileBuilder template(Template template) {
        this.template = template;
        return this;
    }

    public ProfileBuilder barcodeType(BarcodeType barcodeType) {
        this.barcodeType = barcodeType;
        return this;
    }

    public Profile build() {
        Profile profile = new Profile();
        profile.setUuid(UUID.randomUUID().toString());
        profile.setType(this.type);
        profile.setFullName(this.fullName);
        profile.setDepartment(this.department != null && !this.department.isBlank() ? this.department : "GEN");
        profile.setTitle(this.title);
        profile.setEmail(this.email);
        profile.setPhone(this.phone);
        profile.setBloodGroup(this.bloodGroup);
        profile.setDateOfBirth(this.dateOfBirth);
        profile.setIssueDate(this.issueDate);
        profile.setExpiryDate(this.expiryDate);
        profile.setTemplate(this.template);
        profile.setBarcodeType(this.barcodeType != null ? this.barcodeType : BarcodeType.CODE_128);
        return profile;
    }
}
