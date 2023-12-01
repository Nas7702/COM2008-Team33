package UserInterface.Views;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashedPasswordGenerator {

    private static final String SALT = "n$3c#r8t&kL!o@pY";

    public static String generateHash(char[] password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((new String(password) + SALT).getBytes());
        byte[] bytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
