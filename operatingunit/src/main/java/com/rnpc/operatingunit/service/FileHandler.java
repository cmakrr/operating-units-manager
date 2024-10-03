package com.rnpc.operatingunit.service;

import java.util.Optional;

public interface FileHandler {
    boolean isFileExtension(String file, String fileExtension);

    Optional<String> getFileExtension(String file);
}
