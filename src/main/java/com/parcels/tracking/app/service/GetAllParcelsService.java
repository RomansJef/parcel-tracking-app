package com.parcels.tracking.app.service;

import com.parcels.tracking.app.ParcelEntity;
import com.parcels.tracking.app.ParcelsTrackingAppRepository;
import com.parcels.tracking.app.response.GetAllParcelsResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Get all parcels stored in DB response.
 */
@Log4j2
@Service
public class GetAllParcelsService {
    private final ParcelsTrackingAppRepository parcelsTrackingAppRepository;

    public GetAllParcelsService(ParcelsTrackingAppRepository parcelsTrackingAppRepository) {
        this.parcelsTrackingAppRepository = parcelsTrackingAppRepository;
    }

    public GetAllParcelsResponse executeRequest() {
        List<ParcelEntity> parcelsEntities = parcelsTrackingAppRepository.getAllParcels().orElseThrow(
                () -> new RuntimeException("57e892f3-ef48-48a1-b561-a40a222cf26e - Data Base is empty"));
        return GetAllParcelsResponse.builder().parcelsList(parcelsEntities).build();
    }
}
