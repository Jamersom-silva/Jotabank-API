package com.jamersom.jotabank.transactions;

import com.jamersom.jotabank.transactions.dto.TransactionItemResponse;
import com.jamersom.jotabank.users.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionQueryService {

    private final UserRepository users;
    private final TransactionRepository transactions;

    public TransactionQueryService(UserRepository users, TransactionRepository transactions) {
        this.users = users;
        this.transactions = transactions;
    }

    @Transactional(readOnly = true)
    public List<TransactionItemResponse> myStatement(String email) {

        var user = users.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var account = user.getAccount();
        if (account == null) throw new IllegalStateException("User has no account");

        var pageable = PageRequest.of(0, 50);

        var list = transactions
                .findByFromAccountOrToAccountOrderByCreatedAtDesc(account, account, pageable);

        return list.stream().map(tx -> {

            boolean isOutgoing = tx.getFromAccount() != null &&
                    tx.getFromAccount().getId().equals(account.getId());

            String direction = isOutgoing ? "OUT" : "IN";

            return new TransactionItemResponse(
                    tx.getId(),
                    tx.getType().name(),
                    direction,
                    tx.getAmount().toPlainString(),
                    account.getCurrency(),
                    tx.getDescription(),
                    tx.getCreatedAt().toString()
            );
        }).toList();
    }
}
