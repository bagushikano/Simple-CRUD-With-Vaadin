package com.bagushikano.application.views.simplecrud.produk;

import com.bagushikano.application.models.merek.MerekProduk;
import com.bagushikano.application.models.produk.Produk;
import com.bagushikano.application.repository.MerekRepository;
import com.bagushikano.application.repository.ProdukRepository;
import com.bagushikano.application.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.ArrayList;

@PageTitle("Daftar Produk")
@Route(value = "produk", layout = MainLayout.class)
public class ProdukView extends VerticalLayout {
    private ProgressBar progressBar = new ProgressBar();
    private Button createDataButton = new Button();
    private final H1 pageTitle = new H1("Daftar Produk");

    private ArrayList<Produk> produkArrayList;
    private ProdukRepository produkRepository = new ProdukRepository();
    public ProdukView() {

        PaginatedGrid<Produk, ?> grid = new PaginatedGrid<>();

        grid.addColumn(Produk::getId).setHeader("ID").setSortable(true);
        grid.addColumn(Produk::getNama_produk).setHeader("Nama Produk").setSortable(true);
        grid.addColumn(Produk::getProdukMerekName).setHeader("Nama Merek").setSortable(true);
        grid.addColumn(Produk::getDesc).setHeader("Keterangan").setSortable(true);
        grid.addColumn(Produk::convertDateTime).setHeader("Date Created");
        grid.addColumn(new ComponentRenderer<>(Button::new, (button, produk) -> {
            button.addThemeVariants(ButtonVariant.LUMO_ICON);
            button.addClickListener(e -> this.navigateToProdukDetail(produk.getId().toString()));
            button.setIcon(new Icon(VaadinIcon.EDIT));
        })).setHeader("Aksi");
        grid.addItemClickListener(new ComponentEventListener<ItemClickEvent<Produk>>() {
            @Override
            public void onComponentEvent(ItemClickEvent<Produk> produkItemClickEvent) {
                navigateToProdukDetail(produkItemClickEvent.getItem().getId().toString());
            }
        });

        grid.setPaginatorSize(10);
        grid.setPageSize(5);

        progressBar.setWidth("15em");
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
        produkArrayList = produkRepository.getProduk();
        progressBar.setVisible(false);
        if (produkArrayList == null) {
            Notification.show("Gagal mengambil data!");
        } else {
            grid.setItems(produkArrayList);
        }
        createDataButton.setText("Tambah Data");
        createDataButton.setIcon(new Icon("vaadin", "plus"));
        createDataButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                UI.getCurrent().navigate("produk/create");
            }
        });

        add(pageTitle, createDataButton, grid);
    }

    public void navigateToProdukDetail(String idProduk) {
        UI.getCurrent().navigate(DetailProdukView.class, idProduk);
    }
}
