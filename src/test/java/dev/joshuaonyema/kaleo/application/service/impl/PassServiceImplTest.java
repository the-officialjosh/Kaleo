package dev.joshuaonyema.kaleo.application.service.impl;

import dev.joshuaonyema.kaleo.application.security.CurrentUserService;
import dev.joshuaonyema.kaleo.application.service.QrCodeService;
import dev.joshuaonyema.kaleo.domain.entity.*;
import dev.joshuaonyema.kaleo.exception.PassSoldOutException;
import dev.joshuaonyema.kaleo.exception.PassTypeNotFoundException;
import dev.joshuaonyema.kaleo.repository.PassRepository;
import dev.joshuaonyema.kaleo.repository.PassTypeRepository;
import dev.joshuaonyema.kaleo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PassRepository passRepository;

    @Mock
    private PassTypeRepository passTypeRepository;

    @Mock
    private QrCodeService qrCodeService;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private PassServiceImpl passService;

    private User user;
    private PassType passType;
    private UUID passTypeId;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Test User");
        user.setEmail("user@test.com");

        passTypeId = UUID.randomUUID();
        passType = new PassType();
        passType.setId(passTypeId);
        passType.setName("General Admission");
        passType.setTotalAvailable(100);
    }

    // ==================== purchasePass Success Tests ====================

    @Test
    void purchasePass_whenValidRequest_thenCreatesPass() {
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(passTypeRepository.findByIdWithLock(passTypeId)).thenReturn(Optional.of(passType));
        when(passRepository.countByPassTypeId(passTypeId)).thenReturn(50);
        when(passRepository.save(any(Pass.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(qrCodeService.generateQrCode(any(Pass.class))).thenReturn(new QrCode());

        passService.purchasePass(passTypeId);

        verify(passRepository, times(2)).save(any(Pass.class));
    }

    @Test
    void purchasePass_whenCalled_thenSetsCorrectPassStatus() {
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(passTypeRepository.findByIdWithLock(passTypeId)).thenReturn(Optional.of(passType));
        when(passRepository.countByPassTypeId(passTypeId)).thenReturn(0);
        when(passRepository.save(any(Pass.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(qrCodeService.generateQrCode(any(Pass.class))).thenReturn(new QrCode());

        passService.purchasePass(passTypeId);

        ArgumentCaptor<Pass> passCaptor = ArgumentCaptor.forClass(Pass.class);
        verify(passRepository, atLeastOnce()).save(passCaptor.capture());
        
        Pass savedPass = passCaptor.getValue();
        assertEquals(PassStatus.PURCHASED, savedPass.getStatus());
    }

    @Test
    void purchasePass_whenCalled_thenLinksPassToPassType() {
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(passTypeRepository.findByIdWithLock(passTypeId)).thenReturn(Optional.of(passType));
        when(passRepository.countByPassTypeId(passTypeId)).thenReturn(0);
        when(passRepository.save(any(Pass.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(qrCodeService.generateQrCode(any(Pass.class))).thenReturn(new QrCode());

        passService.purchasePass(passTypeId);

        ArgumentCaptor<Pass> passCaptor = ArgumentCaptor.forClass(Pass.class);
        verify(passRepository, atLeastOnce()).save(passCaptor.capture());
        
        Pass savedPass = passCaptor.getValue();
        assertEquals(passType, savedPass.getPassType());
    }

    @Test
    void purchasePass_whenCalled_thenLinksPassToCurrentUser() {
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(passTypeRepository.findByIdWithLock(passTypeId)).thenReturn(Optional.of(passType));
        when(passRepository.countByPassTypeId(passTypeId)).thenReturn(0);
        when(passRepository.save(any(Pass.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(qrCodeService.generateQrCode(any(Pass.class))).thenReturn(new QrCode());

        passService.purchasePass(passTypeId);

        ArgumentCaptor<Pass> passCaptor = ArgumentCaptor.forClass(Pass.class);
        verify(passRepository, atLeastOnce()).save(passCaptor.capture());
        
        Pass savedPass = passCaptor.getValue();
        assertEquals(user, savedPass.getRegistrant());
    }

    @Test
    void purchasePass_whenCalled_thenGeneratesQrCode() {
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(passTypeRepository.findByIdWithLock(passTypeId)).thenReturn(Optional.of(passType));
        when(passRepository.countByPassTypeId(passTypeId)).thenReturn(0);
        when(passRepository.save(any(Pass.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(qrCodeService.generateQrCode(any(Pass.class))).thenReturn(new QrCode());

        passService.purchasePass(passTypeId);

        verify(qrCodeService, times(1)).generateQrCode(any(Pass.class));
    }

    // ==================== purchasePass Edge Cases ====================

    @Test
    void purchasePass_whenAtCapacityMinusOne_thenSucceeds() {
        passType.setTotalAvailable(100);
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(passTypeRepository.findByIdWithLock(passTypeId)).thenReturn(Optional.of(passType));
        when(passRepository.countByPassTypeId(passTypeId)).thenReturn(99); // At 99, can still buy 1 more
        when(passRepository.save(any(Pass.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(qrCodeService.generateQrCode(any(Pass.class))).thenReturn(new QrCode());

        assertDoesNotThrow(() -> passService.purchasePass(passTypeId));
        verify(passRepository, times(2)).save(any(Pass.class));
    }

    // ==================== purchasePass Exception Tests ====================

    @Test
    void purchasePass_whenPassTypeNotFound_thenThrowsException() {
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(passTypeRepository.findByIdWithLock(passTypeId)).thenReturn(Optional.empty());

        PassTypeNotFoundException exception = assertThrows(
            PassTypeNotFoundException.class,
            () -> passService.purchasePass(passTypeId)
        );

        assertTrue(exception.getMessage().contains(passTypeId.toString()));
    }

    @Test
    void purchasePass_whenAtCapacity_thenThrowsSoldOutException() {
        passType.setTotalAvailable(100);
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(passTypeRepository.findByIdWithLock(passTypeId)).thenReturn(Optional.of(passType));
        when(passRepository.countByPassTypeId(passTypeId)).thenReturn(100); // Already at capacity

        assertThrows(PassSoldOutException.class, () -> passService.purchasePass(passTypeId));
    }

    @Test
    void purchasePass_whenOverCapacity_thenThrowsSoldOutException() {
        passType.setTotalAvailable(50);
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(passTypeRepository.findByIdWithLock(passTypeId)).thenReturn(Optional.of(passType));
        when(passRepository.countByPassTypeId(passTypeId)).thenReturn(50);

        assertThrows(PassSoldOutException.class, () -> passService.purchasePass(passTypeId));
        verify(passRepository, never()).save(any(Pass.class));
    }

    // ==================== purchasePass Verification Tests ====================

    @Test
    void purchasePass_whenCalled_thenUsesLockingQuery() {
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(passTypeRepository.findByIdWithLock(passTypeId)).thenReturn(Optional.of(passType));
        when(passRepository.countByPassTypeId(passTypeId)).thenReturn(0);
        when(passRepository.save(any(Pass.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(qrCodeService.generateQrCode(any(Pass.class))).thenReturn(new QrCode());

        passService.purchasePass(passTypeId);

        verify(passTypeRepository).findByIdWithLock(passTypeId);
        verify(passTypeRepository, never()).findById(any());
    }

    @Test
    void purchasePass_whenCalled_thenCountsExistingPasses() {
        when(currentUserService.getCurrentUser()).thenReturn(user);
        when(passTypeRepository.findByIdWithLock(passTypeId)).thenReturn(Optional.of(passType));
        when(passRepository.countByPassTypeId(passTypeId)).thenReturn(0);
        when(passRepository.save(any(Pass.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(qrCodeService.generateQrCode(any(Pass.class))).thenReturn(new QrCode());

        passService.purchasePass(passTypeId);

        verify(passRepository).countByPassTypeId(passTypeId);
    }
}
