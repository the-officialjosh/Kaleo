package dev.joshuaonyema.kaleo.application.service;

import dev.joshuaonyema.kaleo.domain.entity.Pass;

import java.util.UUID;

public interface PassService {
    Pass purchasePass(UUID ticketTypeId);
}
