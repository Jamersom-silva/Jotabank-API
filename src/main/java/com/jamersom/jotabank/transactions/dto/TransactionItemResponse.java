package com.jamersom.jotabank.transactions.dto;

public record TransactionItemResponse(
        Long id,
        String type,
        String direction,
        String amount,
        String currency,
        String description,
        String createdAt
) {}
