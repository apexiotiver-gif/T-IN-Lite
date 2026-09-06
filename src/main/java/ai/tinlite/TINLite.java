package ai.tinlite;

import java.util.*;

/**
 * T-IN-Lite: Main AI Assistant Class
 * Combines ChatEngine + WikipediaSearch + TradingAI + ModelLoader
 * 
 * When TFLite model is trained on Google GPU and loaded via ModelLoader,
 * responses will come from the neural network.
 * Until then, rule-based ChatEngine handles all responses.
 * 
 * Usage:
 *   TINLite ai = new TINLite("API-KEY-AVKtyhhjkuaaweruubhh");
 *   String response = ai.ask("hello");
 *   String wiki = ai.searchWikipedia("India");
 *   String trade = ai.getTradingTip("NIFTY");
 */
public class TINLite {
    private static final String API_KEY = "API-KEY-AVKtyhhjkuaaweruubhh";
    private static final String BASE_URL = "https://offlineAi.com";
    private static final String VERSION = "T-IN-Lite-v1.0.0";
    
    private String apiKey;
    private ChatEngine chatEngine;
    private WikipediaSearch wikiSearch;
    private TradingAI tradingAI;
    private ModelLoader modelLoader;
    private int totalTokensUsed = 0;
    private boolean modelMode = false;
    
    public TINLite(String apiKey) {
        this.apiKey = apiKey;
        this.chatEngine = new ChatEngine();
        this.wikiSearch = new WikipediaSearch();
        this.tradingAI = new TradingAI();
        this.modelLoader = null;
    }
    
    public TINLite(String apiKey, String modelPath) {
        this.apiKey = apiKey;
        this.chatEngine = new ChatEngine();
        this.wikiSearch = new WikipediaSearch();
        this.tradingAI = new TradingAI();
        this.modelLoader = new ModelLoader(modelPath);
        if (modelLoader.loadModel()) modelMode = true;
    }
    
    public String ask(String input) {
        if (!API_KEY.equals(apiKey)) return "Error: Invalid API Key. Visit " + BASE_URL;
        String lower = input.toLowerCase();
        totalTokensUsed += 5;
        if (lower.contains("wiki") || lower.contains("wikipedia") || lower.contains("search for") || lower.contains("what is") || lower.contains("who is") || lower.contains("tell me about")) {
            String query = input.replace("what is", "").replace("who is", "").replace("tell me about", "").replace("search for", "").replace("wikipedia", "").replace("wiki", "").trim();
            String result = wikiSearch.searchAndSummarize(query);
            totalTokensUsed += wikiSearch.getTokensUsed();
            return result;
        }
        if (lower.contains("trade") || lower.contains("stock") || lower.contains("nifty") || lower.contains("sensex") || lower.contains("share")) {
            String response = tradingAI.getTradingTip(apiKey, input.replaceAll("[^a-zA-Z]", "").toUpperCase());
            totalTokensUsed += tradingAI.getTokensUsed();
            return response;
        }
        String response = chatEngine.chat(apiKey, input);
        totalTokensUsed += chatEngine.getTokensUsed();
        return response;
    }
    
    public String chat(String input) { return chatEngine.chat(apiKey, input); }
    
    public String searchWikipedia(String query) {
        String result = wikiSearch.searchAndSummarize(query);
        totalTokensUsed += wikiSearch.getTokensUsed();
        return result;
    }
    
    public String getTradingTip(String symbol) {
        String result = tradingAI.getTradingTip(apiKey, symbol);
        totalTokensUsed += tradingAI.getTokensUsed();
        return result;
    }
    
    public String explainTradingTerm(String term) {
        String result = tradingAI.explainTerm(apiKey, term);
        totalTokensUsed += tradingAI.getTokensUsed();
        return result;
    }
    
    public String generatePrompt(String input) {
        if (!API_KEY.equals(apiKey)) return "Error: Invalid API Key. Get key from " + BASE_URL;
        return "prompt{input:" + input + "} prompt{response:" + ask(input) + "}";
    }
    
    public boolean loadModel(String path) {
        modelLoader = new ModelLoader(path);
        boolean loaded = modelLoader.loadModel();
        modelMode = loaded;
        return loaded;
    }
    
    public boolean isModelLoaded() { return modelMode; }
    public String getModelInfo() {
        if (modelLoader == null) return "No model loaded. Using rule-based engine.";
        return modelLoader.getModelInfo();
    }
    
    public boolean validateApiKey(String key) { return API_KEY.equals(key); }
    public String getApiKey() { return API_KEY; }
    public String getUrl() { return BASE_URL; }
    public String getBaseUrl() { return BASE_URL; }
    public String getVersion() { return VERSION; }
    public int getTokensUsed() { return totalTokensUsed; }
    public List<String> getChatHistory() { return chatEngine.getHistory(); }
    public void clearHistory() { chatEngine.clearHistory(); totalTokensUsed = 0; }
    public int getHistorySize() { return chatEngine.getHistorySize(); }
    
    public String getInfo() {
        return VERSION + "\nAPI Key: " + API_KEY + "\nURL: " + BASE_URL + "\nModel: " + (modelMode ? "Loaded" : "Not loaded (rule-based)") + "\nTokens used: " + totalTokensUsed + "\nHistory: " + chatEngine.getHistorySize() + " messages";
    }
}
