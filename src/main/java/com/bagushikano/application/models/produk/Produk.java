package com.bagushikano.application.models.produk;

import com.bagushikano.application.models.merek.MerekProduk;
import com.bagushikano.application.utils.ChangeDateTimeFormat;
import lombok.Data;

@Data
public class Produk {
    Integer id;
    String nama_produk;
    String merek_id;
    String desc;
    String created_at;
    String deleted_at;
    String foto;
    MerekProduk merek_produk;
    public String convertDateTime() {
        return ChangeDateTimeFormat.changeDateTimeFormatForCreatedAt(getCreated_at());
    }
    public String getProdukMerekName() {
        return merek_produk.getMerek();
    }
}
