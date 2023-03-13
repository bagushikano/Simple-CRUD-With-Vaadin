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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Create Merek")
@Route(value = "merek/create", layout = MainLayout.class)
public class CreateMerekView extends VerticalLayout {
    private final TextField namaMerekField = new TextField("Nama merek");
    private final TextArea keteranganMerekField = new TextArea("Keterangan merek");
    private final Button submitDataButton = new Button("Simpan data");
    private final H1 pageTitle = new H1("Create Merek Baru");
    private final ProgressBar progressBar = new ProgressBar();
    private MerekRepository merekRepository = new MerekRepository();
    private MerekProduk merekProduk;

    public CreateMerekView() {
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
                submitData(namaMerekField.getValue(), keteranganMerekField.getValue());
            }
        });

        add(pageTitle, namaMerekField, keteranganMerekField, submitDataButton, progressBar);
    }


    public void submitData(String merek, String keterangan) {
        progressBar.setVisible(true);
        merekProduk = merekRepository.createMerek(merek, keterangan);
        if (merekProduk != null) {
            Notification.show("Berhasil membuat merek");
            UI.getCurrent().navigate("hello");
        } else {
            Notification.show("Gagal membuat merek");
        }
        progressBar.setVisible(false);
    }
}
