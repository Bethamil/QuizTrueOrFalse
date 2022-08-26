package org.example.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * @author Emiel Bloem
 * <p>
 * Json file is parsed into this class.
 */
public class JsonOpenTDB {
    public Integer response_code;
    public List<Questions> results;

    public JsonOpenTDB(int response_code, List<Questions> results) {
        this.response_code = response_code;
        this.results = results;
    }

    public static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
