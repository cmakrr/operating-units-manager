package com.rnpc.operatingunit.service.impl;

import com.rnpc.operatingunit.service.PersonService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.StringJoiner;

@Service
public class DefaultPersonService implements PersonService {
    private static final String DOT = ".";
    private static final String RUSSIAN_ALPHABET_REGEX = "[^а-яА-Я]+";
    private static final String WHITESPACE = " ";

    public String getFullName(String fullNameValue) {
        StringJoiner fullName = new StringJoiner(WHITESPACE);
        String[] splitFullName = fullNameValue.split(RUSSIAN_ALPHABET_REGEX);

        for (String namePart : splitFullName) {
            if (namePart.length() > 1) {
                fullName.add(namePart);
            } else {
                fullName.add(namePart + DOT);
            }
        }

        return fullName.toString().trim();
    }

    public boolean isFullName(String name) {
        return StringUtils.isNotBlank(name) && name.contains(DOT);
    }
}
