package com.jamersom.jotabank.accounts;

import com.jamersom.jotabank.common.errors.UnprocessableEntityException;
import com.jamersom.jotabank.users.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Entity
@Table(
        name = "accounts",
        indexes = {
                @Index(name = "idx_accounts_account_number", columnList = "accountNumber", unique = true)
        }
)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, length = 8)
    private String currency;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    protected Account() {
    }

    public Account(User user, String accountNumber, BigDecimal initialBalance, String currency) {
        if (user == null) throw new IllegalArgumentException("User is required");
        if (accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("Account number is required");
        if (currency == null || currency.isBlank())
            throw new IllegalArgumentException("Currency is required");

        this.user = user;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.balance = normalize(initialBalance == null ? BigDecimal.ZERO : initialBalance);
    }

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
    public Instant getCreatedAt() { return createdAt; }
    public User getUser() { return user; }

    public void credit(BigDecimal amount) {
        BigDecimal normalized = normalize(amount);
        if (normalized.signum() <= 0)
            throw new UnprocessableEntityException("Amount must be positive");

        this.balance = this.balance.add(normalized);
    }

    public void debit(BigDecimal amount) {
        BigDecimal normalized = normalize(amount);

        if (normalized.signum() <= 0)
            throw new UnprocessableEntityException("Amount must be positive");

        if (this.balance.compareTo(normalized) < 0)
            throw new UnprocessableEntityException("Insufficient balance");

        this.balance = this.balance.subtract(normalized);
    }

    private BigDecimal normalize(BigDecimal amount) {
        if (amount == null)
            throw new UnprocessableEntityException("Amount is required");

        return amount.setScale(2, RoundingMode.UNNECESSARY);
    }
}
