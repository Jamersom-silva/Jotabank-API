package com.jamersom.jotabank.transactions;

import com.jamersom.jotabank.common.security.AuthContext;
import com.jamersom.jotabank.transactions.dto.TransactionItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionQueryService service;

    public TransactionController(TransactionQueryService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<List<TransactionItemResponse>> myTransactions() {
        String email = AuthContext.requireEmail();
        return ResponseEntity.ok(service.myStatement(email));
    }
}
