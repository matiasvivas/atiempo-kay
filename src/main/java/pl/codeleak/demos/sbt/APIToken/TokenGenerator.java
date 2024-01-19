package pl.codeleak.demos.sbt.APIToken;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class TokenGenerator {

    private String secretKey;

    public TokenGenerator(String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken() {
        long expirationTime = System.currentTimeMillis() + 10 * 60 * 1000; // 10 minutos
        String dataToHash = secretKey + expirationTime;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
