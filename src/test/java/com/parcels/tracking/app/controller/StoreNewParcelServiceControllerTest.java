package com.parcels.tracking.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parcels.tracking.app.ParcelsTrackingAppController;
import com.parcels.tracking.app.requests.StoreNewParcelRequest;
import com.parcels.tracking.app.response.StoreNewParcelResponse;
import com.parcels.tracking.app.service.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class StoreNewParcelServiceControllerTest {

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
    public void whenStoreNewParcelSuccess() throws Exception {
        // request entity
        StoreNewParcelRequest storeNewParcelRequest = StoreNewParcelRequest.builder()
                .parcelId("parcel0001")
                .clientId("client0001")
                .parcelType("package")
                .build();

        // response
        StoreNewParcelResponse storeNewParcelResponse = StoreNewParcelResponse.builder()
                .status("Parcel is saved").build();

        // when
        when(storeNewParcelService.executeRequest(storeNewParcelRequest)).thenReturn(storeNewParcelResponse);

        // then
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        post("/newParcel")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(storeNewParcelRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").exists())
                .andDo(print());
    }

    @Test
    public void whenStoreNewParcelNull() throws Exception {
        // request entity
        StoreNewParcelRequest storeNewParcelRequest = StoreNewParcelRequest.builder()
                .parcelId(null)
                .clientId(null)
                .parcelType(null)
                .build();

        // then
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        post("/newParcel")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(storeNewParcelRequest)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void whenStoreNewParcelRequestNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        post("/newParcel")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(null)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
