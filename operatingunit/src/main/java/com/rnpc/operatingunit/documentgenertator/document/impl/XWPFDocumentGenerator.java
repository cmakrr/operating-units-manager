package com.rnpc.operatingunit.documentgenertator.document.impl;

import com.rnpc.operatingunit.documentgenertator.document.DocumentGenerator;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;


@Getter
@Setter
@Service
public class XWPFDocumentGenerator implements DocumentGenerator {
    public int fontSize = 14;
    public String fontStyle = "Times New Roman";

    public XWPFDocument createA4FormatDocument() {
        XWPFDocument document = new XWPFDocument();

        CTBody body = document.getDocument().getBody();
        if (!body.isSetSectPr()) {
            body.addNewSectPr();
        }

        CTSectPr section = body.getSectPr();
        if (!section.isSetPgSz()) {
            section.addNewPgSz();
        }

        CTPageSz pageSize = section.getPgSz();
        pageSize.setOrient(STPageOrientation.LANDSCAPE);
        pageSize.setW(BigInteger.valueOf(16840));
        pageSize.setH(BigInteger.valueOf(11900));

        return document;
    }

    public XWPFTableRow mergeCellsHorizontally(XWPFTable table, int rowNum, int fromCell, int toCell) {
        XWPFTableRow row = table.getRow(rowNum);
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
            XWPFTableCell cell = row.getCell(cellIndex);
            CTHMerge hMerge = cell.getCTTc().addNewTcPr().addNewHMerge();
            if (cellIndex == fromCell) {
                hMerge.setVal(STMerge.RESTART);
            } else {
                hMerge.setVal(STMerge.CONTINUE);
            }
        }

        return row;
    }

    public void addPageBreak(XWPFDocument document) {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.addBreak(BreakType.PAGE);
    }

    public void setParagraphsSpacingAfterToZero(List<XWPFParagraph> paragraphs) {
        setParagraphsSpacingAfter(paragraphs, 0);
    }

    public void setParagraphsSpacingAfter(List<XWPFParagraph> paragraphs, int space) {
        paragraphs.forEach(paragraph -> paragraph.setSpacingAfter(space));
    }

    public void populateTableTitle(XWPFTable table, String[] titles) {
        populateTableTitleFromColumn(table, titles, 0);
    }

    public void populateTableTitleFromColumn(XWPFTable table, String[] titles, int columnIndex) {
        AtomicInteger columnIndexAtomic = new AtomicInteger(columnIndex);
        Arrays.stream(titles).forEach(title -> {
            int currentColumnIndex = columnIndexAtomic.getAndIncrement();
            setBoldParagraph(table.getRow(0).getCell(currentColumnIndex).getParagraphs().get(0), title);
            table.getRow(0).getCell(currentColumnIndex).getParagraphs().get(0).setSpacingAfter(0);
        });

    }

    public void populateLeftTableTitle(XWPFTable table, String[] titles) {
        populateLeftTableTitleFromRow(table, titles, 0);
    }

    public void populateLeftTableTitleFromRow(XWPFTable table, String[] titles, int rowIndex) {
        AtomicInteger rowIndexAtomic = new AtomicInteger(rowIndex);
        Arrays.stream(titles).forEach(title -> {
            int currentRowIndex = rowIndexAtomic.getAndIncrement();
            setBoldParagraph(table.getRow(currentRowIndex).getCell(0).getParagraphs().get(0), title);
            table.getRow(currentRowIndex).getCell(0).getParagraphs().get(0).setSpacingAfter(0);
        });
    }

    public void setColumnWidth(XWPFTable table, int columnIndex, int width) {
        table.getRows().forEach(row -> {
            XWPFTableCell cell = row.getCell(columnIndex);
            CTTc ctTc = cell.getCTTc();
            CTTcPr tcPr = ctTc.getTcPr();
            if (tcPr == null) {
                tcPr = ctTc.addNewTcPr();
            }
            CTTblWidth tblWidth = tcPr.isSetTcW() ? tcPr.getTcW() : tcPr.addNewTcW();
            tblWidth.setW(BigInteger.valueOf(width));
            tblWidth.setType(STTblWidth.DXA);
        });
    }

    public void setColumnsContentToCenter(XWPFTable table, int startRow, int startColumn) {
        IntStream.range(startRow, table.getRows().size()).forEach(i -> IntStream.range(startColumn, table.getRow(i).getTableCells().size()).forEach(j -> setColumnContentToCenter(table.getRow(i).getCell(j))));
    }

    public void setColumnContentToCenter(XWPFTableCell cell) {
        cell.getParagraphs().forEach(paragraph -> {
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            paragraph.setSpacingAfter(0);
        });
    }

    public void setBoldParagraph(XWPFParagraph paragraph, String text) {
        paragraph.setSpacingAfter(0);

        XWPFRun run = paragraph.createRun();

        run.setBold(true);
        setRun(run, text);
    }

    public void setParagraph(XWPFParagraph paragraph, String text) {
        paragraph.setSpacingAfter(0);

        XWPFRun run = paragraph.createRun();
        setRun(run, text);
    }

    public void setRun(XWPFRun run, String text) {
        if (StringUtils.isNotBlank(text)) {
            run.setFontFamily(fontStyle);
            run.setFontSize(fontSize);
            run.setText(text);

            run.removeBreak();
            run.removeTab();
            run.removeCarriageReturn();
        }
    }

}
