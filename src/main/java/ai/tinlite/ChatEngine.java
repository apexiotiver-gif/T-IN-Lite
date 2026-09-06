package ai.tinlite;

import java.util.*;

/**
 * T-IN-Lite Chat Engine
 * Rule-based chat with context awareness
 * Falls back to this when TFLite model is not loaded
 */
public class ChatEngine {
    private static final String API_KEY = "API-KEY-AVKtyhhjkuaaweruubhh";
    private static final String BASE_URL = "https://offlineAi.com";
    
    private List<String> conversationHistory = new ArrayList<>();
    private Map<String, String> contextMap = new HashMap<>();
    private int tokensUsed = 0;
    private Random random = new Random();
    
    private String[] greetings = {"Namaste!", "Hello!", "Hi there!", "Hey!", "Kaise ho?", "Namaskar!"};
    private String[] farewells = {"Bye!", "Tata!", "Phir milenge!", "Alvida!", "Goodbye!", "Take care!"};
    private String[] compliments = {"Bahut accha!", "Great!", "Nice!", "Wonderful!", "Zabardast!", "Fantastic!"};
    private String[] emotions_happy = {"Khushi hui!", "I am happy!", "Great mood!", "Feeling good!"};
    private String[] emotions_sad = {"Don't worry", "Everything will be fine", "I understand", "Stay strong"};
    private String[] emotions_angry = {"Calm down", "Take a breath", "Relax", "Let it go"};
    private String[] casual = {"Interesting", "Tell me more", "Hmm", "I see", "Okay", "Go on"};
    private String[] thinking = {"Let me think...", "Hmm, good question", "Let me process that", "Thinking..."};
    
    public ChatEngine() {}
    
    public String chat(String apiKey, String input) {
        if (!API_KEY.equals(apiKey)) {
            return "Error: Invalid API Key. Visit " + BASE_URL;
        }
        conversationHistory.add("user:" + input);
        tokensUsed += 5;
        String lower = input.toLowerCase();
        String response;
        if (lower.contains("hi") || lower.contains("hello") || lower.contains("namaste") || lower.contains("hey")) {
            response = getRandom(greetings);
        } else if (lower.contains("bye") || lower.contains("tata") || lower.contains("alvida")) {
            response = getRandom(farewells);
        } else if (lower.contains("good") || lower.contains("nice") || lower.contains("great") || lower.contains("accha")) {
            response = getRandom(compliments);
        } else if (lower.contains("happy") || lower.contains("khush") || lower.contains("good mood")) {
            response = getRandom(emotions_happy);
        } else if (lower.contains("sad") || lower.contains("dukhi") || lower.contains("depressed")) {
            response = getRandom(emotions_sad);
        } else if (lower.contains("angry") || lower.contains("gussa") || lower.contains("mad")) {
            response = getRandom(emotions_angry);
        } else if (lower.contains("think") || lower.contains("soch") || lower.contains("question")) {
            response = getRandom(thinking);
        } else if (lower.contains("who are you") || lower.contains("kaun ho")) {
            response = "I am T-IN-Lite, an offline AI assistant. Version 1.0";
        } else if (lower.contains("what can you do") || lower.contains("kya kar sakte")) {
            response = "I can chat, search Wikipedia, give trading info, and answer questions. All offline!";
        } else if (lower.contains("name") || lower.contains("naam")) {
            response = "My name is T-IN-Lite. Offline AI by apexiotiver-gif.";
        } else if (lower.contains("help") || lower.contains("madad")) {
            response = "I can help with: chat, Wikipedia search, trading info, questions. Just ask me anything!";
        } else if (lower.contains("thank") || lower.contains("shukriya") || lower.contains("dhanyawad")) {
            response = "You are welcome! Anytime!";
        } else if (lower.contains("wiki") || lower.contains("wikipedia") || lower.contains("search")) {
            response = "Use WikipediaSearch class to search Wikipedia. Example: new WikipediaSearch().searchAndSummarize(\"India\")";
        } else if (lower.contains("trade") || lower.contains("trading") || lower.contains("stock")) {
            response = "Use TradingAI class for trading info. Example: new TradingAI().getTradingTip(\"NIFTY\")";
        } else if (lower.contains("api") || lower.contains("key")) {
            response = "API Key: " + API_KEY + ". Get it from " + BASE_URL;
        } else if (lower.contains("token")) {
            response = "Tokens used: " + tokensUsed + ". Token system tracks your usage.";
        } else if (lower.contains("version") || lower.contains("update")) {
            response = "T-IN-Lite v1.0.0 - Offline AI with Wikipedia + Trading + Chat";
        } else if (lower.contains("joke") || lower.contains("hasa")) {
            response = "Why do programmers prefer dark mode? Because light attracts bugs!";
        } else if (lower.contains("time") || lower.contains("samay")) {
            response = "Current time: " + new Date().toString();
        } else if (lower.contains("date") || lower.contains("tareek")) {
            response = "Today: " + new Date().toString();
        } else {
            response = getRandom(casual) + " Tell me more about what you want to know.";
        }
        conversationHistory.add("ai:" + response);
        tokensUsed += 3;
        contextMap.put("last_input", input);
        contextMap.put("last_response", response);
        return response;
    }
    
    private String getRandom(String[] arr) { return arr[random.nextInt(arr.length)]; }
    
    public String generatePrompt(String apiKey, String input) {
        if (!API_KEY.equals(apiKey)) return "Error: Invalid API Key. Get key from " + BASE_URL;
        return "prompt{input:" + input + "} prompt{response:" + chat(apiKey, input) + "}";
    }
    
    public boolean validateApiKey(String key) { return API_KEY.equals(key); }
    public String getApiKey() { return API_KEY; }
    public String getUrl() { return BASE_URL; }
    public String getBaseUrl() { return BASE_URL; }
    public int getTokensUsed() { return tokensUsed; }
    public List<String> getHistory() { return conversationHistory; }
    public String getLastInput() { return contextMap.get("last_input"); }
    public String getLastResponse() { return contextMap.get("last_response"); }
    public void clearHistory() { conversationHistory.clear(); contextMap.clear(); tokensUsed = 0; }
    public int getHistorySize() { return conversationHistory.size(); }
}
