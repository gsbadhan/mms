package mms.controller.search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import mms.dto.search.TheatreResponse;
import mms.dto.search.TheatreSearchRequest;
import mms.service.search.TheatreSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search-svc/v1/theatres")
@Slf4j
@CrossOrigin
@Tag(name = "search-service")
public class TheatreController {
    @Autowired
    private TheatreSearchService theatreSearchService;

    @Operation(summary = "Return list of theatres by city, lat/lng with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "500", description = "Error occurred while processing")
    })
    @GetMapping(value = "/{filter-type}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<TheatreResponse>> searchTheatres(@PathVariable("filter-type") TheatreSearchFilterType filterType, @RequestParam(required = false) String city,
                                                                @RequestParam(required = false) Double lat, @RequestParam(required = false) Double lng,
                                                                @RequestParam(defaultValue = "0", required = false) Integer page,
                                                                @RequestParam(defaultValue = "5", required = false) Integer pageSize) throws Exception {
        log.info("get all theatres by city request filterType={}, city={}, lat={}, lng={} page={}, pageSize={}", filterType, city, lat, lng, page, pageSize);
        List<TheatreResponse> results = theatreSearchService.theatreSearch(new TheatreSearchRequest(filterType, city, lat,
                lng, page, pageSize));
        return ResponseEntity.ok(results);
    }

}
