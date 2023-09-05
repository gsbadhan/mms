package mms.dao.search;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TheatreSearchRepositoryTestIT {

    @Autowired
    private TheatreSearchRepository theatreSearchRepository;

    @BeforeEach
    void setUp() {
        theatreSearchRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void saveTheatre() {
        Theatre theatre = new Theatre(null, 1001, "PVR", "Noida", 8001, new GeoPoint(77.23, 34.14));
        Theatre saved = theatreSearchRepository.save(theatre);
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    void findByCityNameSuccess() {
        //insert theatres
        Theatre theatre1 = new Theatre(null, 1001, "PVR", "Noida", 8001, new GeoPoint(77.23, 34.14));
        Theatre saved1 = theatreSearchRepository.save(theatre1);
        Theatre theatre2 = new Theatre(null, 1002, "PVR", "Gurgaon", 8901, new GeoPoint(66.21, 22.10));
        Theatre saved2 = theatreSearchRepository.save(theatre2);

        String cityName = "noida";
        Page<Theatre> pages = theatreSearchRepository.findByCityName(cityName, PageRequest.of(0, 5));
        assertFalse(pages.isEmpty());
        List<Theatre> theatres = pages.stream().toList();
        assertTrue(theatres.size() == 1);
        assertTrue(theatres.get(0).getTheatreId().equals(theatre1.getTheatreId()));
    }

    @Test
    void findNearBy() {
        //insert theatres
        Theatre theatre1 = new Theatre(null, 1001, "PVR", "Noida", 8001, new GeoPoint(77.23, 34.14));
        Theatre saved1 = theatreSearchRepository.save(theatre1);

        double lat = 77.23;
        double lng = 34.20;
        String distance = "5km";
        Page<Theatre> pages = theatreSearchRepository.findNearBy(lat, lng, distance, PageRequest.of(0, 5));
        assertFalse(pages.isEmpty());
        List<Theatre> theatres = pages.stream().toList();
        assertTrue(theatres.size() == 1);
        assertTrue(theatres.get(0).getTheatreId().equals(theatre1.getTheatreId()));
    }
}