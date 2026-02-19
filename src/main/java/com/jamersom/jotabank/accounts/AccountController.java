package com.jamersom.jotabank.accounts;

import com.jamersom.jotabank.accounts.dto.DepositRequest;
import com.jamersom.jotabank.accounts.dto.DepositResponse;
import com.jamersom.jotabank.common.security.AuthContext;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountOperationsService ops;

    public AccountController(AccountOperationsService ops) {
        this.ops = ops;
    }

    @PostMapping("/me/deposit")
    public ResponseEntity<DepositResponse> deposit(@Valid @RequestBody DepositRequest req) {
        String email = AuthContext.requireEmail();
        return ResponseEntity.ok(ops.deposit(email, req));
    }
}
