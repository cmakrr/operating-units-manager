package com.rnpc.operatingunit.parser;

import com.rnpc.operatingunit.exception.plan.MissingPlanColumnsException;
import com.rnpc.operatingunit.model.Operation;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.time.LocalDate;
import java.util.List;

public interface OperationPlanTableParser {
    List<Operation> parse(XWPFTable table, LocalDate operationDate) throws MissingPlanColumnsException;
}
