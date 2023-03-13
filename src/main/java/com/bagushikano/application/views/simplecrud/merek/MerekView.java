package com.bagushikano.application.views.simplecrud.merek;

import com.bagushikano.application.models.merek.MerekProduk;
import com.bagushikano.application.repository.MerekRepository;
import com.bagushikano.application.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.ArrayList;

@PageTitle("Daftar Merek")
@Route(value = "hello", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class MerekView extends VerticalLayout {
    private ProgressBar progressBar = new ProgressBar();
    private Button createDataButton = new Button();
    private final H1 pageTitle = new H1("Daftar Data Merek Produk");

    private ArrayList<MerekProduk> merekList;
    private MerekRepository merekRepository = new MerekRepository();

    public MerekView() {

        PaginatedGrid<MerekProduk, ?> grid = new PaginatedGrid<>();

        grid.addColumn(MerekProduk::getId).setHeader("ID").setSortable(true);
        grid.addColumn(MerekProduk::getMerek).setHeader("Merek").setSortable(true);
        grid.addColumn(MerekProduk::getKeterangan).setHeader("Keterangan").setSortable(true);
        grid.addColumn(MerekProduk::convertDateTime).setHeader("Date Created");
        grid.addItemClickListener(new ComponentEventListener<ItemClickEvent<MerekProduk>>() {
            @Override
            public void onComponentEvent(ItemClickEvent<MerekProduk> merekProdukItemClickEvent) {
                UI.getCurrent().navigate(DetailMerekView.class, merekProdukItemClickEvent.getItem().getId().toString());
            }
        });

        grid.setPaginatorSize(10);
        grid.setPageSize(5);

        progressBar.setWidth("15em");
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
        merekList = merekRepository.getMerek();
        progressBar.setVisible(false);
        if (merekList == null) {
            Notification.show("Gagal mengambil data!");
        } else {
            grid.setItems(merekList);
        }
        createDataButton.setText("Tambah Data");
        createDataButton.setIcon(new Icon("vaadin", "plus"));
        createDataButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                UI.getCurrent().navigate("merek/create");
            }
        });

        add(pageTitle, createDataButton, grid);
    }
}
