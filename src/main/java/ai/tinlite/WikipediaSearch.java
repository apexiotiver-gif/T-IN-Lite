package ai.tinlite;

import java.io.*;
import java.net.*;

/**
 * Wikipedia Search Engine
 * Real API calls to Wikipedia for search results
 * Requires internet connection
 */
public class WikipediaSearch {
    private static final String WIKI_API = "https://en.wikipedia.org/w/api.php";
    private static final String WIKI_API_HI = "https://hi.wikipedia.org/w/api.php";
    private boolean useHindi = false;
    private int tokensUsed = 0;

    public WikipediaSearch() {}
    public WikipediaSearch(boolean hindi) { this.useHindi = hindi; }

    public String search(String query) {
        tokensUsed += 10;
        try {
            String api = useHindi ? WIKI_API_HI : WIKI_API;
            String encoded = URLEncoder.encode(query, "UTF-8");
            String urlStr = api + "?action=query&list=search&srsearch=" + encoded + "&format=json&srlimit=3";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();
            conn.disconnect();
            return sb.toString();
        } catch (Exception e) { return "Error: Wikipedia search failed - " + e.getMessage(); }
    }

    public String getSummary(String title) {
        tokensUsed += 15;
        try {
            String encoded = URLEncoder.encode(title, "UTF-8");
            String urlStr = useHindi ? "https://hi.wikipedia.org/api/rest_v1/page/summary/" + encoded : "https://en.wikipedia.org/api/rest_v1/page/summary/" + encoded;
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            reader.close();
            conn.disconnect();
            return sb.toString();
        } catch (Exception e) { return "Error: Could not get summary - " + e.getMessage(); }
    }

    public String searchAndSummarize(String query) {
        tokensUsed += 25;
        String searchResult = search(query);
        if (searchResult.startsWith("Error:")) return searchResult;
        String summary = getSummary(query);
        if (summary.startsWith("Error:")) return searchResult;
        return summary;
    }

    public void setHindi(boolean hindi) { this.useHindi = hindi; }
    public boolean isHindi() { return useHindi; }
    public int getTokensUsed() { return tokensUsed; }
    public void resetTokens() { tokensUsed = 0; }
}
