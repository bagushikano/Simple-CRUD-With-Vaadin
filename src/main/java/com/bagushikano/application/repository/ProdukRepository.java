package com.bagushikano.application.repository;

import com.bagushikano.application.models.merek.MerekDetailResponse;
import com.bagushikano.application.models.merek.MerekProduk;
import com.bagushikano.application.models.produk.Produk;
import com.bagushikano.application.models.produk.ProdukDetailResponse;
import com.bagushikano.application.models.produk.ProdukGetResponse;
import com.bagushikano.application.services.ProdukClient;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;

public class ProdukRepository {
    private final ProdukClient produkClient = new ProdukClient();

    public ArrayList<Produk> getProduk() {
        ProdukGetResponse produkGetResponse = produkClient.getALlProduk();
        if (produkGetResponse.status()) {
            return (ArrayList<Produk>) produkGetResponse.produkList();
        } else {
            return null;
        }
    }
    public Produk createProduk(String namaProduk, String desc,
                               String idMerek , String filename,
                               byte[] produkImage) {
        ProdukDetailResponse produkDetailResponse;
        try {
            produkDetailResponse = produkClient.createProduk(namaProduk, desc,
                    idMerek, filename, produkImage);
            if (produkDetailResponse.status()) {
                return produkDetailResponse.produk();
            } else {
                return null;
            }
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public Produk updateProduk(String namaProduk, String desc,
                               String idMerek , String filename,
                               byte[] produkImage, String idProduk) {
        ProdukDetailResponse produkDetailResponse;
        try {
            produkDetailResponse = produkClient.updateProduk(namaProduk, desc,
                    idMerek, filename, idProduk, produkImage);
            if (produkDetailResponse.status()) {
                return produkDetailResponse.produk();
            } else {
                return null;
            }
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public Boolean deleteProduk(String idProduk) {
        ProdukDetailResponse produkDetailResponse;
        try {
            produkDetailResponse = produkClient.deleteProduk(idProduk);
            return produkDetailResponse.status();
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    public Produk detailProduk(String idProduk) {
        ProdukDetailResponse produkDetailResponse;
        try {
            produkDetailResponse = produkClient.getDetailProduk(idProduk);
            if (produkDetailResponse.status()) {
                return produkDetailResponse.produk();
            } else {
                return null;
            }
        } catch (HttpClientErrorException e) {
            return null;
        }
    }
}
