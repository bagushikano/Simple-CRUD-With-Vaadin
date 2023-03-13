package com.bagushikano.application.views.simplecrud.merek;

import com.bagushikano.application.models.merek.MerekProduk;
import com.bagushikano.application.repository.MerekRepository;
import com.bagushikano.application.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

@PageTitle("Detail Merek")
@Route(value = "/merek/detail", layout = MainLayout.class)
public class DetailMerekView extends VerticalLayout implements HasUrlParameter<String> {
    private final TextField namaMerekField = new TextField("Nama merek");
    private final TextArea keteranganMerekField = new TextArea("Keterangan merek");
    private final Button submitDataButton = new Button("Update data");
    private final Button deleteDataButton = new Button("Delete data");
    private final H1 pageTitle = new H1("Detail Merek");
    private final ProgressBar progressBar = new ProgressBar();
    private MerekRepository merekRepository = new MerekRepository();
    private MerekProduk merekProduk;
    private String idMerekProduk;

    public DetailMerekView() {

    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        idMerekProduk = s;
        buildUI();
    }

    public void buildUI() {
        progressBar.setWidth("15em");
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        namaMerekField.setWidthFull();
        keteranganMerekField.setWidthFull();
        setWidth("50%");
        submitDataButton.setIcon(new Icon("vaadin", "plus"));
        submitDataButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                submitData(namaMerekField.getValue(), keteranganMerekField.getValue(), idMerekProduk);
            }
        });
        deleteDataButton.setIcon(new Icon("vaadin", "trash"));
        deleteDataButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                deleteData(idMerekProduk);
            }
        });
        add(pageTitle, namaMerekField, keteranganMerekField, submitDataButton, progressBar, deleteDataButton);
        getDetailmerek();
    }

    public void getDetailmerek() {
        hideForm();
        progressBar.setVisible(true);
        Notification.show("Mengambil detail merek...");
        merekProduk = merekRepository.detailMerek(idMerekProduk);
        if (merekProduk != null) {
            Notification.show("Berhasil mengambil detail merek");
            namaMerekField.setValue(merekProduk.getMerek());
            keteranganMerekField.setValue(merekProduk.getKeterangan());
            showForm();
        } else {
            Notification.show("Gagal mengambil detail merek");
        }
        progressBar.setVisible(false);
    }

    public void submitData(String merek, String keterangan, String idMerekProduk) {
        progressBar.setVisible(true);
        merekProduk = merekRepository.updateMerek(merek, keterangan, idMerekProduk);
        if (merekProduk != null) {
            Notification.show("Berhasil mengupdate merek");
            UI.getCurrent().navigate("hello");
        } else {
            Notification.show("Gagal mengupdate merek");
        }
        progressBar.setVisible(false);
    }
    public void deleteData(String idMerekProduk) {
        progressBar.setVisible(true);
        Boolean hasilDelete = merekRepository.deleteMerek(idMerekProduk);
        if (hasilDelete) {
            Notification.show("Berhasil menghapus merek");
            UI.getCurrent().navigate("hello");
        } else {
            Notification.show("Gagal menghapus merek");
        }
        progressBar.setVisible(false);
    }

    public void hideForm() {
        namaMerekField.setVisible(false);
        keteranganMerekField.setVisible(false);
        submitDataButton.setVisible(false);
        deleteDataButton.setVisible(false);
    }
    public void showForm() {
        deleteDataButton.setVisible(true);
        namaMerekField.setVisible(true);
        keteranganMerekField.setVisible(true);
        submitDataButton.setVisible(true);
    }
}
