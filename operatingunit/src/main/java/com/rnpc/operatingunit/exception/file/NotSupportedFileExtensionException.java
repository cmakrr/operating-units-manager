package com.rnpc.operatingunit.exception.file;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NotSupportedFileExtensionException extends RuntimeException {
    private String notSupportedExtension;
    private String supportedExtension;
    private List<String> supportedExtensions;

    public NotSupportedFileExtensionException(String notSupportedExtension) {
        super();

        this.notSupportedExtension = notSupportedExtension;
    }

    public NotSupportedFileExtensionException(List<String> supportedExtensions) {
        super();

        this.supportedExtensions = supportedExtensions;
    }

    public NotSupportedFileExtensionException(String notSupportedExtension, String supportedExtension) {
        super();

        this.notSupportedExtension = notSupportedExtension;
        this.supportedExtension = supportedExtension;
    }

    public NotSupportedFileExtensionException(String notSupportedExtension, List<String> supportedExtensions) {
        super();

        this.notSupportedExtension = notSupportedExtension;
        this.supportedExtensions = supportedExtensions;
    }

}
