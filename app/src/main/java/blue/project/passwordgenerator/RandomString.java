package blue.project.passwordgenerator;

import java.security.SecureRandom;

public class RandomString {
    // Constants used for generating a random string
    private static final String UPPER_CASE_LETTERS = "ABCDEFGHIGKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE_LETTERS = UPPER_CASE_LETTERS.toLowerCase();
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    /**
     * This method generates a random string.
     * @param length    length of string
     * @param specialCharacters Flag to include special characters
     * @return
     */
    public static String getString(int length, boolean specialCharacters) {
        String finalString;
        if (specialCharacters) {
            finalString = UPPER_CASE_LETTERS + LOWER_CASE_LETTERS + NUMBERS + SPECIAL_CHARACTERS;
        } else {
            finalString = UPPER_CASE_LETTERS + LOWER_CASE_LETTERS + NUMBERS;
        }
        StringBuilder randomString = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomNumber = random.nextInt(finalString.length());
            char pickCharAt = finalString.charAt(randomNumber);
            randomString.append(pickCharAt);
        }
        return randomString.toString();
    }
}
