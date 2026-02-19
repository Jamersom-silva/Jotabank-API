package com.jamersom.jotabank.accounts.dto;

public record AccountMeResponse(
        String accountNumber,
        String balance,
        String currency
) {
    public static AccountMeResponse of(String accountNumber, String balance, String currency) {
        return new AccountMeResponse(accountNumber, balance, currency);
    }
}
