package dev.joshuaonyema.kaleo.config.qrcode;

import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

import static org.junit.jupiter.api.Assertions.*;

class QrCodeConfigTest {

    private final QrCodeConfig qrCodeConfig = new QrCodeConfig();

    @Test
    void qrCodeWriter_whenCalled_thenReturnsNonNullQRCodeWriter() {
        QRCodeWriter result = qrCodeConfig.qrCodeWriter();

        assertNotNull(result);
    }

    @Test
    void qrCodeWriter_whenCalled_thenReturnsQRCodeWriterInstance() {
        QRCodeWriter result = qrCodeConfig.qrCodeWriter();

        assertInstanceOf(QRCodeWriter.class, result);
    }

    @Test
    void qrCodeWriter_whenCalledMultipleTimes_thenReturnsNewInstances() {
        QRCodeWriter result1 = qrCodeConfig.qrCodeWriter();
        QRCodeWriter result2 = qrCodeConfig.qrCodeWriter();

        // Each call creates a new instance (not a singleton by default)
        assertNotSame(result1, result2);
    }

    @Test
    void qrCodeConfig_hasConfigurationAnnotation() {
        Configuration annotation = AnnotationUtils.findAnnotation(
            QrCodeConfig.class, 
            Configuration.class
        );

        assertNotNull(annotation, "@Configuration annotation should be present");
    }
}
