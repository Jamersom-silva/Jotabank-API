package com.jamersom.jotabank.transfers;

import com.jamersom.jotabank.accounts.Account;
import com.jamersom.jotabank.accounts.AccountRepository;
import com.jamersom.jotabank.transfers.dto.TransferRequest;
import com.jamersom.jotabank.transfers.dto.TransferResponse;
import com.jamersom.jotabank.transactions.Transaction;
import com.jamersom.jotabank.transactions.TransactionRepository;
import com.jamersom.jotabank.users.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TransferService {

    private final UserRepository users;
    private final AccountRepository accounts;
    private final TransactionRepository transactions;

    public TransferService(UserRepository users, AccountRepository accounts, TransactionRepository transactions) {
        this.users = users;
        this.accounts = accounts;
        this.transactions = transactions;
    }

    @Transactional
    public TransferResponse transfer(String email, TransferRequest req) {
        var user = users.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Account from = user.getAccount();
        if (from == null) throw new IllegalStateException("User has no account");

        Account to = accounts.findByAccountNumber(req.toAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found"));

        if (from.getId().equals(to.getId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        BigDecimal amount = req.amount().setScale(2, RoundingMode.UNNECESSARY);
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch");
        }

        // atualiza saldos (debit já valida saldo suficiente)
        from.debit(amount);
        to.credit(amount);

        // opcional dentro de @Transactional, mas pode manter
        accounts.save(from);
        accounts.save(to);

        String description = (req.description() == null || req.description().isBlank())
                ? "Transfer"
                : req.description().trim();

        Transaction tx = Transaction.transfer(from, to, amount, description);
        tx = transactions.save(tx);

        return new TransferResponse(
                tx.getId(),
                from.getAccountNumber(),
                to.getAccountNumber(),
                amount.toPlainString(),
                from.getCurrency(),
                tx.getCreatedAt().toString()
        );
    }
}
