package com.bagushikano.application.services;

import com.bagushikano.application.models.merek.MerekDetailResponse;
import com.bagushikano.application.models.merek.MerekGetResponse;
import com.bagushikano.application.models.merek.MerekProdukCreateRequest;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.bagushikano.application.Const;

public class MerekClient {
    RestTemplate restTemplate = new RestTemplate();
    public MerekGetResponse getALlMerek() {
        var url = Const.BASE_URL + "/api/merek/get";
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> request = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        ResponseEntity<MerekGetResponse> response = restTemplate.exchange(request, MerekGetResponse.class);
        return response.getBody();
    }

    public MerekDetailResponse getDetailMerek(String idMerek) {
        String url = String.format("%s%s%s", Const.BASE_URL, "/api/merek/detail/", idMerek);
        RequestEntity<Void> request = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        ResponseEntity<MerekDetailResponse> response = restTemplate.exchange(request, MerekDetailResponse.class);
        return response.getBody();
    }
    public MerekDetailResponse createMerek(String merek, String keterangan) {
        var url = Const.BASE_URL + "/api/merek/create";
        MerekProdukCreateRequest requestBody = new MerekProdukCreateRequest(merek, keterangan);
        RequestEntity<MerekProdukCreateRequest> request = RequestEntity
                .post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(requestBody);
        ResponseEntity<MerekDetailResponse> response = restTemplate.exchange(request, MerekDetailResponse.class);
        return response.getBody();
    }

    public MerekDetailResponse updateMerek(String merek, String keterangan, String idMerek) {
        String url = String.format("%s%s%s", Const.BASE_URL, "/api/merek/update/", idMerek);
        MerekProdukCreateRequest requestBody = new MerekProdukCreateRequest(merek, keterangan);
        RequestEntity<MerekProdukCreateRequest> request = RequestEntity
                .post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(requestBody);
        ResponseEntity<MerekDetailResponse> response = restTemplate.exchange(request, MerekDetailResponse.class);
        return response.getBody();
    }

    public MerekDetailResponse deleteMerek(String idMerek) {
        String url = String.format("%s%s%s", Const.BASE_URL, "/api/merek/delete/", idMerek);
        RequestEntity<Void> request = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        ResponseEntity<MerekDetailResponse> response = restTemplate.exchange(request, MerekDetailResponse.class);
        return response.getBody();
    }
}
