package mms.service.catalog;

import mms.dto.catalog.ShowResponse;
import mms.dto.catalog.ShowUpsertRequest;

public interface ShowsService {
    ShowResponse saveShow(ShowUpsertRequest request) throws Exception;

    ShowResponse getShow(Integer id) throws Exception;

    ShowResponse updateShow(Integer id, ShowUpsertRequest request) throws Exception;

    void deleteShow(Integer id) throws Exception;

    Integer getFreeSeatsCount(Integer id) throws Exception;
}
