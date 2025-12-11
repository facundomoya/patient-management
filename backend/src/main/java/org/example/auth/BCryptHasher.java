package org.example.auth;

import org.mindrot.jbcrypt.BCrypt;
import org.example.auth.ports.PasswordHasher;

public class BCryptHasher implements PasswordHasher {
    private final int logRounds;

    public BCryptHasher(int logRounds) {
        this.logRounds = logRounds;
    }

    @Override
    public String hash(String plain) {
        if (plain == null) throw new IllegalArgumentException("password null");
        return BCrypt.hashpw(plain, BCrypt.gensalt(logRounds));
    }

    @Override
    public boolean matches(String plain, String hash) {
        if (plain == null || hash == null) return false;
        return BCrypt.checkpw(plain, hash);
    }
}
