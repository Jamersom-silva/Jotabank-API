package com.jamersom.jotabank.transfers;

import com.jamersom.jotabank.common.security.AuthContext;
import com.jamersom.jotabank.transfers.dto.TransferRequest;
import com.jamersom.jotabank.transfers.dto.TransferResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest req) {
        String email = AuthContext.requireEmail();
        var res = service.transfer(email, req);
        return ResponseEntity.status(201).body(res);
    }
}
