package com.example.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class is based on a sample found here:
 *   https://thepracticaldeveloper.com/custom-error-handling-rest-controllers-spring-boot/
 *
 * @author User
 *
 */
public class AppError {

    private final String apiVersion;
    private final ErrorBlock error;

    public AppError(final String apiVersion, final String code, final String message, final String domain,
                             final String reason, final String errorMessage, final String errorReportUri) {
        this.apiVersion = apiVersion;
        this.error = new ErrorBlock(code, message, domain, reason, errorMessage, errorReportUri);
    }

    public static AppError fromDefaultAttributeMap(final String apiVersion,
                                                            final Map<String, Object> defaultErrorAttributes,
                                                            final String sendReportBaseUri) {
        return new AppError(
                apiVersion,
                ((Integer) defaultErrorAttributes.get("status")).toString(),
                (String) defaultErrorAttributes.getOrDefault("message", "no message available"),
                (String) defaultErrorAttributes.getOrDefault("path", "no domain available"),
                (String) defaultErrorAttributes.getOrDefault("error", "no reason available"),
                (String) defaultErrorAttributes.get("message"),
                sendReportBaseUri
        );
    }

    public Map<String, Object> toAttributeMap() {
        return Map.of(
          "apiVersion", apiVersion,
          "error", error
        );
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public ErrorBlock getError() {
        return error;
    }

    private static final class ErrorBlock {

        @JsonIgnore
        private final UUID uniqueId;
        private final String code;
        private final String message;
        private final List<Error> errors;

        public ErrorBlock(final String code, final String message, final String domain,
                          final String reason, final String errorMessage, final String errorReportUri) {
            this.code = code;
            this.message = message;
            this.uniqueId = UUID.randomUUID();
            this.errors = List.of(
                    new Error(domain, reason, errorMessage, errorReportUri + "?id=" + uniqueId)
            );
        }

        @SuppressWarnings("unused")
		public UUID getUniqueId() {
            return uniqueId;
        }

        @SuppressWarnings("unused")
		public String getCode() {
            return code;
        }

        @SuppressWarnings("unused")
		public String getMessage() {
            return message;
        }

        @SuppressWarnings("unused")
		public List<Error> getErrors() {
            return errors;
        }
    }

    private static final class Error {
        private final String domain;
        private final String reason;
        private final String message;
        private final String sendReport;

        public Error(final String domain, final String reason, final String message, final String sendReport) {
            this.domain = domain;
            this.reason = reason;
            this.message = message;
            this.sendReport = sendReport;
        }

        @SuppressWarnings("unused")
        public String getDomain() {
            return domain;
        }

        @SuppressWarnings("unused")
        public String getReason() {
            return reason;
        }

        @SuppressWarnings("unused")
        public String getMessage() {
            return message;
        }

        @SuppressWarnings("unused")
        public String getSendReport() {
            return sendReport;
        }
    }
}