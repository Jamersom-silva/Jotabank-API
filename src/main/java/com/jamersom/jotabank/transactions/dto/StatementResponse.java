package com.jamersom.jotabank.transactions.dto;

import java.util.List;

public record StatementResponse(
        int page,
        int size,
        long totalElements,
        int totalPages,
        List<TransactionItemResponse> items
) {}
