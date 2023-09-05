package mms.controller.search;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mms.dao.search.Theatre;
import mms.dao.search.TheatreSearchRepository;
import mms.dto.search.TheatreResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TheatreControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TheatreSearchRepository theatreSearchRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        theatreSearchRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void searchTheatresByCity() throws Exception {
        //insert theatres
        Theatre theatre1 = new Theatre(null, 1001, "PVR", "Noida", 8001, new GeoPoint(77.23, 34.14));
        Theatre saved1 = theatreSearchRepository.save(theatre1);

        String cityName = "noida";
        ResultActions response = mockMvc.perform(get("/search-svc/v1/theatres/{filter-type}",
                TheatreSearchFilterType.city).param("city", cityName));
        MvcResult result = response.andDo(print()).andExpect(status().isOk()).andReturn();
        List<TheatreResponse> theatreList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<TheatreResponse>>() {
                });
        assertNotNull(theatreList);
        assertFalse(theatreList.isEmpty());
        assertTrue(theatreList.size() == 1);
        assertTrue(theatreList.get(0).getTheatreId().equals(theatre1.getTheatreId()));
    }

    @Test
    void searchTheatresByGeo() throws Exception {
        //insert theatres
        Theatre theatre1 = new Theatre(null, 1001, "PVR", "Noida", 8001, new GeoPoint(77.23, 34.14));
        Theatre saved1 = theatreSearchRepository.save(theatre1);

        Double lat = 77.23;
        Double lng = 34.20;
        ResultActions response = mockMvc.perform(get("/search-svc/v1/theatres/{filter-type}",
                TheatreSearchFilterType.geo).param("lat", lat.toString()).param("lng", lng.toString()));
        MvcResult result = response.andDo(print()).andExpect(status().isOk()).andReturn();
        List<TheatreResponse> theatreList = objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<TheatreResponse>>() {
                });
        assertNotNull(theatreList);
        assertFalse(theatreList.isEmpty());
        assertTrue(theatreList.size() == 1);
        assertTrue(theatreList.get(0).getTheatreId().equals(theatre1.getTheatreId()));
    }
}
