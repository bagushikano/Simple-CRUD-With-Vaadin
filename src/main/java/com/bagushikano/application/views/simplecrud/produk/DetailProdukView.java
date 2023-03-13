package com.bagushikano.application.views.simplecrud.produk;


import com.bagushikano.application.Const;
import com.bagushikano.application.models.merek.MerekProduk;
import com.bagushikano.application.models.produk.Produk;
import com.bagushikano.application.repository.MerekRepository;
import com.bagushikano.application.repository.ProdukRepository;
import com.bagushikano.application.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@PageTitle("Detail Produk")
@Route(value = "produk/detail", layout = MainLayout.class)
public class DetailProdukView extends VerticalLayout implements HasUrlParameter<String> {
    private final TextField namaProdukField = new TextField("Nama Produk");
    private final TextArea descProdukField = new TextArea("Keterangan Produk");
    private final Button submitDataButton = new Button("Simpan data");
    private final Button deleteDataButton = new Button("Delete data");
    private final H1 pageTitle = new H1("Create Produk Baru");
    private final ProgressBar progressBar = new ProgressBar();
    private ProdukRepository produkRepository = new ProdukRepository();
    private Produk produk;
    private ArrayList<MerekProduk> merekProdukArrayList = new ArrayList<>();
    private MerekRepository merekRepository = new MerekRepository();
    private Image imageProduk = new Image();
    private H4 imageTitle = new H4("Foto Produk");
    private MemoryBuffer memoryBuffer = new MemoryBuffer();
    private VerticalLayout uploadFotoContainer = new VerticalLayout();
    private Upload uploadFotoProduk = new Upload(memoryBuffer);
    private H4 uploadFotoTitle = new H4("Upload Foto Produk");
    private Paragraph uploadFotoHint = new Paragraph("Format file: PNG,JPG,JPEG (.png, .jpg, .jpeg). Ukuran Maks 5MB");
    private byte[] fotoProdukFile;
    private String produkFileName;
    private Boolean fileReady = false;
    private final ComboBox<MerekProduk> merekProdukComboBox = new ComboBox<>("Pilih Merek Produk");
    private String idProduk;

    public DetailProdukView() {
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        idProduk = s;
        buildUI();
    }

    public void buildUI() {
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
                submitData(
                        namaProdukField.getValue(),
                        descProdukField.getValue(),
                        merekProdukComboBox.getValue().getId().toString(),
                        produkFileName, fotoProdukFile, idProduk);
            }
        });

        deleteDataButton.setIcon(new Icon("vaadin", "trash"));
        deleteDataButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                deleteData(idProduk);
            }
        });
        getDaftarMerek();
        getDetailProduk();
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

    public void getDetailProduk() {
        hideForm();
        progressBar.setVisible(true);
        Notification.show("Mengambil detail produk...");
        produk = produkRepository.detailProduk(idProduk);
        if (produk != null) {
            Notification.show("Berhasil mengambil detail produk");
            namaProdukField.setValue(produk.getNama_produk());
            descProdukField.setValue(produk.getDesc());
            merekProdukComboBox.setValue(produk.getMerek_produk());
            if (produk.getFoto() != null) {
                imageProduk = new Image(Const.BASE_URL + produk.getFoto(), "Foto produk");
                imageProduk.setMaxHeight("400px");
            }
            add(pageTitle, namaProdukField, descProdukField,
                    merekProdukComboBox, imageTitle,
                    imageProduk, uploadFotoContainer,
                    submitDataButton, deleteDataButton, progressBar);
            showForm();
        } else {
            Notification.show("Gagal mengambil detail produk");
        }
        progressBar.setVisible(false);
    }

    public void deleteData(String idProduk) {
        progressBar.setVisible(true);
        Boolean hasilDelete = produkRepository.deleteProduk(idProduk);
        if (hasilDelete) {
            Notification.show("Berhasil menghapus produk");
            UI.getCurrent().navigate("produk");
        } else {
            Notification.show("Gagal menghapus produk");
        }
        progressBar.setVisible(false);
    }

    public void submitData(String namaProduk, String desc, String idMerek,
                           String filename, byte[] produkImage, String idProduk) {
        progressBar.setVisible(true);
        produk = produkRepository.updateProduk(namaProduk, desc, idMerek,
                filename, produkImage, idProduk);
        progressBar.setVisible(false);
        if (produk != null) {
            Notification.show("Berhasil mengupdate produk");
            UI.getCurrent().navigate("produk");
        } else {
            Notification.show("Gagal mengupdate produk");
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
    public void hideForm() {
        namaProdukField.setVisible(false);
        descProdukField.setVisible(false);
        submitDataButton.setVisible(false);
        deleteDataButton.setVisible(false);
        uploadFotoContainer.setVisible(false);
        merekProdukComboBox.setVisible(false);
    }
    public void showForm() {
        deleteDataButton.setVisible(true);
        namaProdukField.setVisible(true);
        descProdukField.setVisible(true);
        submitDataButton.setVisible(true);
        uploadFotoContainer.setVisible(true);
        merekProdukComboBox.setVisible(true);
    }
}
