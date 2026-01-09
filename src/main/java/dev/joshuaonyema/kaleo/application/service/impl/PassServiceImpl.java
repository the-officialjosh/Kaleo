package dev.joshuaonyema.kaleo.application.service.impl;

import dev.joshuaonyema.kaleo.application.security.CurrentUserService;
import dev.joshuaonyema.kaleo.application.service.PassService;
import dev.joshuaonyema.kaleo.application.service.QrCodeService;
import dev.joshuaonyema.kaleo.domain.entity.Pass;
import dev.joshuaonyema.kaleo.domain.entity.PassType;
import dev.joshuaonyema.kaleo.domain.entity.User;
import dev.joshuaonyema.kaleo.exception.PassTypeNotFoundException;
import dev.joshuaonyema.kaleo.repository.PassRepository;
import dev.joshuaonyema.kaleo.repository.PassTypeRepository;
import dev.joshuaonyema.kaleo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PassServiceImpl implements PassService {

    private final UserRepository userRepository;
    private final PassRepository passRepository;
    private final PassTypeRepository passTypeRepository;
    private final QrCodeService qrCodeService;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public Pass purchasePass(UUID passTypeId) {
        User user = currentUserService.getCurrentUser();

        PassType passType = passTypeRepository.findById(passTypeId).orElseThrow(() -> new PassTypeNotFoundException(
                String.format("Pass type with ID '%s' not found", passTypeId)
        ));

        int purchasedPasses = passRepository.countByPassTypeId(passTypeId);

        return null;
    }
}
