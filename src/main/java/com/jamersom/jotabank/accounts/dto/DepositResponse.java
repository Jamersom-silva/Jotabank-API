package com.jamersom.jotabank.accounts.dto;

public record DepositResponse(
        Long transactionId,
        String accountNumber,
        String newBalance,
        String currency,
        String createdAt
) {}
