package com.bagushikano.application.models.merek;

import java.util.List;

public record MerekPaginated(
        Integer current_page,
        List<MerekProduk> data,
        String first_page_url,
        Integer from,
        Integer last_page,
        String last_page_url,
        String next_page_url,
        Integer per_page,
        String prev_page_url,
        Integer to,
        Integer total
) {
}
