package com.jamersom.jotabank.transactions;

import com.jamersom.jotabank.common.errors.UnprocessableEntityException;
import com.jamersom.jotabank.transactions.dto.TransactionItemResponse;
import com.jamersom.jotabank.users.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionQueryService {

    private static final int MAX_SIZE = 100;

    private final UserRepository users;
    private final TransactionRepository transactions;

    public TransactionQueryService(UserRepository users, TransactionRepository transactions) {
        this.users = users;
        this.transactions = transactions;
    }

    @Transactional(readOnly = true)
    public List<TransactionItemResponse> myStatement(String email, int page, int size) {

        if (page < 0) throw new UnprocessableEntityException("page must be >= 0");
        if (size <= 0) throw new UnprocessableEntityException("size must be >= 1");
        if (size > MAX_SIZE) throw new UnprocessableEntityException("size must be <= " + MAX_SIZE);

        var user = users.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var account = user.getAccount();
        if (account == null) throw new IllegalStateException("User has no account");

        var pageable = PageRequest.of(page, size);

        var list = transactions
                .findByFromAccountOrToAccountOrderByCreatedAtDesc(account, account, pageable);

        return list.stream().map(tx -> {

            String direction = switch (tx.getType()) {
                case DEPOSIT -> "IN";
                case TRANSFER -> {
                    boolean isOutgoing = tx.getFromAccount() != null
                            && tx.getFromAccount().getId().equals(account.getId());
                    yield isOutgoing ? "OUT" : "IN";
                }
                default -> "IN"; // segurança caso adicione novos tipos no futuro
            };

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
