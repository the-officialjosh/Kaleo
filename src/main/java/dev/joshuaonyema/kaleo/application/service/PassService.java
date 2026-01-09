package dev.joshuaonyema.kaleo.application.service;

import dev.joshuaonyema.kaleo.domain.entity.Pass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PassService {
    void purchasePass(UUID passTypeId);
    Page<Pass> listPassesForUser(Pageable pageable);
}
