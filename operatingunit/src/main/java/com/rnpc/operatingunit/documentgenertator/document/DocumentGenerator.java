package com.rnpc.operatingunit.documentgenertator.document;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.List;

public interface DocumentGenerator {
    XWPFDocument createA4FormatDocument();

    void addPageBreak(XWPFDocument document);

    XWPFTableRow mergeCellsHorizontally(XWPFTable table, int rowNum, int fromCell, int toCell);

    void setParagraphsSpacingAfterToZero(List<XWPFParagraph> paragraphs);

    void setParagraphsSpacingAfter(List<XWPFParagraph> paragraphs, int space);

    void setFontStyle(String fontStyle);

    void setFontSize(int fontSize);

    void populateTableTitle(XWPFTable table, String[] titles);

    void populateTableTitleFromColumn(XWPFTable table, String[] titles, int columnIndex);

    void populateLeftTableTitle(XWPFTable table, String[] titles);

    void populateLeftTableTitleFromRow(XWPFTable table, String[] titles, int rowIndex);

    void setColumnWidth(XWPFTable table, int columnIndex, int width);

    void setColumnsContentToCenter(XWPFTable table, int startRow, int startColumn);

    void setColumnContentToCenter(XWPFTableCell cell);

    void setBoldParagraph(XWPFParagraph paragraph, String text);

    void setParagraph(XWPFParagraph paragraph, String text);

    void setRun(XWPFRun run, String text);

}
