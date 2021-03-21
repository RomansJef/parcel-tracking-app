package com.parcels.tracking.app.controller;

import com.parcels.tracking.app.ParcelEntity;
import com.parcels.tracking.app.ParcelsTrackingAppController;
import com.parcels.tracking.app.response.GetAllParcelsResponse;
import com.parcels.tracking.app.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class GetAllParcelsServiceControllerTest {

    private final StoreNewParcelService storeNewParcelService = mock(StoreNewParcelService.class);
    private final GetAllParcelsService getAllParcelsService = mock(GetAllParcelsService.class);
    private final GetParcelsByClientIdService getParcelsByClientIdService =
            mock(GetParcelsByClientIdService.class);
    private final GetByParcelIdService getByParcelIdService = mock(GetByParcelIdService.class);
    private final UpdateParcelService updateParcelService = mock(UpdateParcelService.class);
    private final ParcelsTrackingAppController appController =
            new ParcelsTrackingAppController(
                    storeNewParcelService,
                    getAllParcelsService,
                    getParcelsByClientIdService,
                    getByParcelIdService,
                    updateParcelService);
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new Exception(), appController).build();
    }

    @Test
    public void whenGetAllParcelsSuccess() throws Exception {
        List<ParcelEntity> parcelsList = new ArrayList<>();
        // response entity
        ParcelEntity parcelEntity =
                ParcelEntity.builder()
                        .parcelId("parcel0003")
                        .clientId("client0003")
                        .parcelType("flowers")
                        .build();
        parcelsList.add(parcelEntity);
        GetAllParcelsResponse getAllParcelsResponse =
                GetAllParcelsResponse.builder().parcelsList(parcelsList).build();
        // when
        when(getAllParcelsService.executeRequest()).thenReturn(getAllParcelsResponse);

        // then
        mockMvc
                .perform(
                        get("/allParcels")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.parcelsList").exists())
                .andExpect(jsonPath("$.parcelsList[0].parcelId").exists())
                .andExpect(jsonPath("$.parcelsList[0].clientId").exists())
                .andDo(print());
    }
}
