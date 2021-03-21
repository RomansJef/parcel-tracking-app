package com.parcels.tracking.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parcels.tracking.app.ParcelsTrackingAppController;
import com.parcels.tracking.app.requests.UpdateParcelRequest;
import com.parcels.tracking.app.response.UpdateParcelResponse;
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
public class UpdateParcelServiceControllerTest {

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
    public void whenGetParcelsByClientIdSuccess() throws Exception {
        // request entity
        UpdateParcelRequest updateParcelRequest = UpdateParcelRequest.builder()
                .parcelId("parcel0001")
                .delivered(true)
                .deliveryDate("2021-03-15")
                .build();

        // response
        UpdateParcelResponse updateParcelResponse = UpdateParcelResponse.builder()
                .status("Parcel is updated").build();

        // when
        when(updateParcelService.executeRequest(updateParcelRequest)).thenReturn(updateParcelResponse);

        // then
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        post("/updateParcel")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateParcelRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.status").exists())
                .andDo(print());
    }

    @Test
    public void whenUpdateParcelNull() throws Exception {
        // request entity
        UpdateParcelRequest updateParcelRequest = UpdateParcelRequest.builder()
                .parcelId(null)
                .build();

        // then
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        post("/updateParcel")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateParcelRequest)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    public void whenUpdateParcelRequestNull() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mockMvc
                .perform(
                        post("/updateParcel")
                                .characterEncoding("utf-8")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(null)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}
