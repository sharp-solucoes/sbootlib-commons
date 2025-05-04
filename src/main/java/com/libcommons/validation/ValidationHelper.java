package com.libcommons.validation;

import java.math.BigDecimal;

public class ValidationHelper {
    public static void validatePagination(int page, int pageSize) {
        if(page < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Página e tamanho de página devem ser números positivos");
        }
    }

    public static boolean bigDecimalsEquals(BigDecimal value1, BigDecimal value2) {
        if (value1 == null || value2 == null) {
            return value1 == value2;
        }
        return value1.stripTrailingZeros().compareTo(value2.stripTrailingZeros()) == 0;
    }
}
