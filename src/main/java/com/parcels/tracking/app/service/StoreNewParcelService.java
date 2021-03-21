package com.parcels.tracking.app.service;

import com.parcels.tracking.app.ParcelEntity;
import com.parcels.tracking.app.ParcelsTrackingAppRepository;
import com.parcels.tracking.app.requests.RequestValidator;
import com.parcels.tracking.app.requests.StoreNewParcelRequest;
import com.parcels.tracking.app.response.StoreNewParcelResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

/**
 * Service to store new parcel in DB.
 */
@Log4j2
@Service
public class StoreNewParcelService {

    private final ParcelsTrackingAppRepository parcelsTrackingAppRepository;
    private final RequestValidator requestValidator = new RequestValidator();

    public StoreNewParcelService(ParcelsTrackingAppRepository parcelsTrackingAppRepository) {
        this.parcelsTrackingAppRepository = parcelsTrackingAppRepository;
    }

    private final StoreNewParcelResponse response = new StoreNewParcelResponse();

    public StoreNewParcelResponse executeRequest(StoreNewParcelRequest storeNewParcelRequest) {
        // Validates StoreNewParcelRequest fields
        Validate.notNull(storeNewParcelRequest, "9a1aec3e-8872-4975-bf0f-aef0a9e759c8 - Missing New Parcel Request");
        Validate.notNull(storeNewParcelRequest.getParcelId(), "d87c2346-89ab-4fd3-82a2-7f9ca9987b84 - Parcel ID cannot be null");
        Validate.notNull(storeNewParcelRequest.getClientId(), "302eea49-83f4-43d0-9f64-e088dab61d5d - Client ID cannot be null");
        Validate.notEmpty(storeNewParcelRequest.getParcelId(), "8c40138d-9414-46b7-b447-20311bcba395 - Parcel ID cannot be empty");
        Validate.notEmpty(storeNewParcelRequest.getClientId(), "c01e6ba2-e8b3-44bf-9e81-16050bb06674 - Client ID cannot be empty");

        // Validates Delivery conditions
        requestValidator.validateDeliveryConditionsForNewParcel(storeNewParcelRequest);

        String status;
        List<ParcelEntity> parcelEntities = parcelsTrackingAppRepository.getAllParcels().orElseThrow(
                () -> new RuntimeException("3745cb5a-7e7e-461f-b0a0-a79345d285bc - Get all parcels error"));

        if (!isAlreadyStored(parcelEntities, storeNewParcelRequest)) {
            ParcelEntity newParcelEntity =
                    ParcelEntity.builder()
                            .id(UUID.randomUUID().toString())
                            .parcelId(storeNewParcelRequest.getParcelId())
                            .clientId(storeNewParcelRequest.getClientId())
                            .parcelType(storeNewParcelRequest.getParcelType())
                            .build();

            // Checks registration date for null
            if (storeNewParcelRequest.getRegistrationDate() != null) {
                LocalDate registrationDate = requestValidator.validateRegistrationDate(storeNewParcelRequest.getRegistrationDate());
                newParcelEntity.setRegistrationDate(registrationDate);
            } else {
                newParcelEntity.setRegistrationDate(LocalDate.now());
            }

            // Checks Delivered field is present and is true
            if (storeNewParcelRequest.getDelivered() != null && storeNewParcelRequest.getDelivered()) {
                newParcelEntity.setDelivered(storeNewParcelRequest.getDelivered());
            } else {
                newParcelEntity.setDelivered(false);
            }

            // Checks delivery date for null
            if (storeNewParcelRequest.getDeliveryDate() != null) {
                LocalDate deliveryDate = requestValidator.validateDeliveryDate(storeNewParcelRequest.getDeliveryDate());
                newParcelEntity.setDeliveryDate(deliveryDate);
            }

            // Check if parcel delivered before than registered
            if ((newParcelEntity.getRegistrationDate() != null && newParcelEntity.getDeliveryDate() != null) &&
                    newParcelEntity.getRegistrationDate().isAfter(newParcelEntity.getDeliveryDate())) {
                throw new RuntimeException("84a58171-bd2e-48c2-904e-6ad564d223e4 - Parcel registration date cannot be later than delivery date");
            }

            parcelsTrackingAppRepository.saveParcel(newParcelEntity);
            status = "New parcel " + storeNewParcelRequest.getParcelId() + " stored in Parcels Tracking App Repository";
        } else {
            status = "Parcel " + storeNewParcelRequest.getParcelId() + " already presented in Parcels Tracking App Repository";
        }
        response.setStatus(status);
        return response;
    }

    /**
     * Check if parcel already stored in DB
     */
    public boolean isAlreadyStored(
            List<ParcelEntity> parcelEntities, StoreNewParcelRequest storeNewParcelRequest) {
        for (ParcelEntity parcelEntity : parcelEntities) {
            if (storeNewParcelRequest.getParcelId().equals(parcelEntity.getParcelId())) return true;
        }
        return false;
    }
}
