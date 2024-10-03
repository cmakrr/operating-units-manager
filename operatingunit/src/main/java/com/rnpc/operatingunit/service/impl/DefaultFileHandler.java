package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.service.FileHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultFileHandler implements FileHandler {
    private static final String DOT = ".";

    public boolean isFileExtension(String file, String fileExtension) {
        Optional<String> currentFileExtension = getFileExtension(file);

        if (currentFileExtension.isPresent() && StringUtils.isNotBlank(fileExtension)) {
            return fileExtension.equalsIgnoreCase(currentFileExtension.get());
        }

        return false;
    }

    public Optional<String> getFileExtension(String file) {
        if (StringUtils.isNotBlank(file)) {
            int index = file.lastIndexOf(DOT);

            if (index >= 0 && index != file.length() - 1) {
                return Optional.of(file.substring(index + 1).trim().toLowerCase());
            }
        }

        return Optional.empty();
    }

}
