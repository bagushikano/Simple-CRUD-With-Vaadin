package com.bagushikano.application.models.merek;

public record MerkGetPaginatedResponse(
        String statusCode,
        Boolean status,
        MerekPaginated merekProdukList,
        String message
) {
}
