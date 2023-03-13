package com.bagushikano.application.models.produk;

import java.util.List;

public record ProdukGetResponse(
        Integer statusCode,
        Boolean status,
        List<Produk> produkList,
        String message
) {
}
