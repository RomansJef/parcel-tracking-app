package com.parcels.tracking.app;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@Log4j2
@RunWith(MockitoJUnitRunner.class)
public class ParcelsTrackingAppRepositoryTest {

    private final IParcelsTrackingAppRepository iTrackingAppRepository = mock(IParcelsTrackingAppRepository.class);
    private final ParcelsTrackingAppRepository appRepository = new ParcelsTrackingAppRepository(iTrackingAppRepository);
    private ParcelEntity parcelEntity;

    @Before
    public void setUp() {
        // response from repository
        parcelEntity = ParcelEntity.builder()
                .id("14c26dee-a039-4019-97cf-51d0aff9b19c")
                .parcelId("parcel0001")
                .clientId("client0001")
                .parcelType("package")
                .registrationDate(LocalDate.of(2021, 2, 17))
                .delivered(false)
                .build();
    }

    @Test
    public void whenGetAllNotNull() {
        assertNotNull(appRepository.getAllParcels());
    }

    @Test
    public void whenGetByParcelIdNotNull() {
        assertNotNull(appRepository.getByParcelId("id"));
    }

    @Test
    public void whenGetParcelByClientIdNotNull() {
        assertNotNull(appRepository.getParcelsByClientId("id"));
    }

    @Test
    public void whenSaveParcelNotNull() {
        appRepository.saveParcel(parcelEntity);
        Assertions.assertThat(appRepository.getByParcelId("parcel0001")).isEqualTo(Optional.empty());
    }
}
