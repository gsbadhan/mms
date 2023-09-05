package mms.service.catalog;

import mms.dto.catalog.AudiResponse;
import mms.dto.catalog.AudiUpsertRequest;

public interface AudiService {
    AudiResponse saveAudi(AudiUpsertRequest request) throws Exception;

    AudiResponse getAudi(Integer id) throws Exception;

    AudiResponse updateAudi(Integer id, AudiUpsertRequest request) throws Exception;

    void deleteAudi(Integer id) throws Exception;
}
