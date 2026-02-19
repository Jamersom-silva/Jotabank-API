package com.jamersom.jotabank.accounts;

import com.jamersom.jotabank.accounts.dto.AccountMeResponse;
import com.jamersom.jotabank.users.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.RoundingMode;

@Service
public class AccountQueryService {

    private final UserRepository users;

    public AccountQueryService(UserRepository users) {
        this.users = users;
    }

    @Transactional(readOnly = true)
    public AccountMeResponse getMyAccount(String email) {
        var user = users.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var account = user.getAccount();
        if (account == null) {
            throw new IllegalStateException("User has no account");
        }

        String balance = account.getBalance()
                .setScale(2, RoundingMode.UNNECESSARY)
                .toPlainString();

        return AccountMeResponse.of(account.getAccountNumber(), balance, account.getCurrency());
    }
}
