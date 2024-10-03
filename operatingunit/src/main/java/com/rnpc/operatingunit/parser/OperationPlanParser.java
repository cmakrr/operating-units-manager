package com.rnpc.operatingunit.parser;

import com.rnpc.operatingunit.model.Operation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface OperationPlanParser {
    List<Operation> parse(MultipartFile planFile);
}