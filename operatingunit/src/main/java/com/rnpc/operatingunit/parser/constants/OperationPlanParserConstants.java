package com.rnpc.operatingunit.parser.constants;

import java.time.format.DateTimeFormatter;

public final class OperationPlanParserConstants {
    public static final class Regexp {
        public static final String DATE_REGEX = "\\b(\\d{1,2}\\.\\d{1,2}\\.\\d{4})\\b";
        public static final String HYPHEN_ONE_OR_MORE_REGEX = "-+";
        public static final String TIME_INTERVAL_REGEX = "[^-\\d\\s]+";
        public static final String SPACE_CHARACTER_REGEX = "\\s+";
        public static final String DIGIT_REGEX = "\\d+";
    }

    public static final class Formatter {
        public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("Hmm");
        public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d.M.yyyy");
    }

    public static final class FileType {
        public static final String DOCX_FILE_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        public static final String DOC_FILE_TYPE = "application/msword";
    }

    public static final class Symbols {
        public static final String DOT = ".";
        public static final String HYPHEN = "-";
        public static final String WHITESPACE = " ";
    }

    public static final class DocumentIdentifiers {
        public static final String FINAL_OPERATION_PLAN = "итоговый план операций на";
    }
}
