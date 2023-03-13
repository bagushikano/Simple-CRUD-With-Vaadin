package com.bagushikano.application.models.merek;

import com.bagushikano.application.utils.ChangeDateTimeFormat;
import lombok.Data;

/*
    Ini model data untuk tb_merek_produk
    @Data itu dateng dari library Lombok, jadi yang isi itu ga perlu lagi bikin
    constructor model nya. getter & setter, udah otomatis di generate dengan @Data itu
 */
@Data
public class MerekProduk {
    Integer id;
    String merek;
    String keterangan;
    String created_at;
    String deleted_at;
    String updated_at;
    String foto;

    //For converting datetime format on laravel to dd-MMMM-yyyy, HH:mm
    public String convertDateTime() {
        return ChangeDateTimeFormat.changeDateTimeFormatForCreatedAt(getCreated_at());
    }
}
