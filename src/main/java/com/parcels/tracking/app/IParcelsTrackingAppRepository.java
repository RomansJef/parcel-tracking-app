package com.parcels.tracking.app;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "parcelentities", path = "parcelentity")
public interface IParcelsTrackingAppRepository extends JpaRepository<ParcelEntity, String> {

    Optional<List<ParcelEntity>> findAllByOrderByDeliveredAsc();

    Optional<List<ParcelEntity>> findAllByClientIdOrderByRegistrationDate(@Param("clientId") @NonNull String clientId);

    Optional<ParcelEntity> findByParcelId(@Param("parcelId") @NonNull String parcelId);
}
