package com.bagushikano.application.views.simplecrud.merek;

import com.bagushikano.application.components.widget.CustomPagination;
import com.bagushikano.application.models.merek.MerekPaginated;
import com.bagushikano.application.models.merek.MerekProduk;
import com.bagushikano.application.repository.MerekRepository;
import com.bagushikano.application.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;

@PageTitle("Daftar Merek Paginated")
@Route(value = "merek/paginated", layout = MainLayout.class)
public class MerekWithPagination extends VerticalLayout {
    private ProgressBar progressBar = new ProgressBar();
    private Button createDataButton = new Button();
    private final H1 pageTitle = new H1("Daftar Data Merek Produk");

    private ArrayList<MerekProduk> merekList;
    private MerekPaginated merekPaginated;
    private MerekRepository merekRepository = new MerekRepository();
    Button prevButton = new Button("<");
    Button nextButton = new Button(">");
    Button goToButton = new Button("GoTo");
    private CustomPagination customPagination;
    Grid<MerekProduk> grid = new Grid<>();
    Integer currentPage;

    public MerekWithPagination() {
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

        progressBar.setWidth("15em");
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);

        merekPaginated = merekRepository.getMerekPaginated("1");

        progressBar.setVisible(false);

        if (merekPaginated == null) {
            Notification.show("Gagal mengambil data!");
        } else {
            grid.setItems(merekPaginated.data());
            customPagination = new CustomPagination(prevButton,
                    nextButton, goToButton, merekPaginated.last_page(),
                    merekPaginated.per_page(), merekPaginated.from(),
                    merekPaginated.total()
            );
            currentPage = merekPaginated.current_page();
            nextButton.setEnabled(currentPage != merekPaginated.last_page());
            prevButton.setEnabled(currentPage != 1);
        }
        nextButton.addClickListener(buttonClickEvent -> {
            getPage(currentPage + 1);
        });
        prevButton.addClickListener(buttonClickEvent ->  {
            getPage(currentPage - 1);
        });
        goToButton.addClickListener( buttonClickEvent -> {
            getPage(Integer.valueOf(customPagination.getGotoValue()));
        });

        createDataButton.setText("Tambah Data");
        createDataButton.setIcon(new Icon("vaadin", "plus"));
        createDataButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                UI.getCurrent().navigate("merek/create");
            }
        });
        add(pageTitle, createDataButton, grid, customPagination);
    }

    public void getPage(Integer page) {
        merekPaginated = merekRepository.getMerekPaginated(String.valueOf(page));
        progressBar.setVisible(false);
        if (merekPaginated == null) {
            Notification.show("Gagal mengambil data!");
        } else {
            grid.setItems(merekPaginated.data());
            currentPage = merekPaginated.current_page();
            nextButton.setEnabled(currentPage != merekPaginated.last_page());
            prevButton.setEnabled(currentPage != 1);
            customPagination.updateTotalDataText(merekPaginated.total(),
                    merekPaginated.from(), merekPaginated.to());
            customPagination.setCurrentPage(String.valueOf(merekPaginated.current_page()));
            grid.setPageSize(merekPaginated.per_page());
        }
    }
}
