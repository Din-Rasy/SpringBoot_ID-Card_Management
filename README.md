# Spring Boot ID Card Management System

A complete Spring Boot web application to manage and generate professional ID cards for students, employees, and general users. The system supports profile registration, custom template layout styling, live preview, photo uploads, dynamic QR and Barcode rendering, single PDF exports, and batch PDF exports.

## Features

* **Profile CRUD:** Register, view, edit, and delete user profiles.
* **Live Card Preview:** Real-time ID card layout updates in the browser using JavaScript as you fill in form details (name, department, role, upload a photo, or change templates).
* **Custom Templates:** Manage card themes, customize primary/secondary colors (via native color pickers), change layouts (vertical vs horizontal), and set organizational taglines.
* **Photo Storage:** Secure JPEG/PNG photo upload verification, validation (<2MB size), and unique file hashing.
* **QR Codes & Barcodes:** Automatic QR code generation (embedding verification URLs `/verify/{uuid}`) and Barcode generation (supporting `CODE_128` and `EAN_13` with check digit resolution and automated fallback).
* **Single & Batch PDF Exports:** Generate single high-fidelity card PDFs (CR80 proportions) or merge multiple cards into printable PDF sheets (filtered by selection, type, or department) using iText 7.
* **Scan Verification:** Dedicated public URL verification page `/verify/{uuid}` to check card validity.

## Technologies Used

* **Language:** Java 17+ (Fully tested on JDK 25)
* **Framework:** Spring Boot 3.3.0
* **Data Layer:** Spring Data JPA + MySQL Driver
* **Template Engine:** Thymeleaf + HTML5 + CSS3 (Premium Glassmorphism styling)
* **Testing:** JUnit 5 + Spring Boot Test + H2 Database (for isolated test execution)
* **Libraries:**
  * **iText 7** (for PDF drawing and scaling)
  * **ZXing** (for QR code and Barcode rendering)

## Database Setup

1. **MySQL Database:** Ensure MySQL Server is running locally.
2. **Create Database:** Create a database named `idcard_db`.
   ```sql
   CREATE DATABASE idcard_db;
   ```
3. **Database Configuration:** Update credentials in `src/main/resources/application.properties` if they differ:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/idcard_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```

## How to Run

Compile and launch the Spring Boot application using Maven:
```bash
mvn spring-boot:run
```
Once started, the application is accessible at: [http://localhost:8080](http://localhost:8080)

## How to Test

Execute the JUnit test suite (runs against a local H2 in-memory profile):
```bash
mvn test
```

## API & Route Reference

### Profiles
* `GET /` - Dashboard/home view.
* `GET /profiles` - List registered profiles with search parameters.
* `GET /profiles/new` - Display registration form.
* `POST /profiles` - Register a new profile (including photo file upload).
* `GET /profiles/{id}` - View specific profile details and rendered card.
* `GET /profiles/{id}/edit` - Display profile editing form.
* `POST /profiles/{id}` - Save profile updates.
* `POST /profiles/{id}/delete` - Delete profile.
* `GET /profiles/{id}/card` - Standalone visual card preview.
* `GET /profiles/{id}/pdf` - Download single PDF card.
* `GET /verify/{uuid}` - Public card verification portal.

### Templates
* `GET /templates` - List custom card themes.
* `GET /templates/new` - Create a custom theme.
* `POST /templates` - Save new theme.
* `GET /templates/{id}/edit` - Edit theme colors/layout.
* `POST /templates/{id}` - Save theme updates.
* `POST /templates/{id}/delete` - Delete theme.

### Batch Operations
* `GET /batch` - Display batch printing options.
* `POST /batch/pdf` - Merge and download print-ready PDFs of matching card selections.

## GitHub Push Instructions

Initialize and upload this project to your GitHub repository using manual Git commands:

1. **Create Repository:** Create a new public repository on GitHub named `springboot-id-card-management`.
2. **Set Origin Remote:** Link your local repository to GitHub:
   ```bash
   git remote add origin https://github.com/YOUR_USERNAME/springboot-id-card-management.git
   ```
3. **Rename Default Branch:** Ensure the default branch is named `main`:
   ```bash
   git branch -M main
   ```
4. **Push to Remote:** Push your changes to GitHub:
   ```bash
   git push -u origin main
   ```
