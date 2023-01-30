package com.blue.excel.common;

import com.blue.excel.api.conf.WorkBookElementConf;
import com.blue.excel.api.conf.WorkBookStyle;
import com.blue.excel.api.conf.WorkBookStyleConf;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.BiConsumer;

import static com.blue.basic.common.base.BlueChecker.*;
import static java.lang.String.valueOf;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * workbook processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"Duplicates", "JavaDoc", "WeakerAccess", "unused", "AliControlFlowStatementWithoutBraces"})
public final class WorkBookProcessor<T> {

    private static final Logger LOGGER = getLogger(WorkBookProcessor.class);

    /**
     * cell width
     */
    private final int columnWidth;

    /**
     * cell height
     */
    private final short columnHeight;

    /**
     * font size
     */
    private final short fontSize;

    /**
     * font name
     */
    private final String fontName;

    /**
     * headers
     */
    private final String[] headers;

    /**
     * elements in data list to excel row converter
     */
    private final BiConsumer<XSSFRow, T> rowElementPackager;

    /**
     * header size
     */
    private final int headerSize;

    public WorkBookProcessor(WorkBookElementConf<T> workBookElementConf) {
        this(workBookElementConf, new WorkBookStyle());
    }

    public WorkBookProcessor(WorkBookElementConf<T> workBookElementConf, WorkBookStyleConf workBookStyleConf) {
        assertParam(workBookElementConf, workBookStyleConf);

        this.headers = workBookElementConf.getHeaders();
        this.headerSize = this.headers.length;
        this.rowElementPackager = workBookElementConf.getRowElementPackager();

        this.columnWidth = workBookStyleConf.getColumnWidth();
        this.columnHeight = workBookStyleConf.getColumnHeight();
        this.fontSize = workBookStyleConf.getFontSize();
        this.fontName = workBookStyleConf.getFontName();
    }

    /**
     * generate workbook
     *
     * @return
     */
    @SuppressWarnings("UnusedAssignment")
    public XSSFWorkbook generate(List<List<T>> sheetElements) {
        LOGGER.info("sheetElements = {}", sheetElements);

        elementsAsserter(sheetElements);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFCellStyle style = generateStyle(workbook);

        //page elements
        int sheetIndex = 1;
        int rowIndex;
        XSSFSheet sheet;
        XSSFRow row;
        XSSFCell cell;
        for (List<T> sheetElement : sheetElements) {
            rowIndex = 0;

            sheet = workbook.createSheet(valueOf(sheetIndex++));
            sheet.setDefaultColumnWidth(columnWidth);
            sheet.setDefaultRowHeight(columnHeight);

            //header
            row = sheet.createRow(rowIndex++);
            row.setRowStyle(style);

            for (int i = 0; i < headerSize; i++) {
                cell = row.createCell(i);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(headers[i]);
            }

            //row
            for (T rowElement : sheetElement) {
                row = sheet.createRow(rowIndex++);
                row.setRowStyle(style);
                rowElementPackager.accept(row, rowElement);
            }
        }

        style = null;
        sheet = null;
        row = null;
        cell = null;

        return workbook;
    }

    /**
     * assert processor params
     */
    private void assertParam(WorkBookElementConf<T> workBookElementConf, WorkBookStyleConf workBookStyleConf) {
        if (isNull(workBookElementConf))
            throw new RuntimeException("workBookElement can't be null");

        String[] headers = workBookElementConf.getHeaders();
        if (isNull(headers) || headers.length < 1)
            throw new RuntimeException("headers can't be null");

        BiConsumer<XSSFRow, T> rowElementPackager = workBookElementConf.getRowElementPackager();
        if (isNull(rowElementPackager))
            throw new RuntimeException("row packager can't be null");

        Integer columnWidth = workBookStyleConf.getColumnWidth();
        if (isNull(columnWidth) || columnWidth < 1)
            throw new RuntimeException("columnWidth can't be null or less than 1");

        Short columnHeight = workBookStyleConf.getColumnHeight();
        if (isNull(columnHeight) || columnHeight < 1)
            throw new RuntimeException("columnHeight can't be null or less than 1");

        String fontName = workBookStyleConf.getFontName();
        if (isBlank(fontName))
            throw new RuntimeException("fontName can't be blank");

        Short fontSize = workBookStyleConf.getFontSize();
        if (isNull(fontSize) || fontSize < 1)
            throw new RuntimeException("fontSize can't be null or less than 1");
    }

    /**
     * assert elements
     *
     * @param sheetElements
     */
    private void elementsAsserter(List<List<T>> sheetElements) {
        if (isEmpty(sheetElements))
            throw new RuntimeException("elements can't be empty");

        int sheetCheckIndex = 1;
        for (List<T> sheetElement : sheetElements) {
            if (isEmpty(sheetElement))
                throw new RuntimeException("elements in page " + sheetCheckIndex + "is empty");
            sheetCheckIndex++;
        }
    }

    /**
     * generate cell style
     *
     * @param workbook
     * @return
     */
    private XSSFCellStyle generateStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(false);

        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);

        style.setFont(font);
        return style;
    }

}
