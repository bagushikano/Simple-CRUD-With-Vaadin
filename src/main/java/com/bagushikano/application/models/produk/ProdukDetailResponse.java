package com.bagushikano.application.models.produk;

public record ProdukDetailResponse(
        Integer statusCode,
        Boolean status,
        Produk produk,
        String message
) {
}
