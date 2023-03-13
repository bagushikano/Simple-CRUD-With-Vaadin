package com.bagushikano.application.models.produk;

public record ProdukCreateRequest(
        String nama_produk,
        String desc,
        String merek_id
) {
}
