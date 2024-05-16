package com.primeit.spacefleet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.primeit.spacefleet.controller.SpaceFleetController;
import com.primeit.spacefleet.domain.SpaceShip;
import com.primeit.spacefleet.service.SpaceShipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = SpaceFleetController.class)
@AutoConfigureMockMvc
public class SpaceFleetRestControllerTest {
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private SpaceShipService spaceShipService;

    @Test
    public void whenValidFindAllReturnsOk() throws Exception {
        List<SpaceShip> spaceShipList = new ArrayList<>();
        Page<SpaceShip> pagedSpaceShips = new PageImpl<>(spaceShipList, PageRequest.of(0, 10), 3);

        Mockito.when(this.spaceShipService.findAll(any())).thenReturn(pagedSpaceShips);

        mockMvc.perform(get("/fleet/all")).andExpect(status().isOk());
    }

    @Test
    public void whenInvalidFindAllReturnsKo() throws Exception {
        List<SpaceShip> spaceShipList = new ArrayList<>();
        Page<SpaceShip> pagedSpaceShips = new PageImpl<>(spaceShipList, PageRequest.of(0, 10), 3);

        Mockito.when(this.spaceShipService.findAll(any())).thenReturn(pagedSpaceShips);

        mockMvc.perform(post("/fleet/all")).andExpect(status().is5xxServerError());
    }

    @Test
    public void whenValidFindByIdReturnsOk() throws Exception {
        SpaceShip spaceShip = new SpaceShip(200l, "Ala-Y");
        Mockito.when(this.spaceShipService.findById(spaceShip.getId())).thenReturn(Optional.of(spaceShip));

        mockMvc.perform(get("/fleet/200")).andExpect(status().isOk());
    }

    @Test
    public void whenInvalidFindByIdReturnsKo() throws Exception {
        SpaceShip spaceShip = new SpaceShip(2l, "Ala-Y");
        Mockito.when(this.spaceShipService.findById(spaceShip.getId())).thenReturn(Optional.of(spaceShip));

        mockMvc.perform(get("/fleet/-2")).andExpect(status().is4xxClientError());
    }

    @Test
    public void whenValidCreateCallThenReturnsOk() throws Exception {
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setName("Ala-Y");

        mockMvc.perform(post("/fleet/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(spaceShip))).andExpect(status().isOk());
    }

    @Test
    public void whenInvalidCreateCallThenReturnsKo() throws Exception {
        mockMvc.perform(post("/fleet/").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SpaceShip()))).andExpect(status().is5xxServerError());
    }


    @Test
    public void whenValidDeleteCallThenReturnsOk() throws Exception {
        SpaceShip spaceShip = new SpaceShip(200l, "Ala-Y");
        Mockito.when(this.spaceShipService.findById(spaceShip.getId())).thenReturn(Optional.of(spaceShip));

        Mockito.doNothing().when(spaceShipService).delete(anyLong());

        mockMvc.perform(delete("/fleet/200")).andExpect(status().isOk());
    }

    @Test
    public void whenInvalidDeleteCallThenReturnsKo() throws Exception {
        long id = 100l;

        Mockito.doNothing().when(spaceShipService).delete(id);

        mockMvc.perform(delete("/fleet/-1")).andExpect(status().is4xxClientError());
    }
}
