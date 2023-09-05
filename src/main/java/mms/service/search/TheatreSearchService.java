package mms.service.search;

import mms.dto.search.TheatreResponse;
import mms.dto.search.TheatreSearchRequest;

import java.util.List;

public interface TheatreSearchService {
    List<TheatreResponse> theatreSearch(TheatreSearchRequest request) throws Exception;
}
