# Spring Boot ID Card Management System Submission

## 1. GitHub URL

https://github.com/Din-Rasy/SpringBoot_ID-Card_Management.git

## 2. Branch Name

main

## 3. Screenshot of File Structure

I attached the screenshot of my project file structure in the Files section.

## Project Summary

This project is a Spring Boot ID Card Management System for managing ID cards for students, employees, and users. The project uses Maven, Spring Boot, Spring Data JPA, MySQL, Thymeleaf, iText, ZXing, and tests.

## Implemented Features

- CRUD for User, Student, and Employee profiles
- MySQL database integration with Spring Data JPA
- Photo upload handling for JPEG/PNG images
- File size and file type validation
- ID card template layout using Thymeleaf, HTML, and CSS
- Live ID card preview
- UUID and registration number generation
- PDF export for ID cards
- Batch ID card generation
- QR code integration
- Barcode support for CODE_128 and EAN_13
- ProfileRepository and TemplateRepository
- Service layer, controller layer, model layer, and test files

## Main File Structure

```text
FINAL_EXAM
├── .github
├── .vscode
├── src
│   ├── images
│   ├── main
│   │   ├── java/net/orderzone/idcard
│   │   │   ├── config
│   │   │   ├── controller
│   │   │   ├── dto
│   │   │   ├── exception
│   │   │   ├── model
│   │   │   ├── repository
│   │   │   ├── service
│   │   │   └── IdCardApplication.java
│   │   └── resources
│   │       ├── static
│   │       ├── templates
│   │       ├── application-local.properties
│   │       └── application.properties
│   └── test
├── target
├── uploads
├── .gitignore
├── pom.xml
└── README.md
```

## Required Models and Repository Classes

The project includes the required models and repositories:

- Profile
- ProfileBuilder
- ProfileType
- Template
- BarcodeType
- ProfileRepository
- TemplateRepository

## Technologies Used

- Java 17
- Maven
- Spring Boot
- Spring Web
- Spring Data JPA
- Thymeleaf
- MySQL
- iText
- ZXing
- JUnit / Spring Boot Test

## Run Command

```bash
mvn spring-boot:run
```

## Test Command

```bash
mvn test
```

## Final Note

The project has been uploaded to a public GitHub repository and uses the `main` branch for submission.
