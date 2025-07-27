package com.tracker.dss.util;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
@Component
public class Weight {

    public @DecimalMin(value = "0.000", message = "Weight cannot be negative") @Digits(integer = 10, fraction = 3, message = "Weight must have maximum 3 decimal places") BigDecimal setWeight(Double weight) {
        if (weight != null) {
            return BigDecimal.valueOf(weight).setScale(3, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }
}
