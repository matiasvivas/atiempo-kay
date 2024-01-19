package pl.codeleak.demos.sbt.APIToken;

public class TokenValidator {
    private String secretKey;

    public TokenValidator(String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean validateToken(String expectedToken, String actualToken) {
        long currentTime = System.currentTimeMillis();
        long expirationTime = currentTime + 10 * 60 * 1000; // 10 minutos

        if (currentTime < expirationTime && expectedToken.equals(actualToken)) {
            return true;
        }

        return false;
    }
}