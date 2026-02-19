package com.jamersom.jotabank.accounts;

import com.jamersom.jotabank.accounts.dto.DepositRequest;
import com.jamersom.jotabank.accounts.dto.DepositResponse;
import com.jamersom.jotabank.transactions.Transaction;
import com.jamersom.jotabank.transactions.TransactionRepository;
import com.jamersom.jotabank.users.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jamersom.jotabank.accounts.dto.WithdrawRequest;
import com.jamersom.jotabank.accounts.dto.WithdrawResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class AccountOperationsService {

    private final UserRepository users;
    private final AccountRepository accounts;
    private final TransactionRepository transactions;

    public AccountOperationsService(
            UserRepository users,
            AccountRepository accounts,
            TransactionRepository transactions
    ) {
        this.users = users;
        this.accounts = accounts;
        this.transactions = transactions;
    }

    @Transactional
    public DepositResponse deposit(String email, DepositRequest req) {
        var user = users.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = user.getAccount();
        if (account == null) throw new IllegalStateException("User has no account");

        BigDecimal amount = req.amount().setScale(2, RoundingMode.UNNECESSARY);
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");

        account.credit(amount);

        accounts.save(account);

        String description = (req.description() == null || req.description().isBlank())
                ? "Deposit"
                : req.description().trim();

        Transaction tx = Transaction.deposit(account, amount, description);
        tx = transactions.save(tx);

        return new DepositResponse(
                tx.getId(),
                account.getAccountNumber(),
                account.getBalance().toPlainString(),
                account.getCurrency(),
                tx.getCreatedAt().toString()
        );

    }
    @Transactional
    public WithdrawResponse withdraw(String email, WithdrawRequest req) {
        var user = users.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account account = user.getAccount();
        if (account == null) throw new IllegalStateException("User has no account");

        BigDecimal amount = req.amount().setScale(2, RoundingMode.UNNECESSARY);
        if (amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");

        // valida saldo + debita
        account.debit(amount);

        accounts.save(account);

        String description = (req.description() == null || req.description().isBlank())
                ? "Withdraw"
                : req.description().trim();

        Transaction tx = Transaction.withdraw(account, amount, description);
        tx = transactions.save(tx);

        return new WithdrawResponse(
                tx.getId(),
                account.getAccountNumber(),
                account.getBalance().toPlainString(),
                account.getCurrency(),
                tx.getCreatedAt().toString()
        );
    }

}
