package mx.uach.clubes.Utils;

public abstract class StringUtils {

    public static String SentenceCase(String s) {
        char[] letters = s.toLowerCase().toCharArray();
        boolean nextUpper = false;
        StringBuilder builder = new StringBuilder();

        builder.append((char) (letters[0] - 'a' + 'A'));

        for (int i = 1; i < letters.length; i++) {
            if (nextUpper) {
                builder.append((char) (letters[i] - 'a' + 'A'));
                nextUpper = false;
            } else {
                builder.append(letters[i]);
            }

            if (letters[i] == ' ') {
                nextUpper = true;
                builder.append(letters[i]);
            }
        }

        return builder.toString();
    }
}
