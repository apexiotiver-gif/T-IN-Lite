package ai.tinlite;

import java.util.*;

/**
 * T-IN-Lite Trading AI
 * Basic trading information and tips
 * Not financial advice - for educational purposes only
 */
public class TradingAI {
    private static final String API_KEY = "API-KEY-AVKtyhhjkuaaweruubhh";
    private static final String BASE_URL = "https://offlineAi.com";
    private int tokensUsed = 0;
    private Random random = new Random();
    private Map<String, String> stockTips = new HashMap<>();
    private Map<String, String> tradingTerms = new HashMap<>();
    private String[] generalTips = {
        "Always use stop-loss to protect your capital",
        "Never invest more than you can afford to lose",
        "Diversify your portfolio across different sectors",
        "Buy low, sell high - but timing is everything",
        "Follow the trend, do not fight the market",
        "Keep emotions out of trading",
        "Always do your own research before investing",
        "Start with paper trading to practice",
        "Risk management is more important than profits",
        "Do not average down on losing trades blindly"
    };

    public TradingAI() { setupData(); }

    private void setupData() {
        stockTips.put("NIFTY", "NIFTY represents top 50 companies on NSE. Good for long-term investment.");
        stockTips.put("SENSEX", "SENSEX is BSE top 30 companies. India market benchmark.");
        stockTips.put("BANKNIFTY", "Bank NIFTY tracks banking sector. High volatility, good for intraday.");
        stockTips.put("RELIANCE", "Reliance Industries - conglomerate with oil, retail, telecom, and green energy.");
        stockTips.put("TCS", "Tata Consultancy Services - IT giant, stable dividend stock.");
        stockTips.put("INFY", "Infosys - IT services leader, good for long-term holding.");
        stockTips.put("HDFC", "HDFC Bank - largest private bank in India.");
        stockTips.put("TATAMOTORS", "Tata Motors - auto sector, EV focus. Cyclical stock.");
        stockTips.put("ADANI", "Adani Group stocks - high risk high reward. Do thorough research.");
        stockTips.put("ITC", "ITC - FMCG + tobacco. Stable stock with good dividends.");
        tradingTerms.put("stop loss", "Stop loss is a price level where you exit a losing trade to limit damage.");
        tradingTerms.put("intraday", "Intraday means buying and selling on the same day. High risk.");
        tradingTerms.put("delivery", "Delivery means holding shares in your demat account. Long-term.");
        tradingTerms.put("futures", "Futures are contracts to buy/sell at a future date. Leverage involved.");
        tradingTerms.put("options", "Options give right but not obligation to buy/sell. Call and Put.");
        tradingTerms.put("call option", "Call option = right to BUY. You buy call when you expect price to go UP.");
        tradingTerms.put("put option", "Put option = right to SELL. You buy put when you expect price to go DOWN.");
        tradingTerms.put("bull market", "Bull market = prices going up. Optimism in market.");
        tradingTerms.put("bear market", "Bear market = prices going down. Pessimism in market.");
        tradingTerms.put("dividend", "Dividend = company shares profit with shareholders. Passive income.");
        tradingTerms.put("ipo", "IPO = Initial Public Offering. Company selling shares first time to public.");
        tradingTerms.put("market cap", "Market cap = share price x total shares. Company total value.");
        tradingTerms.put("pe ratio", "P/E ratio = price per share / earnings per share. Valuation metric.");
        tradingTerms.put("volume", "Volume = number of shares traded. High volume = high interest.");
        tradingTerms.put("leverage", "Leverage = borrowing to trade more. Amplifies profit AND loss.");
    }

    public String getTradingTip(String apiKey, String symbol) {
        if (!API_KEY.equals(apiKey)) return "Error: Invalid API Key. Visit " + BASE_URL;
        tokensUsed += 10;
        String upper = symbol.toUpperCase();
        if (stockTips.containsKey(upper)) return stockTips.get(upper);
        return "No info for " + symbol + ". Try: NIFTY, SENSEX, BANKNIFTY, RELIANCE, TCS, INFY, HDFC, ITC";
    }

    public String getGeneralTip(String apiKey) {
        if (!API_KEY.equals(apiKey)) return "Error: Invalid API Key";
        tokensUsed += 5;
        return generalTips[random.nextInt(generalTips.length)];
    }

    public String explainTerm(String apiKey, String term) {
        if (!API_KEY.equals(apiKey)) return "Error: Invalid API Key";
        tokensUsed += 8;
        String lower = term.toLowerCase();
        if (tradingTerms.containsKey(lower)) return tradingTerms.get(lower);
        return "Term not found. Try: stop loss, intraday, futures, options, call option, put option, bull market, bear market, dividend, IPO, P/E ratio";
    }

    public String getRandomTip() { return generalTips[random.nextInt(generalTips.length)]; }
    public List<String> getAllSymbols() { return new ArrayList<>(stockTips.keySet()); }
    public List<String> getAllTerms() { return new ArrayList<>(tradingTerms.keySet()); }
    public boolean validateApiKey(String key) { return API_KEY.equals(key); }
    public String getApiKey() { return API_KEY; }
    public String getUrl() { return BASE_URL; }
    public int getTokensUsed() { return tokensUsed; }
    public void resetTokens() { tokensUsed = 0; }
}
