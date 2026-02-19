package com.jamersom.jotabank.accounts;

import com.jamersom.jotabank.accounts.dto.AccountMeResponse;
import com.jamersom.jotabank.common.security.AuthContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountQueryService query;

    public AccountController(AccountQueryService query) {
        this.query = query;
    }

    @GetMapping("/me")
    public ResponseEntity<AccountMeResponse> me() {
        String email = AuthContext.requireEmail();
        return ResponseEntity.ok(query.getMyAccount(email));
    }
}
