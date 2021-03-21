package com.parcels.tracking.app.service;

import com.parcels.tracking.app.ParcelEntity;
import com.parcels.tracking.app.ParcelsTrackingAppRepository;
import com.parcels.tracking.app.response.GetAllParcelsResponse;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@Log4j2
@RunWith(MockitoJUnitRunner.class)
public class GetAllParcelsServiceTest {

    private final ParcelsTrackingAppRepository trackingAppRepository = mock(ParcelsTrackingAppRepository.class);
    private final GetAllParcelsService getAllParcelsService =
            new GetAllParcelsService(trackingAppRepository);

    @Test
    public void whenGetAllParcelsSuccess() {
        // entity
        ParcelEntity parcelEntity =
                ParcelEntity.builder()
                        .id("14c26dee-a039-4019-97cf-51d0aff9b19c")
                        .parcelId("parcel0001")
                        .clientId("client0001")
                        .parcelType("package")
                        .registrationDate(LocalDate.of(2021, 2, 17))
                        .delivered(true)
                        .deliveryDate(LocalDate.of(2021, 2, 18))
                        .build();

        // response
        List<ParcelEntity> parcelsList = new ArrayList<>();
        parcelsList.add(parcelEntity);

        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.of(parcelsList));
        GetAllParcelsResponse actual = getAllParcelsService.executeRequest();
        verify(trackingAppRepository, times(1)).getAllParcels();

        // set auto generated uuid
        parcelEntity.setId(actual.getParcelsList().get(0).getId());
        List<ParcelEntity> newParcelsList = new ArrayList<>();
        newParcelsList.add(parcelEntity);
        GetAllParcelsResponse getAllParcelsResponse = GetAllParcelsResponse.builder()
                .parcelsList(newParcelsList).build();
        assertThat(actual).isEqualTo(getAllParcelsResponse);
    }

    @Test
    public void whenGetAllParcelsNullRepository() {
        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.ofNullable(null));
        RuntimeException exception = assertThrows(RuntimeException.class,
                getAllParcelsService::executeRequest);
        assertThat(exception.getMessage()).isEqualTo("57e892f3-ef48-48a1-b561-a40a222cf26e - Data Base is empty");
    }

    @Test
    public void whenGetAllParcelsEmptyRepository() {
        when(trackingAppRepository.getAllParcels()).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                getAllParcelsService::executeRequest);
        assertThat(exception.getMessage()).isEqualTo("57e892f3-ef48-48a1-b561-a40a222cf26e - Data Base is empty");
    }
}
