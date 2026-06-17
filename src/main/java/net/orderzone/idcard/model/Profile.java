package net.orderzone.idcard.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "profiles",
        uniqueConstraints = @UniqueConstraint(name = "uk_profile_reg_number", columnNames = "registration_number"))
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Public, stable identifier used in QR codes / verification URLs. */
    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String uuid;

    /** Human-friendly registration number, e.g. 2026-ENG-014. */
    @Column(name = "registration_number", nullable = false, unique = true, length = 64)
    private String registrationNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private ProfileType type;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(length = 80)
    private String department;

    /** Job title (employee) or program / class (student). */
    @Column(length = 120)
    private String title;

    @Column(length = 120)
    private String email;

    @Column(length = 40)
    private String phone;

    @Column(length = 60)
    private String bloodGroup;

    private LocalDate dateOfBirth;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    /** Relative file name of the stored photo (under the configured photo dir). */
    @Column(length = 255)
    private String photoFileName;

    @Column(length = 60)
    private String photoContentType;

    /** Selected card template / theme. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "template_id")
    private Template template;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private BarcodeType barcodeType = BarcodeType.CODE_128;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.issueDate == null) {
            this.issueDate = LocalDate.now();
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Transient
    public boolean hasPhoto() {
        return photoFileName != null && !photoFileName.isBlank();
    }

    // Constructors
    public Profile() {
    }

    public Profile(Long id, String uuid, String registrationNumber, ProfileType type, String fullName, String department, String title, String email, String phone, String bloodGroup, LocalDate dateOfBirth, LocalDate issueDate, LocalDate expiryDate, String photoFileName, String photoContentType, Template template, BarcodeType barcodeType, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.uuid = uuid;
        this.registrationNumber = registrationNumber;
        this.type = type;
        this.fullName = fullName;
        this.department = department;
        this.title = title;
        this.email = email;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
        this.dateOfBirth = dateOfBirth;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.photoFileName = photoFileName;
        this.photoContentType = photoContentType;
        this.template = template;
        this.barcodeType = barcodeType != null ? barcodeType : BarcodeType.CODE_128;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public ProfileType getType() {
        return type;
    }

    public void setType(ProfileType type) {
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public BarcodeType getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(BarcodeType barcodeType) {
        this.barcodeType = barcodeType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
