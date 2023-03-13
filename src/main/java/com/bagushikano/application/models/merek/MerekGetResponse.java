package com.bagushikano.application.models.merek;

import java.util.List;

public record MerekGetResponse(
       Integer statusCode,
       Boolean status,
       List<MerekProduk> merekProdukList,
       String message
) {
}
