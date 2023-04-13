package com.bagushikano.application.components.widget;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class CustomPagination extends HorizontalLayout {
    String totalDataFormat = "Menampilkan %s s/d %s dari %s data";
    Paragraph totalDataText = new Paragraph("");
    TextField currentPage = new TextField();
    TextField goToField = new TextField();
    TextField pageSizeField = new TextField();
    Button prevButton;
    Button nextButton;
    Button goToButton;

    /**
     * Untuk generate component pagination, prev, next dan goTo itu button yang di bikin di view nya, baru di add kesini
     * tujuannya untuk bisa nge set aksi untuk get datanya sesuai viewnya
     * @param prevButton Button
     * @param nextButton Button
     * @param goToButton Button
     * @param totalPage Integer
     * @param pageSize Integer
     * @param startingData Integer, Ini posisi data awalnya
     * @param totalData Integer
     */
    public CustomPagination(Button prevButton, Button nextButton, Button goToButton,
                            Integer totalPage, Integer pageSize, Integer startingData, Integer totalData) {
        this.prevButton = prevButton;
        this.nextButton = nextButton;
        this.goToButton = goToButton;

        currentPage.setReadOnly(true);
        currentPage.setValue("1");
        totalDataText.setText(formatTotalPage(totalData, startingData+pageSize, startingData));
        goToField.setPlaceholder("Hal..");
        goToField.setWidth("5%");
        currentPage.setWidth("5%");
        currentPage.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        totalDataText.addClassName(LumoUtility.AlignSelf.CENTER);

        /**
         * Validasi untuk goto nya, jadi semisal user nge input go to page yang
         * lebih besar dari page total nya, fiedlnya akan otomatis di set ke
         * page terakhir
         */
        goToField.addValueChangeListener(event -> {
            if (Integer.parseInt(event.getValue()) > totalPage) {
                goToField.setValue(String.valueOf(totalPage));
            }
        });

        pageSizeField.

        setWidthFull();
        addClassName(LumoUtility.JustifyContent.BETWEEN);
        setPadding(false);
        add(totalDataText, createSpacer(), this.prevButton, currentPage, this.nextButton, createSpacer(), goToField, this.goToButton);
    }
    public String getGotoValue() {
        return goToField.getValue();
    }
    public void setCurrentPage(String pageNumber) {
        currentPage.setValue(pageNumber);
    }
    public void updateTotalDataText(Integer totalData, Integer to, Integer from) {
        totalDataText.setText(formatTotalPage(totalData, from, to));
    }
    public String formatTotalPage(Integer totalData, Integer to, Integer from) {
        return String.format(
                totalDataFormat,
                from,
                to,
                totalData
        );
    }
    public Div createSpacer() {
        Div spacer = new Div();
        spacer.addClassNames(LumoUtility.Flex.GROW);
        spacer.addClassNames(LumoUtility.FlexDirection.ROW);
        return spacer;
    }
}
