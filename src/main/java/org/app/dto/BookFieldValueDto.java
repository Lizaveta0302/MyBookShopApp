package org.app.dto;

import javax.validation.constraints.NotBlank;

public class BookFieldValueDto {
    @NotBlank
    String inputBookFieldValue;

    public String getInputBookFieldValue() {
        return inputBookFieldValue;
    }

    public void setInputBookFieldValue(String inputBookFieldValue) {
        this.inputBookFieldValue = inputBookFieldValue;
    }
}
