package net.orderzone.idcard.service;

import net.orderzone.idcard.model.Profile;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class IdCardRenderService {

    private final QrCodeService qrCodeService;
    private final BarcodeService barcodeService;

    public IdCardRenderService(QrCodeService qrCodeService, BarcodeService barcodeService) {
        this.qrCodeService = qrCodeService;
        this.barcodeService = barcodeService;
    }

    public Map<String, Object> prepareCardData(Profile profile, String baseUrl) {
        Map<String, Object> data = new HashMap<>();
        data.put("profile", profile);

        String verifyUrl = baseUrl + "/verify/" + profile.getUuid();
        byte[] qrBytes = qrCodeService.generateQrCodeImage(verifyUrl, 150, 150);
        String qrBase64 = Base64.getEncoder().encodeToString(qrBytes);
        data.put("qrCodeBase64", qrBase64);

        byte[] barcodeBytes = barcodeService.generateBarcode(
                profile.getRegistrationNumber(),
                profile.getBarcodeType(),
                250,
                50
        );
        String barcodeBase64 = Base64.getEncoder().encodeToString(barcodeBytes);
        data.put("barcodeBase64", barcodeBase64);

        return data;
    }
}
