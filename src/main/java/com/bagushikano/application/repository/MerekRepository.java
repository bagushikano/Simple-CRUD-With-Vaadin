package com.bagushikano.application.repository;

import com.bagushikano.application.models.merek.MerekDetailResponse;
import com.bagushikano.application.models.merek.MerekGetResponse;
import com.bagushikano.application.models.merek.MerekProduk;
import com.bagushikano.application.services.MerekClient;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;

public class MerekRepository {
    private final MerekClient merekClient = new MerekClient();

    public ArrayList<MerekProduk> getMerek() {
        MerekGetResponse merekGetResponse = merekClient.getALlMerek();
        if (merekGetResponse.status()) {
            return (ArrayList<MerekProduk>) merekGetResponse.merekProdukList();
        } else {
            return null;
        }
    }

    public MerekProduk createMerek(String merek, String keterangan) {
        MerekDetailResponse merekDetailResponse;
        try {
            merekDetailResponse = merekClient.createMerek(merek, keterangan);
            if (merekDetailResponse.status()) {
                return merekDetailResponse.merekProduk();
            } else {
                return null;
            }
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public MerekProduk updateMerek(String merek, String keterangan, String idMerek) {
        MerekDetailResponse merekDetailResponse;
        try {
            merekDetailResponse = merekClient.updateMerek(merek, keterangan, idMerek);
            if (merekDetailResponse.status()) {
                return merekDetailResponse.merekProduk();
            } else {
                return null;
            }
        } catch (HttpClientErrorException e) {
            return null;
        }
    }
    public Boolean deleteMerek(String idMerek) {
        MerekDetailResponse merekDetailResponse;
        try {
            merekDetailResponse = merekClient.deleteMerek(idMerek);
            return merekDetailResponse.status();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public MerekProduk detailMerek(String idMerek) {
        MerekDetailResponse merekDetailResponse;
        try {
            merekDetailResponse = merekClient.getDetailMerek(idMerek);
            if (merekDetailResponse.status()) {
                return merekDetailResponse.merekProduk();
            } else {
                return null;
            }
        } catch (HttpClientErrorException e) {
            return null;
        }
    }
}
