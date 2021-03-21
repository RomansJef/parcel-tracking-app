package com.parcels.tracking.app;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * DTO and DB fields for Parcels Tracking App.
 */
@Data
@Entity
@Table(name = "PARCELS")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParcelEntity {

    /**
     * ID for Parcel stored in DB.
     */
    @Id
    @Column(name = "ID")
    @NotNull(message = "ID cannot be null")
    private String id;

    /**
     * Parcel ID.
     */
    @Column(name = "PARCEL_ID")
    @NotNull(message = "Parcel ID cannot be null")
    private String parcelId;


    /**
     * Client ID for Parcel stored in DB.
     */
    @Column(name = "CLIENT_ID")
    @NotNull(message = "Client ID cannot be null")
    private String clientId;

    /**
     * Type of the Parcel.
     */
    @Column(name = "PARCEL_TYPE")
    @NotNull(message = "Parcel type cannot be null")
    private String parcelType;

    /**
     * Parcel registration date.
     */
    @Column(name = "PARCEL_REGISTERED")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Parcel registration date cannot be null")
    private LocalDate registrationDate;

    /**
     * Parcel delivered to client, by default false.
     */
    @Column(name = "PARCEL_DELIVERED")
    private Boolean delivered;

    /**
     * Parcel delivery date if delivered.
     */
    @Column(name = "PARCEL_DELIVERY_DATE")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate deliveryDate;

    @Override
    public String toString() {
        return "Report{" + "ID: " + id +
                ", ParcelId: '" + parcelId + '\'' +
                ", ClientId: '" + clientId + '\'' +
                ", ParcelType: '" + parcelType + '\'' +
                ", RegistrationDate: '" + registrationDate + '\'' +
                ", ParcelDelivered: '" + delivered + '\'' +
                ", DeliveryDate: '" + deliveryDate + '\'' +
                '}';
    }
}
