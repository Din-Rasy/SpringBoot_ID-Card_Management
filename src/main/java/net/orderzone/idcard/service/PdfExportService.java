package net.orderzone.idcard.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import net.orderzone.idcard.model.Profile;
import net.orderzone.idcard.model.Template;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class PdfExportService {

    private final PhotoStorageService photoStorageService;
    private final QrCodeService qrCodeService;
    private final BarcodeService barcodeService;

    public PdfExportService(PhotoStorageService photoStorageService,
                            QrCodeService qrCodeService,
                            BarcodeService barcodeService) {
        this.photoStorageService = photoStorageService;
        this.qrCodeService = qrCodeService;
        this.barcodeService = barcodeService;
    }

    public byte[] generatePdfCard(Profile profile, String baseUrl) {
        return generateBatchPdfCards(List.of(profile), baseUrl);
    }

    public byte[] generateBatchPdfCards(List<Profile> profiles, String baseUrl) {
        if (profiles == null || profiles.isEmpty()) {
            return new byte[0];
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            
            Document document = new Document(pdfDoc);
            document.setMargins(0, 0, 0, 0);

            for (int i = 0; i < profiles.size(); i++) {
                Profile profile = profiles.get(i);
                Template template = profile.getTemplate();
                if (template == null) {
                    template = Template.builder()
                            .organizationName("Organization")
                            .layout("VERTICAL")
                            .primaryColor("#1d4ed8")
                            .secondaryColor("#e0e7ff")
                            .textColor("#111827")
                            .tagline("ID Card")
                            .build();
                }

                boolean isVertical = "VERTICAL".equalsIgnoreCase(template.getLayout());
                PageSize pageSize = isVertical ? new PageSize(180, 280) : new PageSize(280, 180);
                pdfDoc.setDefaultPageSize(pageSize);

                if (i > 0) {
                    document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                }

                if (isVertical) {
                    renderVerticalCard(document, profile, template, baseUrl);
                } else {
                    renderHorizontalCard(document, profile, template, baseUrl);
                }
            }

            document.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating PDF cards", e);
        }
    }

    private void renderVerticalCard(Document document, Profile profile, Template template, String baseUrl) {
        DeviceRgb primary = parseHexColor(template.getPrimaryColor());
        DeviceRgb secondary = parseHexColor(template.getSecondaryColor());
        DeviceRgb textCol = parseHexColor(template.getTextColor());

        Table cardTable = new Table(UnitValue.createPercentArray(new float[]{100}))
                .useAllAvailableWidth()
                .setBackgroundColor(secondary)
                .setBorder(Border.NO_BORDER);

        String orgName = template.getOrganizationName() != null ? template.getOrganizationName().toUpperCase() : "";
        Cell headerCell = new Cell()
                .add(new Paragraph(orgName)
                        .setFontSize(10)
                        .setBold()
                        .setFontColor(new DeviceRgb(255, 255, 255))
                        .setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(primary)
                .setPadding(6)
                .setBorder(Border.NO_BORDER);
        cardTable.addCell(headerCell);

        if (template.getTagline() != null && !template.getTagline().isBlank()) {
            Cell taglineCell = new Cell()
                    .add(new Paragraph(template.getTagline())
                            .setFontSize(6)
                            .setFontColor(new DeviceRgb(255, 255, 255))
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBackgroundColor(primary)
                    .setPaddingTop(0)
                    .setPaddingBottom(3)
                    .setBorder(Border.NO_BORDER);
            cardTable.addCell(taglineCell);
        }

        Cell photoCell = new Cell()
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(6)
                .setBorder(Border.NO_BORDER);

        if (profile.hasPhoto()) {
            try {
                Path photoPath = photoStorageService.load(profile.getPhotoFileName());
                if (Files.exists(photoPath)) {
                    byte[] photoBytes = Files.readAllBytes(photoPath);
                    ImageData photoData = ImageDataFactory.create(photoBytes);
                    Image photoImg = new Image(photoData);
                    photoImg.setHeight(65);
                    photoImg.setWidth(55);
                    photoImg.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    photoCell.add(photoImg);
                } else {
                    addPhotoPlaceholder(photoCell);
                }
            } catch (Exception e) {
                addPhotoPlaceholder(photoCell);
            }
        } else {
            addPhotoPlaceholder(photoCell);
        }
        cardTable.addCell(photoCell);

        Cell infoCell = new Cell()
                .setPaddingLeft(10)
                .setPaddingRight(10)
                .setPaddingTop(2)
                .setPaddingBottom(2)
                .setBorder(Border.NO_BORDER);

        infoCell.add(new Paragraph(profile.getFullName())
                .setFontSize(10)
                .setBold()
                .setFontColor(textCol)
                .setTextAlignment(TextAlignment.CENTER));

        String titleDept = profile.getTitle();
        if (profile.getDepartment() != null && !profile.getDepartment().isBlank()) {
            titleDept += " - " + profile.getDepartment();
        }
        infoCell.add(new Paragraph(titleDept)
                .setFontSize(7)
                .setFontColor(textCol)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(4));

        Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                .useAllAvailableWidth()
                .setBorder(Border.NO_BORDER);

        addDetailRow(detailsTable, "Reg No:", profile.getRegistrationNumber(), textCol);
        addDetailRow(detailsTable, "Type:", profile.getType().toString(), textCol);
        if (profile.getEmail() != null && !profile.getEmail().isBlank()) {
            addDetailRow(detailsTable, "Email:", profile.getEmail(), textCol);
        }
        if (profile.getPhone() != null && !profile.getPhone().isBlank()) {
            addDetailRow(detailsTable, "Phone:", profile.getPhone(), textCol);
        }
        if (profile.getBloodGroup() != null && !profile.getBloodGroup().isBlank()) {
            addDetailRow(detailsTable, "Blood:", profile.getBloodGroup(), textCol);
        }
        if (profile.getIssueDate() != null) {
            addDetailRow(detailsTable, "Issued:", profile.getIssueDate().toString(), textCol);
        }
        if (profile.getExpiryDate() != null) {
            addDetailRow(detailsTable, "Expires:", profile.getExpiryDate().toString(), textCol);
        }

        infoCell.add(detailsTable);
        cardTable.addCell(infoCell);

        Cell footerCell = new Cell()
                .setPadding(6)
                .setBorder(Border.NO_BORDER);

        Table footerTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
                .useAllAvailableWidth()
                .setBorder(Border.NO_BORDER);

        Cell qrCell = new Cell().setBorder(Border.NO_BORDER);
        try {
            String verifyUrl = baseUrl + "/verify/" + profile.getUuid();
            byte[] qrBytes = qrCodeService.generateQrCodeImage(verifyUrl, 50, 50);
            ImageData qrData = ImageDataFactory.create(qrBytes);
            Image qrImg = new Image(qrData);
            qrImg.setHeight(40);
            qrImg.setWidth(40);
            qrCell.add(qrImg);
        } catch (Exception e) {
            // Skip
        }
        footerTable.addCell(qrCell);

        Cell barcodeCell = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
        try {
            byte[] barcodeBytes = barcodeService.generateBarcode(
                    profile.getRegistrationNumber(),
                    profile.getBarcodeType(),
                    100,
                    25
            );
            ImageData barcodeData = ImageDataFactory.create(barcodeBytes);
            Image barcodeImg = new Image(barcodeData);
            barcodeImg.setHeight(25);
            barcodeImg.setWidth(85);
            barcodeImg.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            barcodeCell.add(barcodeImg);

            barcodeCell.add(new Paragraph(profile.getRegistrationNumber())
                    .setFontSize(5)
                    .setFontColor(textCol)
                    .setTextAlignment(TextAlignment.RIGHT));
        } catch (Exception e) {
            // Skip
        }
        footerTable.addCell(barcodeCell);

        footerCell.add(footerTable);
        cardTable.addCell(footerCell);

        document.add(cardTable);
    }

    private void renderHorizontalCard(Document document, Profile profile, Template template, String baseUrl) {
        DeviceRgb primary = parseHexColor(template.getPrimaryColor());
        DeviceRgb secondary = parseHexColor(template.getSecondaryColor());
        DeviceRgb textCol = parseHexColor(template.getTextColor());

        Table cardTable = new Table(UnitValue.createPercentArray(new float[]{100}))
                .useAllAvailableWidth()
                .setBackgroundColor(secondary)
                .setBorder(Border.NO_BORDER);

        String orgName = template.getOrganizationName() != null ? template.getOrganizationName().toUpperCase() : "";
        String tagline = template.getTagline() != null ? template.getTagline() : "";
        Cell headerCell = new Cell()
                .add(new Paragraph(orgName + (tagline.isEmpty() ? "" : " - " + tagline))
                        .setFontSize(9)
                        .setBold()
                        .setFontColor(new DeviceRgb(255, 255, 255))
                        .setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(primary)
                .setPadding(4)
                .setBorder(Border.NO_BORDER);
        cardTable.addCell(headerCell);

        Cell bodyCell = new Cell().setBorder(Border.NO_BORDER).setPadding(6);
        Table bodyTable = new Table(UnitValue.createPercentArray(new float[]{25, 50, 25}))
                .useAllAvailableWidth()
                .setBorder(Border.NO_BORDER);

        Cell photoCol = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
        if (profile.hasPhoto()) {
            try {
                Path photoPath = photoStorageService.load(profile.getPhotoFileName());
                if (Files.exists(photoPath)) {
                    byte[] photoBytes = Files.readAllBytes(photoPath);
                    ImageData photoData = ImageDataFactory.create(photoBytes);
                    Image photoImg = new Image(photoData);
                    photoImg.setHeight(60);
                    photoImg.setWidth(50);
                    photoCol.add(photoImg);
                } else {
                    addPhotoPlaceholder(photoCol);
                }
            } catch (Exception e) {
                addPhotoPlaceholder(photoCol);
            }
        } else {
            addPhotoPlaceholder(photoCol);
        }
        bodyTable.addCell(photoCol);

        Cell infoCol = new Cell().setBorder(Border.NO_BORDER).setPaddingLeft(6).setPaddingRight(6);
        infoCol.add(new Paragraph(profile.getFullName())
                .setFontSize(9)
                .setBold()
                .setFontColor(textCol));

        String titleDept = profile.getTitle();
        if (profile.getDepartment() != null && !profile.getDepartment().isBlank()) {
            titleDept += " - " + profile.getDepartment();
        }
        infoCol.add(new Paragraph(titleDept)
                .setFontSize(6)
                .setFontColor(textCol)
                .setMarginBottom(3));

        Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                .useAllAvailableWidth()
                .setBorder(Border.NO_BORDER);

        addDetailRow(detailsTable, "Reg No:", profile.getRegistrationNumber(), textCol);
        addDetailRow(detailsTable, "Type:", profile.getType().toString(), textCol);
        if (profile.getEmail() != null && !profile.getEmail().isBlank()) {
            addDetailRow(detailsTable, "Email:", profile.getEmail(), textCol);
        }
        if (profile.getPhone() != null && !profile.getPhone().isBlank()) {
            addDetailRow(detailsTable, "Phone:", profile.getPhone(), textCol);
        }
        if (profile.getIssueDate() != null) {
            addDetailRow(detailsTable, "Issued:", profile.getIssueDate().toString(), textCol);
        }
        if (profile.getExpiryDate() != null) {
            addDetailRow(detailsTable, "Expires:", profile.getExpiryDate().toString(), textCol);
        }
        infoCol.add(detailsTable);
        bodyTable.addCell(infoCol);

        Cell codeCol = new Cell().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER);
        
        try {
            String verifyUrl = baseUrl + "/verify/" + profile.getUuid();
            byte[] qrBytes = qrCodeService.generateQrCodeImage(verifyUrl, 40, 40);
            ImageData qrData = ImageDataFactory.create(qrBytes);
            Image qrImg = new Image(qrData);
            qrImg.setHeight(36);
            qrImg.setWidth(36);
            qrImg.setHorizontalAlignment(HorizontalAlignment.CENTER);
            codeCol.add(qrImg);
        } catch (Exception e) {
            // Skip
        }

        codeCol.add(new Paragraph("").setFontSize(4));

        try {
            byte[] barcodeBytes = barcodeService.generateBarcode(
                    profile.getRegistrationNumber(),
                    profile.getBarcodeType(),
                    80,
                    20
            );
            ImageData barcodeData = ImageDataFactory.create(barcodeBytes);
            Image barcodeImg = new Image(barcodeData);
            barcodeImg.setHeight(16);
            barcodeImg.setWidth(55);
            barcodeImg.setHorizontalAlignment(HorizontalAlignment.CENTER);
            codeCol.add(barcodeImg);

            codeCol.add(new Paragraph(profile.getRegistrationNumber())
                    .setFontSize(4)
                    .setFontColor(textCol)
                    .setTextAlignment(TextAlignment.CENTER));
        } catch (Exception e) {
            // Skip
        }
        bodyTable.addCell(codeCol);

        bodyCell.add(bodyTable);
        cardTable.addCell(bodyCell);

        document.add(cardTable);
    }

    private void addPhotoPlaceholder(Cell cell) {
        Table placeholder = new Table(UnitValue.createPercentArray(new float[]{100}))
                .setHeight(60)
                .setWidth(50)
                .setBorder(Border.NO_BORDER)
                .setBackgroundColor(new DeviceRgb(200, 200, 200));
        cell.add(placeholder);
    }

    private void addDetailRow(Table table, String label, String value, DeviceRgb color) {
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setPadding(0)
                .add(new Paragraph(label).setFontSize(5).setBold().setFontColor(color)));
        table.addCell(new Cell().setBorder(Border.NO_BORDER).setPadding(0)
                .add(new Paragraph(value).setFontSize(5).setFontColor(color)));
    }

    private DeviceRgb parseHexColor(String hex) {
        try {
            if (hex.startsWith("#")) {
                hex = hex.substring(1);
            }
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return new DeviceRgb(r, g, b);
        } catch (Exception e) {
            return new DeviceRgb(29, 78, 216);
        }
    }
}
