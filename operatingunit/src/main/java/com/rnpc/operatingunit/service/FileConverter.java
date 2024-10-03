package com.rnpc.operatingunit.service;

import com.spire.doc.FileFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;

public interface FileConverter {
    OutputStream convertToDocx(MultipartFile sourceFilePath, FileFormat sourceFileFormat);

    InputStream convertToInputStream(OutputStream outputStream);
}
