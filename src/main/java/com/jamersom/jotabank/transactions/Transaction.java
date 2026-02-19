package com.jamersom.jotabank.transactions;

import com.jamersom.jotabank.accounts.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "idx_tx_created_at", columnList = "created_at"),
        @Index(name = "idx_tx_from_account_id", columnList = "from_account_id"),
        @Index(name = "idx_tx_to_account_id", columnList = "to_account_id")
})
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pode ser null em DEPOSIT (ex: saldo inicial)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 16)
    private TransactionType type;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public static Transaction transfer(Account from, Account to, BigDecimal amount, String description) {
        return new Transaction(from, to, amount, TransactionType.TRANSFER, description);
    }

    public static Transaction deposit(Account to, BigDecimal amount, String description) {
        return new Transaction(null, to, amount, TransactionType.DEPOSIT, description);
    }

    private Transaction(
            Account fromAccount,
            Account toAccount,
            BigDecimal amount,
            TransactionType type,
            String description
    ) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.description = (description == null || description.isBlank()) ? type.name() : description.trim();
        this.createdAt = OffsetDateTime.now();
    }
}
