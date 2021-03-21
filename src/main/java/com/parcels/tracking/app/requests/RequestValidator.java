package com.parcels.tracking.app.requests;

import com.parcels.tracking.app.ParcelEntity;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates fields in requests.
 */
@AllArgsConstructor
public class RequestValidator {

    /**
     * Validates Delivery conditions for new parcel.
     */
    public void validateDeliveryConditionsForNewParcel(StoreNewParcelRequest storeNewParcelRequest) {
        if ((storeNewParcelRequest.getDelivered() != null) && (storeNewParcelRequest.getDeliveryDate() == null)) {
            throw new RuntimeException("6a7cdd63-f145-4a07-85fa-8db028202ac6 - If parcel is delivered, Delivery Date cannot be null");
        }
        if ((storeNewParcelRequest.getDelivered() == null || !storeNewParcelRequest.getDelivered()) && storeNewParcelRequest.getDeliveryDate() != null) {
            throw new RuntimeException("59a1f173-8ab8-4c5f-8946-c4ca3c1822bb - If parcel is not delivered, Delivery Date cannot be presented");
        }
    }

    /**
     * Validates Delivery conditions updating parcel.
     */
    public void validateDeliveryConditionsUpdatingParcel(UpdateParcelRequest updateParcelRequest, ParcelEntity parcelEntity) {
        if (parcelEntity.getDelivered()) {
            throw new RuntimeException("00263119-5814-41a5-99b4-1a0197bbec9c - Parcel was already delivered");
        }
        if ((updateParcelRequest.getDelivered() != null) && (updateParcelRequest.getDeliveryDate() == null)) {
            throw new RuntimeException("a732d13b-6057-4e4e-8dae-bc069a240fb5 - If parcel is delivered, Delivery Date cannot be null");
        }
        if ((updateParcelRequest.getDelivered() == null || !updateParcelRequest.getDelivered()) && updateParcelRequest.getDeliveryDate() != null) {
            throw new RuntimeException("40c724ce-cf5f-4979-b461-efe22d26dd6f - If parcel is not delivered, Delivery Date cannot be presented");
        }
    }

    /**
     * Validates Registration Date.
     */
    public LocalDate validateRegistrationDate(String date) {
        // Check format of String Registration Date
        if (validateStringFormat(date)) {
            throw new RuntimeException("d3a30226-5389-4009-8b43-4d685dfe9e42 - Registration Date should be in format yyyy-MM-dd");
        }
        LocalDate registration = LocalDate.parse(date);
        if (registration.isAfter(LocalDate.now())) {
            throw new RuntimeException("f5b128ef-4813-4b13-a7a1-80f8048b7d80 - Registration Date cannot be in future");
        }
        return registration;
    }

    /**
     * Validates Delivery Date.
     */
    public LocalDate validateDeliveryDate(String date) {
        // Check format of String Delivery Date
        if (validateStringFormat(date)) {
            throw new RuntimeException("3572d01e-7f46-437a-b825-7ae8be22d42a - Delivery Date should be in format yyyy-MM-dd");
        }
        LocalDate localDate = LocalDate.parse(date);
        if (localDate.isAfter(LocalDate.now())) {
            throw new RuntimeException("9c1132b8-2b45-4dc7-b209-1ab36e86f003 - Delivery Date cannot be in future");
        }
        return localDate;
    }

    /**
     * Validates format of string as date.
     */
    private boolean validateStringFormat(String date) {
        Pattern pattern = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        Matcher matcher = pattern.matcher(date);
        return !matcher.find();
    }
}
