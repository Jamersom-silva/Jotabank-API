package com.jamersom.jotabank.accounts.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawRequest(
        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "amount must be greater than zero")
        @Digits(integer = 17, fraction = 2, message = "amount must have up to 2 decimal places")
        BigDecimal amount,

        String description
) {}
