package dev.joshuaonyema.kaleo.application.service.impl;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import dev.joshuaonyema.kaleo.domain.entity.Pass;
import dev.joshuaonyema.kaleo.domain.entity.QrCode;
import dev.joshuaonyema.kaleo.domain.entity.QrCodeStatus;
import dev.joshuaonyema.kaleo.exception.QrCodeGenerationException;
import dev.joshuaonyema.kaleo.repository.QrCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QrCodeServiceImplTest {

    @Mock
    private QRCodeWriter qrCodeWriter;

    @Mock
    private QrCodeRepository qrCodeRepository;

    @InjectMocks
    private QrCodeServiceImpl qrCodeService;

    private Pass pass;

    @BeforeEach
    void setUp() {
        pass = new Pass();
        pass.setId(UUID.randomUUID());
    }

    // ==================== generateQrCode Success Tests ====================

    @Test
    void generateQrCode_whenValidPass_thenReturnsQrCode() throws Exception {
        when(qrCodeWriter.encode(anyString(), any(), anyInt(), anyInt()))
            .thenReturn(new com.google.zxing.common.BitMatrix(300, 300));
        when(qrCodeRepository.saveAndFlush(any(QrCode.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        QrCode result = qrCodeService.generateQrCode(pass);

        assertNotNull(result);
    }

    @Test
    void generateQrCode_whenCalled_thenSetsActiveStatus() throws Exception {
        when(qrCodeWriter.encode(anyString(), any(), anyInt(), anyInt()))
            .thenReturn(new com.google.zxing.common.BitMatrix(300, 300));
        when(qrCodeRepository.saveAndFlush(any(QrCode.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        QrCode result = qrCodeService.generateQrCode(pass);

        assertEquals(QrCodeStatus.ACTIVE, result.getStatus());
    }

    @Test
    void generateQrCode_whenCalled_thenSetsUniqueId() throws Exception {
        when(qrCodeWriter.encode(anyString(), any(), anyInt(), anyInt()))
            .thenReturn(new com.google.zxing.common.BitMatrix(300, 300));
        when(qrCodeRepository.saveAndFlush(any(QrCode.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        QrCode result = qrCodeService.generateQrCode(pass);

        assertNotNull(result.getId());
    }

    @Test
    void generateQrCode_whenCalled_thenLinksToPass() throws Exception {
        when(qrCodeWriter.encode(anyString(), any(), anyInt(), anyInt()))
            .thenReturn(new com.google.zxing.common.BitMatrix(300, 300));
        when(qrCodeRepository.saveAndFlush(any(QrCode.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        QrCode result = qrCodeService.generateQrCode(pass);

        assertEquals(pass, result.getPass());
    }

    @Test
    void generateQrCode_whenCalled_thenGeneratesBase64Value() throws Exception {
        when(qrCodeWriter.encode(anyString(), any(), anyInt(), anyInt()))
            .thenReturn(new com.google.zxing.common.BitMatrix(300, 300));
        when(qrCodeRepository.saveAndFlush(any(QrCode.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        QrCode result = qrCodeService.generateQrCode(pass);

        assertNotNull(result.getValue());
        assertFalse(result.getValue().isEmpty());
    }

    @Test
    void generateQrCode_whenCalled_thenSavesToRepository() throws Exception {
        when(qrCodeWriter.encode(anyString(), any(), anyInt(), anyInt()))
            .thenReturn(new com.google.zxing.common.BitMatrix(300, 300));
        when(qrCodeRepository.saveAndFlush(any(QrCode.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        qrCodeService.generateQrCode(pass);

        verify(qrCodeRepository, times(1)).saveAndFlush(any(QrCode.class));
    }

    // ==================== generateQrCode Exception Tests ====================

    @Test
    void generateQrCode_whenWriterFails_thenThrowsQrCodeGenerationException() throws Exception {
        when(qrCodeWriter.encode(anyString(), any(), anyInt(), anyInt()))
            .thenThrow(new WriterException("Encoding failed"));

        QrCodeGenerationException exception = assertThrows(
            QrCodeGenerationException.class,
            () -> qrCodeService.generateQrCode(pass)
        );

        assertEquals("Failed to generate QR Code", exception.getMessage());
    }

    @Test
    void generateQrCode_whenWriterFails_thenDoesNotSaveToRepository() throws Exception {
        when(qrCodeWriter.encode(anyString(), any(), anyInt(), anyInt()))
            .thenThrow(new WriterException("Encoding failed"));

        assertThrows(QrCodeGenerationException.class, () -> qrCodeService.generateQrCode(pass));

        verify(qrCodeRepository, never()).saveAndFlush(any());
    }

    // ==================== generateQrCode Verification Tests ====================

    @Test
    void generateQrCode_whenCalled_thenUsesCorrectDimensions() throws Exception {
        when(qrCodeWriter.encode(anyString(), any(), eq(300), eq(300)))
            .thenReturn(new com.google.zxing.common.BitMatrix(300, 300));
        when(qrCodeRepository.saveAndFlush(any(QrCode.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        qrCodeService.generateQrCode(pass);

        verify(qrCodeWriter).encode(anyString(), any(), eq(300), eq(300));
    }

    @Test
    void generateQrCode_whenCalledMultipleTimes_thenGeneratesUniqueIds() throws Exception {
        when(qrCodeWriter.encode(anyString(), any(), anyInt(), anyInt()))
            .thenReturn(new com.google.zxing.common.BitMatrix(300, 300));
        when(qrCodeRepository.saveAndFlush(any(QrCode.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        QrCode result1 = qrCodeService.generateQrCode(pass);
        QrCode result2 = qrCodeService.generateQrCode(pass);

        assertNotEquals(result1.getId(), result2.getId());
    }
}
