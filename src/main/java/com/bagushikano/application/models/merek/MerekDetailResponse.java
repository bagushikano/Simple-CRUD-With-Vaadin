package com.bagushikano.application.models.merek;

public record MerekDetailResponse (
        Integer statusCode,
        Boolean status,
        MerekProduk merekProduk,
        String message
) {
}
