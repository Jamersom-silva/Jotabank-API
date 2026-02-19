package com.jamersom.jotabank.auth;

import com.jamersom.jotabank.accounts.Account;
import com.jamersom.jotabank.accounts.AccountRepository;
import com.jamersom.jotabank.auth.dto.*;
import com.jamersom.jotabank.users.Role;
import com.jamersom.jotabank.users.User;
import com.jamersom.jotabank.users.UserRepository;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.AuthenticationException;

import java.math.BigDecimal;

@Service
public class AuthService {

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");
    private static final String CURRENCY = "JTC";

    private final UserRepository users;
    private final AccountRepository accounts;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public AuthService(
            UserRepository users,
            AccountRepository accounts,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authManager,
            JwtService jwt
    ) {
        this.users = users;
        this.accounts = accounts;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwt = jwt;
    }

    @Transactional
    public void register(RegisterRequest req) {
        if (users.existsByEmail(req.email())) {
            throw new com.jamersom.jotabank.common.errors.ConflictException("Email already in use");

        }

        var user = new User(
                req.name(),
                req.email().toLowerCase(),
                passwordEncoder.encode(req.password()),
                Role.CLIENT
        );

        // Persist user first to get ID
        user = users.save(user);

        String accountNumber = generateAccountNumber(user.getId());
        var account = new Account(user, accountNumber, INITIAL_BALANCE, CURRENCY);

        // link both sides
        user.setAccount(account);

        accounts.save(account);
    }

    public AuthResponse login(LoginRequest req) {
        try {
            var token = new UsernamePasswordAuthenticationToken(req.email().toLowerCase(), req.password());
            authManager.authenticate(token);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String jwtToken = jwt.generateToken(req.email().toLowerCase());
        return AuthResponse.bearer(jwtToken);
    }

    private String generateAccountNumber(Long userId) {
        return "ACC-" + String.format("%06d", userId);
    }
}
