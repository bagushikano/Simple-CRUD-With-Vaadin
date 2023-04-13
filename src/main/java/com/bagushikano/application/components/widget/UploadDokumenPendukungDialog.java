package com.bagushikano.application.components.widget;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.InputStream;

public class UploadDokumenPendukungDialog extends Dialog {
    Button openDialog;
    TextField judulBerkasField = new TextField("Judul Berkas");
    FormLayout uploadBerkasFormLayout = new FormLayout();
    HorizontalLayout buttonDialogContainer = new HorizontalLayout();
    Button saveDokumen = new Button("Simpan Dokumen");
    Button cancelUpload = new Button("Batal");
    private MemoryBuffer memoryBuffer = new MemoryBuffer();
    private VerticalLayout uploadBerkasPendukungContainer = new VerticalLayout();
    private Upload uploadBerkasPendukung = new Upload(memoryBuffer);
    private H6 uploadBerkasPendukungTitle = new H6("Dokumen Pendukung");
    private Paragraph uploadBerkasPendukungHint = new Paragraph("Format file: PNG, JPG, JPEG (.png, .jpg, .jpeg). Ukuran Maks 10MB");
    private byte[] berkasPendukungFile;
    private String berkasPendukungFilename;
    private Boolean isFileUploaded = false;
    private Boolean isFileSelected = false;
    InputStream fileData;
    String fileName;
    long contentLength;
    String mimeType;
    public UploadDokumenPendukungDialog() {
        setMaxWidth(500, Unit.PIXELS);

        //TODO Tambahin tombol preview dokumen waktu selesai
        // di upload di setiap form dokumen pendukung

        uploadBerkasFormLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0",2));
        uploadBerkasFormLayout.add(judulBerkasField);
        uploadBerkasFormLayout.setColspan(judulBerkasField, 2);

        uploadBerkasPendukungTitle.getStyle().set("margin-top", "0");
        uploadBerkasPendukungHint.getStyle().set("color", "var(--lumo-secondary-text-color)");
        int maxFileSizeInBytes = 10 * 1024 * 1024; // set max upload ke 10mb
        uploadBerkasPendukung.setMaxFileSize(maxFileSizeInBytes);
        uploadBerkasPendukung.setWidthFull();
        uploadBerkasPendukung.setAcceptedFileTypes("image/png", "image/jpg", "image/jpeg", "application/pdf");
        uploadBerkasPendukungContainer.add(uploadBerkasPendukungTitle, uploadBerkasPendukungHint, uploadBerkasPendukung);
        uploadBerkasPendukungContainer.setMargin(false);
        uploadBerkasPendukungContainer.setPadding(false);
        uploadBerkasPendukungContainer.setSpacing(false);
        uploadBerkasPendukungContainer.addClassNames(LumoUtility.Margin.Top.MEDIUM);
        uploadBerkasPendukungContainer.setWidthFull();

        uploadBerkasFormLayout.add(uploadBerkasPendukungContainer);
        uploadBerkasFormLayout.setColspan(uploadBerkasPendukungContainer, 2);
        uploadBerkasFormLayout.setWidthFull();

        buttonDialogContainer.add(cancelUpload, saveDokumen);
        buttonDialogContainer.setWidthFull();
        buttonDialogContainer.addClassName(LumoUtility.JustifyContent.END);
        buttonDialogContainer.addClassName(LumoUtility.Margin.Top.MEDIUM);

        cancelUpload.addClickListener(buttonClickEvent -> {
            close();
        });

        addOpenedChangeListener(openedChangeEvent -> {
            if (isOpened()) {{
                isFileUploaded = false;
                isFileSelected = false;
                saveDokumen.setEnabled(false);
                saveDokumen.setTooltipText("Lengkapi data berkas terlebih dahulu");
            }}
        });

        setModal(true);

        getElement().setAttribute("aria-label", "Upload Dokumen Pendukung");
        setHeaderTitle("Unggah Dokumen Pendukung");

        add(uploadBerkasFormLayout, buttonDialogContainer);

        saveDokumen.addClickListener(buttonClickEvent -> {
            if (isFileSelected) {
                prepareFileToPost(fileData, fileName);
            }
        });

        uploadBerkasPendukung.addSucceededListener(event -> {
            // Get information about the uploaded file
            fileData = memoryBuffer.getInputStream();
            fileName = event.getFileName();
            contentLength = event.getContentLength();
            mimeType = event.getMIMEType();
            isFileSelected = true;
            saveDokumen.setTooltipText("Upload dokumen");
            saveDokumen.setEnabled(true);
        });
    }

    //preapre file nya di memory untuk nanti di upload ke server
    public void prepareFileToPost (InputStream file, String fileName) {
        /*
        produkFileName = fileName;
        fileReady = false;
        try {
            fotoProdukFile = file.readAllBytes();
            fileReady = true;
        } catch (IOException e) {
            fileReady = false;
            throw new RuntimeException(e);
        }
        */
        isFileUploaded = true;
        berkasPendukungFilename = fileName;
        close();
    }

    public Boolean getFileStatus() {
        return isFileUploaded;
    }

    public String getFileName() {
        return berkasPendukungFilename;
    }

    //TODO implement untuk post datanya
    public void submitData(String namaProduk, String desc, String idMerek,
                           String filename, byte[] produkImage) {
        /*
        progressBar.setVisible(true);
        produk = produkRepository.createProduk(namaProduk, desc, idMerek, filename, produkImage);
        progressBar.setVisible(false);
        if (produk != null) {
            Notification.show("Berhasil membuat produk");
            UI.getCurrent().navigate("produk");
        } else {
            Notification.show("Gagal membuat produk");
        }
        */
    }

    public void editDokumen() {
        //TODO implement edit dokumennya,
        // fundamentalnya masih sama kaya create, tapi formnya nanti udah isi filenya
        // sama parameter lainnya, nandi dia nge post file + id dokumen yg di upload
    }
}
