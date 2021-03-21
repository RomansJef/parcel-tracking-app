package com.parcels.tracking.app;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ParcelsTrackingAppRepository interface.
 */
@Service
public class ParcelsTrackingAppRepository {

    /**
     * Parcels Tracking App Repository.
     */
    @Autowired
    private final IParcelsTrackingAppRepository iParcelsTrackingAppRepository;

    public ParcelsTrackingAppRepository(IParcelsTrackingAppRepository parcelsRepository) {
        this.iParcelsTrackingAppRepository = parcelsRepository;
    }

    public Optional<List<ParcelEntity>> getAllParcels() {
        return iParcelsTrackingAppRepository.findAllByOrderByDeliveredAsc();
    }

    public Optional<List<ParcelEntity>> getParcelsByClientId(@NonNull String clientId) {
        return iParcelsTrackingAppRepository.findAllByClientIdOrderByRegistrationDate(clientId);
    }

    public Optional<ParcelEntity> getByParcelId(@NonNull String parcelId) {
        return iParcelsTrackingAppRepository.findByParcelId(parcelId);
    }

    public void saveParcel(@NonNull ParcelEntity parcelEntity) {
        iParcelsTrackingAppRepository.save(parcelEntity);
    }
}
