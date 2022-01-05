package com.blue.media.api.model;

/**
 * media valid result
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class FileValidResult {

    private final Boolean valid;

    private final String name;

    private final String message;

    public FileValidResult(Boolean valid, String name, String message) {
        this.valid = valid;
        this.name = name;
        this.message = message;
    }

    public Boolean getValid() {
        return valid;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "FileValidResultDTO{" +
                "valid=" + valid +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
