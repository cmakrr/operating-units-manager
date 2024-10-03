package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.exception.file.InvalidExtensionException;
import com.rnpc.operatingunit.service.FileConverter;
import com.rnpc.operatingunit.service.FileHandler;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
@RequiredArgsConstructor
public class DefaultFileConverter implements FileConverter {
    private final FileHandler fileHandler;

    @Override
    public OutputStream convertToDocx(MultipartFile file, FileFormat sourceFileFormat) {
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document doc = new Document(file.getInputStream(), sourceFileFormat);
            doc.saveToStream(outputStream, FileFormat.Docx);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outputStream;
    }

    @Override
    public InputStream convertToInputStream(OutputStream outputStream) {
        return new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());
    }

    private boolean isSupportedFileExtension(String filePath, String extension, String ErrorMessage) {
        if (!fileHandler.isFileExtension(filePath, extension)) {
            String message = String.format(ErrorMessage, extension);
            throw new InvalidExtensionException(message);
        }

        return true;
    }

}
