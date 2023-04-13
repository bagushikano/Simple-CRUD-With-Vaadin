package com.bagushikano.application.views.simplecrud.produk;

import com.bagushikano.application.models.merek.MerekProduk;
import com.bagushikano.application.models.produk.Produk;
import com.bagushikano.application.repository.MerekRepository;
import com.bagushikano.application.repository.ProdukRepository;
import com.bagushikano.application.views.MainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.component.upload.receivers.FileData;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@PageTitle("Create Produk")
@Route(value = "produk/create", layout = MainLayout.class)
@AnonymousAllowed
public class CreateProdukView extends VerticalLayout {
    private final TextField namaProdukField = new TextField("Nama Produk");
    private final TextArea descProdukField = new TextArea("Keterangan Produk");
    private final Button submitDataButton = new Button("Simpan data");
    private final H1 pageTitle = new H1("Create Produk Baru");
    private final ProgressBar progressBar = new ProgressBar();
    private ProdukRepository produkRepository = new ProdukRepository();
    private Produk produk;
    private ArrayList<MerekProduk> merekProdukArrayList = new ArrayList<>();
    private MerekRepository merekRepository = new MerekRepository();
    private Image imageProduk = new Image();
    private MemoryBuffer memoryBuffer = new MemoryBuffer();
    private VerticalLayout uploadFotoContainer = new VerticalLayout();
    private Upload uploadFotoProduk = new Upload(memoryBuffer);
    private H4 uploadFotoTitle = new H4("Upload Foto Produk");
    private Paragraph uploadFotoHint = new Paragraph("Format file: PNG,JPG,JPEG (.png, .jpg, .jpeg). Ukuran Maks 5MB");
    private byte[] fotoProdukFile;
    private String produkFileName;
    private Boolean fileReady = false;
    private final ComboBox<MerekProduk> merekProdukComboBox = new ComboBox<>("Pilih Merek Produk");
    public CreateProdukView() {
        /**
         * https://vaadin.com/docs/latest/components/upload
         */
        uploadFotoTitle.getStyle().set("margin-top", "0");
        uploadFotoHint.getStyle().set("color", "var(--lumo-secondary-text-color)");
        int maxFileSizeInBytes = 5 * 1024 * 1024; // 5mb
        uploadFotoProduk.setMaxFileSize(maxFileSizeInBytes);
        uploadFotoProduk.setAcceptedFileTypes("image/png", "image/jpg", "image/jpeg");

        uploadFotoProduk.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();
            Notification notification = Notification.show(errorMessage, 5000,
                    Notification.Position.MIDDLE);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        });

        uploadFotoProduk.addSucceededListener(event -> {
            // Get information about the uploaded file
            InputStream fileData = memoryBuffer.getInputStream();
            String fileName = event.getFileName();
            long contentLength = event.getContentLength();
            String mimeType = event.getMIMEType();
            prepareFileToPost(fileData, fileName);
        });
        uploadFotoContainer.add(uploadFotoTitle, uploadFotoHint, uploadFotoProduk);
        uploadFotoContainer.setMargin(false);
        uploadFotoContainer.setSpacing(false);

        progressBar.setWidth("15em");
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        namaProdukField.setWidthFull();
        descProdukField.setWidthFull();
        merekProdukComboBox.setWidthFull();
        merekProdukComboBox.setItemLabelGenerator(MerekProduk::getMerek);
        setWidth("50%");
        submitDataButton.setIcon(new Icon("vaadin", "plus"));
        submitDataButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if (fileReady) {
                    submitData(
                            namaProdukField.getValue(),
                            descProdukField.getValue(),
                            merekProdukComboBox.getValue().getId().toString(),
                            produkFileName, fotoProdukFile
                    );
                } else {
                    Notification.show("File belum siap");
                }
            }
        });
        getDaftarMerek();
        add(pageTitle, namaProdukField, descProdukField,
                merekProdukComboBox, uploadFotoContainer, submitDataButton, progressBar);
    }

    public void prepareFileToPost (InputStream file, String fileName) {
        produkFileName = fileName;
        fileReady = false;
        try {
            fotoProdukFile = file.readAllBytes();
            fileReady = true;
        } catch (IOException e) {
            fileReady = false;
            throw new RuntimeException(e);
        }
    }

    public void submitData(String namaProduk, String desc, String idMerek,
                           String filename, byte[] produkImage) {
        progressBar.setVisible(true);
        produk = produkRepository.createProduk(namaProduk, desc, idMerek, filename, produkImage);
        progressBar.setVisible(false);
        if (produk != null) {
            Notification.show("Berhasil membuat produk");
            UI.getCurrent().navigate("produk");
        } else {
            Notification.show("Gagal membuat produk");
        }
    }

    public void getDaftarMerek() {
        merekProdukArrayList = merekRepository.getMerek();
        if (merekProdukArrayList == null) {
            Notification.show("Gagal mengambil data Merek!");
        } else {
            merekProdukComboBox.setItems(merekProdukArrayList);
        }
    }
}
