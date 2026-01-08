package dev.joshuaonyema.kaleo.application.service;

import dev.joshuaonyema.kaleo.domain.entity.Pass;
import dev.joshuaonyema.kaleo.domain.entity.QrCode;

public interface QrCodeService {

    QrCode generateQrCode(Pass pass);
}
