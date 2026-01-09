package dev.joshuaonyema.kaleo.application.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import dev.joshuaonyema.kaleo.application.security.CurrentUserService;
import dev.joshuaonyema.kaleo.application.service.QrCodeService;
import dev.joshuaonyema.kaleo.domain.entity.Pass;
import dev.joshuaonyema.kaleo.domain.entity.QrCode;
import dev.joshuaonyema.kaleo.domain.entity.QrCodeStatus;
import dev.joshuaonyema.kaleo.exception.QrCodeGenerationException;
import dev.joshuaonyema.kaleo.exception.QrCodeNotFoundException;
import dev.joshuaonyema.kaleo.repository.QrCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    private static final int QR_HEIGHT = 300;
    private static final int QR_WIDTH = 300;

    private final QRCodeWriter qrCodeWriter;
    private final QrCodeRepository qrCodeRepository;
    private final CurrentUserService currentUserService;

    @Override
    public QrCode generateQrCode(Pass pass) {
        try {
            UUID uniqueId = UUID.randomUUID();
            String qrCodeImage = generateQrCodeImage(uniqueId);

            QrCode qrCode = new QrCode();
            qrCode.setId(uniqueId);
            qrCode.setStatus(QrCodeStatus.ACTIVE);
            qrCode.setValue(qrCodeImage);
            qrCode.setPass(pass);

            return qrCodeRepository.saveAndFlush(qrCode);
        }catch (WriterException | IOException exception){
            throw new QrCodeGenerationException("Failed to generate QR Code", exception);
        }
    }

    @Override
    public byte[] getQrCodeImageForUserAndPass(UUID passId) {
        UUID currentUserId = currentUserService.getCurrentUserId();
        QrCode qrCode = qrCodeRepository.findByPassIdAndPassRegistrantId(passId, currentUserId)
                .orElseThrow(QrCodeNotFoundException::new);

        try {
            return Base64.getDecoder().decode(qrCode.getValue());
        }catch (IllegalArgumentException exception){
            log.error("Invalid base64 QR Code for ticket ID: {}", passId, exception);
            throw  new QrCodeNotFoundException();
        }
    }

    private String generateQrCodeImage(UUID uniqueId) throws WriterException, IOException {
        BitMatrix bitMatrix = qrCodeWriter.encode(
                uniqueId.toString(),
                BarcodeFormat.QR_CODE,
                QR_WIDTH,
                QR_HEIGHT
        );

        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(qrCodeImage, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();

            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }
}
