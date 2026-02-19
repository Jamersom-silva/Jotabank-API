package com.jamersom.jotabank.transactions;

import com.jamersom.jotabank.accounts.Account;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccountOrToAccountOrderByCreatedAtDesc(
            Account fromAccount,
            Account toAccount,
            Pageable pageable
    );
}
