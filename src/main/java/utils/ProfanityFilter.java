package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ProfanityFilter {
    public static boolean containsBadWords(String text) {
        try {
            String encodedText = URLEncoder.encode(text, "UTF-8");
            String url = "https://www.purgomalum.com/service/containsprofanity?text=" + encodedText;

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = in.readLine();
            in.close();

            return Boolean.parseBoolean(result);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // fail-safe: allow if check fails
        }
    }
}
