package com.jamersom.jotabank.accounts.dto;

public record WithdrawResponse(
        Long transactionId,
        String accountNumber,
        String newBalance,
        String currency,
        String createdAt
) {}
