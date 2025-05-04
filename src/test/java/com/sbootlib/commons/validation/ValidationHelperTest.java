package com.sbootlib.commons.validation;

import com.libcommons.validation.ValidationHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidationHelperTest {

    @Test
    @DisplayName("Should throw an error when page less or equal to 0")
    public void testPageLessOrEqualZero() {
        RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> ValidationHelper.validatePagination(0,1));
    }

    @Test
    @DisplayName("Should throw an error when pageSize less or equal to 0")
    public void testPageSizeLessOrEqualZero() {
        RuntimeException exception = assertThrows(IllegalArgumentException.class, () -> ValidationHelper.validatePagination(1,0));
    }

    @Test
    @DisplayName("Should throw an error when page & pageSize above 0")
    public void testBothAbove() {
        assertDoesNotThrow(() -> ValidationHelper.validatePagination(5,3));
    }

    @Test
    void testEqualBigDecimalsWithTrailingZeros() {
        BigDecimal num1 = new BigDecimal("100");
        BigDecimal num2 = new BigDecimal("100.00");
        assertTrue(ValidationHelper.bigDecimalsEquals(num1, num2));
    }

    @Test
    void testEqualBigDecimalsWithoutTrailingZeros() {
        BigDecimal num1 = new BigDecimal("100.1");
        BigDecimal num2 = new BigDecimal("100.1");
        assertTrue(ValidationHelper.bigDecimalsEquals(num1, num2));
    }

    @Test
    void testDifferentBigDecimals() {
        BigDecimal num1 = new BigDecimal("100");
        BigDecimal num2 = new BigDecimal("100.01");
        assertFalse(ValidationHelper.bigDecimalsEquals(num1, num2));
    }

    @Test
    void testDifferentBigDecimalsWithTrailingZeros() {
        BigDecimal num1 = new BigDecimal("100.10");
        BigDecimal num2 = new BigDecimal("100.11");
        assertFalse(ValidationHelper.bigDecimalsEquals(num1, num2));
    }

    @Test
    void testNullFirstBigDecimal() {
        BigDecimal num2 = new BigDecimal("100");
        assertFalse(ValidationHelper.bigDecimalsEquals(null, num2));
    }

    @Test
    void testNullSecondBigDecimal() {
        BigDecimal num1 = new BigDecimal("100");
        assertFalse(ValidationHelper.bigDecimalsEquals(num1, null));
    }

    @Test
    void testBothNullBigDecimals() {
        assertTrue(ValidationHelper.bigDecimalsEquals(null, null));
    }

    @Test
    void testEqualZeroes() {
        BigDecimal num1 = new BigDecimal("0.00");
        BigDecimal num2 = new BigDecimal("0");
        assertTrue(ValidationHelper.bigDecimalsEquals(num1, num2));
    }

    @Test
    void testBigDecimalsWithDifferentScalesButSameValue() {
        BigDecimal num1 = new BigDecimal("100.000");
        BigDecimal num2 = new BigDecimal("100");
        assertTrue(ValidationHelper.bigDecimalsEquals(num1, num2));
    }
}
