package filemanager.utility;

import java.nio.CharBuffer;

public class FileNameValidator {
    private static final String[] notAllowedChars = {"<", ">", "/", "\\", "*", "?", "\"", ":", "|"};

    public static boolean validate(String fileName) {
        if ((fileName.length() > 0 && fileName.length() <= 100)) {
            for (String charToCheck : notAllowedChars) {
                if (fileName.contains(charToCheck))
                    return false;
            }
        } else return false;
        return true;
    }

}
