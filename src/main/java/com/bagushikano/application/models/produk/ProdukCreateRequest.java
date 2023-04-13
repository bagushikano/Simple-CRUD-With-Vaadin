package com.bagushikano.application.models.produk;

import com.bagushikano.application.models.dokumen.Dokumen;

import java.lang.reflect.Array;
import java.util.ArrayList;

public record ProdukCreateRequest(
        String nama_produk,
        String desc,
        String merek_id,
        ArrayList<Dokumen> list_foto
) {
}
