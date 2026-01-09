package dev.joshuaonyema.kaleo.application.service;

import dev.joshuaonyema.kaleo.domain.entity.Pass;
import dev.joshuaonyema.kaleo.domain.entity.QrCode;

import java.util.UUID;

public interface QrCodeService {

    QrCode generateQrCode(Pass pass);

    byte[] getQrCodeImageForUserAndPass(UUID passId);
}
