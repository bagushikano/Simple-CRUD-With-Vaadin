package com.bagushikano.application.models.dokumen;

import com.bagushikano.application.models.produk.Produk;

import java.util.List;

public record DokumenGetRequest (
        Integer statusCode,
        Boolean status,
        List<Dokumen> produkList,
        String message
){
}
