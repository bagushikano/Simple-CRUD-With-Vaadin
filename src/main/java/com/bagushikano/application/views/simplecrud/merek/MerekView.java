package com.bagushikano.application.views.simplecrud.merek;

import com.bagushikano.application.models.merek.MerekProduk;
import com.bagushikano.application.repository.MerekRepository;
import com.bagushikano.application.views.MainLayout;
import com.flowingcode.vaadin.addons.gridexporter.GridExporter;
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
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@PageTitle("Daftar Merek")
@Route(value = "hello", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class MerekView extends VerticalLayout {
    private ProgressBar progressBar = new ProgressBar();
    private Button createDataButton = new Button();
    private final H1 pageTitle = new H1("Daftar Data Merek Produk");

    private ArrayList<MerekProduk> merekList;
    private MerekRepository merekRepository = new MerekRepository();
    Button prevButton = new Button("<");
    Button nextButton = new Button(">");
    Button goToButton = new Button("GoTo");

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    public MerekView() {
        Grid<MerekProduk> grid = new Grid<>();


        Button exportButton = new Button("export");

        FileDownloadWrapper buttonWrapper = new FileDownloadWrapper(
                new StreamResource("export.xlsx", () -> {
                    try {
                        return export();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }));
        buttonWrapper.wrapComponent(exportButton);

        grid.addColumn(MerekProduk::getId).setHeader("ID").setSortable(true);
        grid.addColumn(MerekProduk::getMerek).setHeader("Merek").setSortable(true);
        grid.addColumn(MerekProduk::getKeterangan).setHeader("Keterangan").setSortable(true);
        grid.addColumn(MerekProduk::convertDateTime).setHeader("Date Created");
        grid.setPageSize(1);
        grid.addItemClickListener(new ComponentEventListener<ItemClickEvent<MerekProduk>>() {
            @Override
            public void onComponentEvent(ItemClickEvent<MerekProduk> merekProdukItemClickEvent) {
                UI.getCurrent().navigate(DetailMerekView.class, merekProdukItemClickEvent.getItem().getId().toString());
            }
        });

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

//        GridExporter<MerekProduk> exporter = GridExporter.createFor(grid);
//        exporter.setTitle("People information");
//        exporter.setFileName("GridExport" + new SimpleDateFormat("yyyyddMM").format(Calendar.getInstance().getTime()));

        add(pageTitle, createDataButton, buttonWrapper, grid);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (MerekProduk merekProduk : merekList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, merekProduk.getId(), style);
            createCell(row, columnCount++, merekProduk.getMerek(), style);
            createCell(row, columnCount++, merekProduk.getKeterangan(), style);
            createCell(row, columnCount++, merekProduk.getCreated_at(), style);
        }
    }

    private void writeHeaderLine() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Users");
        Row row = sheet.createRow(0);



        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "ID", style);
        createCell(row, 1, "Merek", style);
        createCell(row, 2, "Keterangan", style);
        createCell(row, 3, "Date Created", style);
    }

    public InputStream export() throws IOException {
        writeHeaderLine();
        writeDataLines();

        final ByteArrayInputStream stream;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        stream = new ByteArrayInputStream(out.toByteArray());
        workbook.close();
        return stream;
    }
}
