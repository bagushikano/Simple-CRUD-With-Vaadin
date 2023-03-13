package com.bagushikano.application.services;

import com.bagushikano.application.Const;
import com.bagushikano.application.models.merek.MerekDetailResponse;
import com.bagushikano.application.models.merek.MerekGetResponse;
import com.bagushikano.application.models.merek.MerekProdukCreateRequest;
import com.bagushikano.application.models.produk.Produk;
import com.bagushikano.application.models.produk.ProdukCreateRequest;
import com.bagushikano.application.models.produk.ProdukDetailResponse;
import com.bagushikano.application.models.produk.ProdukGetResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

public class ProdukClient {
    RestTemplate restTemplate = new RestTemplate();
    public ProdukGetResponse getALlProduk() {
        var url = Const.BASE_URL + "/api/produk/get";
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> request = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        ResponseEntity<ProdukGetResponse> response = restTemplate.exchange(request, ProdukGetResponse.class);
        return response.getBody();
    }

    public ProdukDetailResponse getDetailProduk(String idProduk) {
        String url = String.format("%s%s%s", Const.BASE_URL, "/api/produk/detail/", idProduk);
        RequestEntity<Void> request = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        ResponseEntity<ProdukDetailResponse> response = restTemplate.exchange(request, ProdukDetailResponse.class);
        return response.getBody();
    }
    public ProdukDetailResponse createProduk(String namaProduk, String desc,
                                            String idMerek , String filename,
                                            byte[] produkImage) {

        String url = Const.BASE_URL + "/api/produk/create";

        ProdukCreateRequest produkCreateRequest = new ProdukCreateRequest(namaProduk, desc, idMerek);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        // This nested HttpEntiy is important to create the correct
        // Content-Disposition entry with metadata "name" and "filename"
        MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
        ContentDisposition contentDisposition = ContentDisposition
                .builder("form-data")
                .name("file")
                .filename(filename)
                .build();
        fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        HttpEntity<byte[]> fileEntity = new HttpEntity<>(produkImage, fileMap);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileEntity);
        body.add("produk", produkCreateRequest);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<ProdukDetailResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                ProdukDetailResponse.class
        );
        return response.getBody();
    }

    public ProdukDetailResponse updateProduk(String namaProduk, String desc,
                                            String idMerek , String filename,
                                            String idProduk,
                                            byte[] produkImage) {
        String url = String.format("%s%s%s", Const.BASE_URL, "/api/produk/update/", idProduk);

        ProdukCreateRequest produkCreateRequest = new ProdukCreateRequest(namaProduk, desc, idMerek);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        // This nested HttpEntiy is important to create the correct
        // Content-Disposition entry with metadata "name" and "filename"

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        if (filename != null) {
            MultiValueMap<String, String> fileMap = new LinkedMultiValueMap<>();
            ContentDisposition contentDisposition = ContentDisposition
                    .builder("form-data")
                    .name("file")
                    .filename(filename)
                    .build();
            fileMap.add(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
            HttpEntity<byte[]> fileEntity = new HttpEntity<>(produkImage, fileMap);
            body.add("file", fileEntity);
        }
        body.add("produk", produkCreateRequest);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<ProdukDetailResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                ProdukDetailResponse.class
        );
        return response.getBody();
    }

    public ProdukDetailResponse deleteProduk(String idMerek) {
        String url = String.format("%s%s%s", Const.BASE_URL, "/api/produk/delete/", idMerek);
        RequestEntity<Void> request = RequestEntity
                .get(url)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        ResponseEntity<ProdukDetailResponse> response = restTemplate.exchange(request, ProdukDetailResponse.class);
        return response.getBody();
    }
}
