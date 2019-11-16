package webapp.utils;

import database.dto.Comment;
import database.dto.FileInfo;

import java.text.Normalizer;

/**
 * Created by Jakub on 16.11.2019.
 */
public class SearchUtil {

    public static String normalizeText(String string) {
        char[] out = new char[string.length()];
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        int j = 0;
        for (int i = 0, n = string.length(); i < n; ++i) {
            char c = string.charAt(i);
            if (c <= '\u007F') out[j++] = c;
        }
        return new String(out);
    }


    public static boolean findTextInFileContent(FileInfo fileInfo, String text) {
        String fileName = normalizeText(fileInfo.getFileName().toLowerCase());
        if(fileName.contains(text)) {
            return true;
        }
        for(Comment c : fileInfo.getCommentList()) {
            String comText = normalizeText(c.getText().toLowerCase());
            if(comText.contains(text)) {
                return true;
            }
        }
        return false;
    }

}
