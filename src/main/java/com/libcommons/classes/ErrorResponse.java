package com.libcommons.classes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String error;

    private String details;
}
