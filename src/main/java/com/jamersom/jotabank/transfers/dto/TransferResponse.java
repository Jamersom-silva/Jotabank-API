package com.jamersom.jotabank.transfers.dto;

public record TransferResponse(
        Long transactionId,
        String fromAccountNumber,
        String toAccountNumber,
        String amount,
        String currency,
        String createdAt
) {}
