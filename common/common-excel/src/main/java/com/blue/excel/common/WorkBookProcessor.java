package com.blue.excel.common;

import com.blue.base.model.exps.BlueException;
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

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.String.valueOf;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Excel文件创建器
 *
 * @author DarkBlue
 */
@SuppressWarnings({"Duplicates", "JavaDoc", "WeakerAccess", "unused", "AliControlFlowStatementWithoutBraces"})
public final class WorkBookProcessor<T> {

    private static final Logger LOGGER = getLogger(WorkBookProcessor.class);

    /**
     * 列宽
     */
    private final int columnWidth;

    /**
     * 行高
     */
    private final short columnHeight;

    /**
     * 字体大小
     */
    private final short fontSize;

    /**
     * 字体
     */
    private final String fontName;

    /**
     * 表头
     */
    private final String[] headers;

    /**
     * 实体到行元素的封装器
     */
    private final BiConsumer<XSSFRow, T> rowElementPackager;

    /**
     * 表头长度
     */
    private final int headerLength;

    public WorkBookProcessor(WorkBookElementConf<T> workBookElementConf) {
        this(workBookElementConf, new WorkBookStyle());
    }

    public WorkBookProcessor(WorkBookElementConf<T> workBookElementConf, WorkBookStyleConf workBookStyleConf) {
        assertParam(workBookElementConf, workBookStyleConf);

        this.headers = workBookElementConf.getHeaders();
        this.headerLength = this.headers.length;
        this.rowElementPackager = workBookElementConf.getRowElementPackager();

        this.columnWidth = workBookStyleConf.getColumnWidth();
        this.columnHeight = workBookStyleConf.getColumnHeight();
        this.fontSize = workBookStyleConf.getFontSize();
        this.fontName = workBookStyleConf.getFontName();
    }

    /**
     * 创建excel
     *
     * @return
     */
    @SuppressWarnings("UnusedAssignment")
    public XSSFWorkbook generate(List<List<T>> sheetElements) {
        LOGGER.info("XSSFWorkbook generate(List<List<T>> sheetElements), sheetElements = {}", sheetElements);

        elementsAsserter(sheetElements);

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFCellStyle style = generateStyle(workbook);

        //内容
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

            //表头
            row = sheet.createRow(rowIndex++);
            row.setRowStyle(style);

            for (int i = 0; i < headerLength; i++) {
                cell = row.createCell(i);
                cell.setCellType(CellType.STRING);
                cell.setCellValue(headers[i]);
            }

            //内容行
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
     * 校验构造
     */
    private void assertParam(WorkBookElementConf<T> workBookElementConf, WorkBookStyleConf workBookStyleConf) {
        if (workBookElementConf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "workBookElement不能为空");

        String[] headers = workBookElementConf.getHeaders();
        if (headers == null || headers.length < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "表头元素不能为空");

        BiConsumer<XSSFRow, T> rowElementPackager = workBookElementConf.getRowElementPackager();
        if (rowElementPackager == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "实体到行元素封装器不能为空");

        Integer columnWidth = workBookStyleConf.getColumnWidth();
        if (columnWidth == null || columnWidth < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "columnWidth不能小于1");

        Short columnHeight = workBookStyleConf.getColumnHeight();
        if (columnHeight == null || columnHeight < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "columnHeight不能小于1");

        String fontName = workBookStyleConf.getFontName();
        if (fontName == null || "".equals(fontName))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "fontName不能为空或''");

        Short fontSize = workBookStyleConf.getFontSize();
        if (fontSize == null || fontSize < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "fontSize不能小于1");
    }

    /**
     * 参数校验
     *
     * @param sheetElements
     */
    private void elementsAsserter(List<List<T>> sheetElements) {
        if (sheetElements == null || sheetElements.size() < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "数据集为空");

        int sheetCheckIndex = 1;
        for (List<T> sheetElement : sheetElements) {
            if (sheetElement == null || sheetElement.size() < 1)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "第" + sheetCheckIndex + "页数据集为空");
            sheetCheckIndex++;
        }
    }

    /**
     * 创建样式
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
