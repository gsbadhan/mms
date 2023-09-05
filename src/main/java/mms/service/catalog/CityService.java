package mms.service.catalog;

import mms.dto.catalog.CityResponse;
import mms.dto.catalog.CityUpsertRequest;

import java.util.List;

public interface CityService {
    CityResponse saveCity(CityUpsertRequest request) throws Exception;

    CityResponse getCity(Integer id) throws Exception;;

    List<CityResponse> getCities(Integer page, Integer pageSize) throws Exception;

    boolean deleteCity(Integer id) throws Exception;

    CityResponse updateCity(Integer id, CityUpsertRequest request) throws Exception;
}
