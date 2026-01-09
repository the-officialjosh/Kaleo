package dev.joshuaonyema.kaleo.api.controller;

import dev.joshuaonyema.kaleo.application.service.PassService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassTypeControllerTest {

    @Mock
    private PassService passService;

    @InjectMocks
    private PassTypeController passTypeController;

    // ==================== purchasePass Tests ====================

    @Test
    void purchasePass_whenValidRequest_thenReturnsNoContent() {
        UUID programId = UUID.randomUUID();
        UUID passTypeId = UUID.randomUUID();
        doNothing().when(passService).purchasePass(passTypeId);

        ResponseEntity<Void> response = passTypeController.purchasePass(programId, passTypeId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void purchasePass_whenCalled_thenServiceIsInvoked() {
        UUID programId = UUID.randomUUID();
        UUID passTypeId = UUID.randomUUID();
        doNothing().when(passService).purchasePass(passTypeId);

        passTypeController.purchasePass(programId, passTypeId);

        verify(passService, times(1)).purchasePass(passTypeId);
    }

    @Test
    void purchasePass_whenCalledWithDifferentPassTypes_thenPassesCorrectId() {
        UUID programId = UUID.randomUUID();
        UUID passTypeId1 = UUID.randomUUID();
        UUID passTypeId2 = UUID.randomUUID();
        doNothing().when(passService).purchasePass(any(UUID.class));

        passTypeController.purchasePass(programId, passTypeId1);
        passTypeController.purchasePass(programId, passTypeId2);

        verify(passService).purchasePass(passTypeId1);
        verify(passService).purchasePass(passTypeId2);
    }

    @Test
    void purchasePass_whenServiceThrowsException_thenExceptionPropagates() {
        UUID programId = UUID.randomUUID();
        UUID passTypeId = UUID.randomUUID();
        doThrow(new RuntimeException("Service error")).when(passService).purchasePass(passTypeId);

        assertThrows(RuntimeException.class, () -> 
            passTypeController.purchasePass(programId, passTypeId)
        );
    }

    @Test
    void purchasePass_whenCalled_thenReturnsCorrectStatusCode() {
        UUID programId = UUID.randomUUID();
        UUID passTypeId = UUID.randomUUID();
        doNothing().when(passService).purchasePass(passTypeId);

        ResponseEntity<Void> response = passTypeController.purchasePass(programId, passTypeId);

        assertEquals(204, response.getStatusCode().value());
    }
}
