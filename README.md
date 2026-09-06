# T-IN-Lite

**Offline AI Assistant with Chat + Wikipedia Search + Trading Info + TFLite Model Support**

T-IN-Lite is NOT an LLM. It is a rule-based AI with real Wikipedia API integration, trading information, and support for loading custom TFLite models trained on Google GPU. Works in Sketchware via JitPack.

- **Chat Engine** — rule-based Hinglish chat with context awareness
- **Wikipedia Search** — real API calls to Wikipedia (English + Hindi)
- **Trading AI** — stock tips, trading terms, general trading advice
- **Model Loader** — load your own .tflite model (trained on Google GPU)
- **Token Tracking** — every action consumes virtual tokens
- **API Key** — `API-KEY-AVKtyhhjkuaaweruubhh`
- **Prompt Format** — `prompt{input:...} prompt{response:...}`

---

## Dependency

```gradle
implementation 'com.github.apexiotiver-gif:T-IN-Lite:v1.0.0'
```

### JitPack Repository

```gradle
repositories {
    google()
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```

### Package

```java
import ai.tinlite.*;
```

---

## Classes

| Class | Purpose |
|---|---|
| `TINLite` | Main class — combines all features |
| `ChatEngine` | Rule-based chat with Hinglish responses |
| `WikipediaSearch` | Real Wikipedia API search (needs internet) |
| `TradingAI` | Stock tips, trading terms, general advice |
| `ModelLoader` | Load .tflite model file for neural network inference |

---

## Quick Start

```java
import ai.tinlite.*;

TINLite ai = new TINLite("API-KEY-AVKtyhhjkuaaweruubhh");

// Chat
String response = ai.ask("hello");
// Returns: "Namaste!" or "Hello!" etc.

// Wikipedia search (needs internet)
String wiki = ai.searchWikipedia("India");
// Returns: Wikipedia summary JSON

// Trading info
String tip = ai.getTradingTip("NIFTY");
// Returns: "NIFTY represents top 50 companies on NSE..."

// Trading term explanation
String explain = ai.explainTradingTerm("call option");
// Returns: "Call option = right to BUY..."

// Generate prompt format
String prompt = ai.generatePrompt("kaise ho");
// Returns: prompt{input:kaise ho} prompt{response:...}

// Get info
String info = ai.getInfo();
// Returns version, API key, model status, tokens used

// Token tracking
int tokens = ai.getTokensUsed();

// Chat history
List<String> history = ai.getChatHistory();
int size = ai.getHistorySize();
```

---

## All Methods

### TINLite (Main Class)

| Method | Parameters | Returns | Description |
|---|---|---|---|
| `ask(input)` | String | String | Auto-route: chat, wiki, or trading |
| `chat(input)` | String | String | Chat response only |
| `searchWikipedia(query)` | String | String | Wikipedia summary (needs internet) |
| `getTradingTip(symbol)` | String | String | Stock info (NIFTY, SENSEX, etc.) |
| `explainTradingTerm(term)` | String | String | Trading term explanation |
| `generatePrompt(input)` | String | String | prompt{input:...} prompt{response:...} |
| `loadModel(path)` | String | boolean | Load .tflite model file |
| `isModelLoaded()` | none | boolean | Check if model is loaded |
| `getModelInfo()` | none | String | Model version, path, loaded status |
| `validateApiKey(key)` | String | boolean | Check API key |
| `getApiKey()` | none | String | Returns API key |
| `getUrl()` | none | String | Returns https://offlineAi.com |
| `getVersion()` | none | String | Returns T-IN-Lite-v1.0.0 |
| `getTokensUsed()` | none | int | Total tokens consumed |
| `getChatHistory()` | none | List&lt;String&gt; | All conversation messages |
| `getHistorySize()` | none | int | Number of messages |
| `clearHistory()` | none | void | Clear history + reset tokens |
| `getInfo()` | none | String | Full status summary |

### ChatEngine

| Method | Parameters | Returns | Description |
|---|---|---|---|
| `chat(apiKey, input)` | String, String | String | Get chat response |
| `generatePrompt(apiKey, input)` | String, String | String | Full prompt format |
| `validateApiKey(key)` | String | boolean | Check API key |
| `getApiKey()` | none | String | API key |
| `getUrl()` | none | String | Base URL |
| `getTokensUsed()` | none | int | Tokens used |
| `getHistory()` | none | List&lt;String&gt; | Conversation history |
| `getLastInput()` | none | String | Last user input |
| `getLastResponse()` | none | String | Last AI response |
| `clearHistory()` | none | void | Reset everything |

### WikipediaSearch

| Method | Parameters | Returns | Description |
|---|---|---|---|
| `search(query)` | String | String | Search Wikipedia (JSON) |
| `getSummary(title)` | String | String | Get page summary (JSON) |
| `searchAndSummarize(query)` | String | String | Search + summary combined |
| `setHindi(bool)` | boolean | void | Use Hindi Wikipedia |
| `isHindi()` | none | boolean | Hindi mode check |
| `getTokensUsed()` | none | int | Tokens used |
| `resetTokens()` | none | void | Reset token counter |

### TradingAI

| Method | Parameters | Returns | Description |
|---|---|---|---|
| `getTradingTip(apiKey, symbol)` | String, String | String | Stock info (NIFTY, RELIANCE, etc.) |
| `getGeneralTip(apiKey)` | String | String | Random trading tip |
| `explainTerm(apiKey, term)` | String, String | String | Trading term explanation |
| `getRandomTip()` | none | String | Random tip (no key needed) |
| `getAllSymbols()` | none | List&lt;String&gt; | All stock symbols |
| `getAllTerms()` | none | List&lt;String&gt; | All trading terms |
| `getTokensUsed()` | none | int | Tokens used |
| `resetTokens()` | none | void | Reset tokens |

### ModelLoader

| Method | Parameters | Returns | Description |
|---|---|---|---|
| `loadModel()` | none | boolean | Load .tflite from path |
| `isModelLoaded()` | none | boolean | Check loaded status |
| `getModelVersion()` | none | String | Model version string |
| `getModelPath()` | none | String | File path |
| `getModelBuffer()` | none | ByteBuffer | Raw model bytes |
| `getInputSize()` | none | int | Input dimension |
| `getOutputSize()` | none | int | Output dimension |
| `setModelVersion(v)` | String | void | Set version |
| `setInputSize(s)` | int | void | Set input size |
| `setOutputSize(s)` | int | void | Set output size |
| `getModelInfo()` | none | String | Full model info |

---

## Available Stock Symbols

`NIFTY`, `SENSEX`, `BANKNIFTY`, `RELIANCE`, `TCS`, `INFY`, `HDFC`, `TATAMOTORS`, `ADANI`, `ITC`

## Available Trading Terms

`stop loss`, `intraday`, `delivery`, `futures`, `options`, `call option`, `put option`, `bull market`, `bear market`, `dividend`, `ipo`, `market cap`, `pe ratio`, `volume`, `leverage`

---

## 10 Usage Examples

### Example 1 — Basic chat
```java
TINLite ai = new TINLite("API-KEY-AVKtyhhjkuaaweruubhh");
String r = ai.ask("hello");
// "Namaste!" or "Hello!"
```

### Example 2 — Wikipedia search
```java
TINLite ai = new TINLite("API-KEY-AVKtyhhjkuaaweruubhh");
String r = ai.searchWikipedia("India");
// Wikipedia summary JSON (needs internet)
```

### Example 3 — Trading tip
```java
TINLite ai = new TINLite("API-KEY-AVKtyhhjkuaaweruubhh");
String r = ai.getTradingTip("NIFTY");
// "NIFTY represents top 50 companies on NSE..."
```

### Example 4 — Explain trading term
```java
TINLite ai = new TINLite("API-KEY-AVKtyhhjkuaaweruubhh");
String r = ai.explainTradingTerm("call option");
// "Call option = right to BUY. You buy call when you expect price to go UP."
```

### Example 5 — Auto-route (ask handles everything)
```java
TINLite ai = new TINLite("API-KEY-AVKtyhhjkuaaweruubhh");
String r1 = ai.ask("what is India");        // → Wikipedia
String r2 = ai.ask("NIFTY stock");          // → Trading
String r3 = ai.ask("hello");                // → Chat
```

### Example 6 — Load TFLite model
```java
TINLite ai = new TINLite("API-KEY-AVKtyhhjkuaaweruubhh", "/sdcard/model.tflite");
if (ai.isModelLoaded()) {
    String info = ai.getModelInfo();
    // "Model: T-IN-Lite-v1.0\nPath: /sdcard/model.tflite\nLoaded: true"
}
```

### Example 7 — Token tracking
```java
TINLite ai = new TINLite("API-KEY-AVKtyhhjkuaaweruubhh");
ai.ask("hello");
ai.ask("NIFTY");
ai.searchWikipedia("India");
int tokens = ai.getTokensUsed();
// Total tokens from all 3 calls
```

### Example 8 — Chat history
```java
TINLite ai = new TINLite("API-KEY-AVKtyhhjkuaaweruubhh");
ai.ask("hello");
ai.ask("kaise ho");
List<String> history = ai.getChatHistory();
// ["user:hello", "ai:Namaste!", "user:kaise ho", "ai:..."]
```

### Example 9 — Hindi Wikipedia
```java
WikipediaSearch wiki = new WikipediaSearch(true);
String r = wiki.searchAndSummarize("India");
// Hindi Wikipedia results
```

### Example 10 — Complete Sketchware app
```java
// onCreate:
final TINLite ai = new TINLite("API-KEY-AVKtyhhjkuaaweruubhh");

// button1 (Send):
String input = edittext1.getText().toString();
String response = ai.ask(input);
textview1.setText(response);

// button2 (Wiki):
String query = edittext1.getText().toString();
String result = ai.searchWikipedia(query);
textview1.setText(result);

// button3 (Trading):
String symbol = edittext1.getText().toString();
String tip = ai.getTradingTip(symbol);
textview1.setText(tip);

// button4 (Info):
textview1.setText(ai.getInfo());

// button5 (Clear):
ai.clearHistory();
textview1.setText("");
```

---

## TFLite Model Training (Google GPU)

When you train your model on Google GPU:

1. Train model in TensorFlow/Keras
2. Convert to TFLite: `converter = tf.lite.TFLiteConverter.from_keras_model(model)`
3. Save as `.tflite` file
4. Load in T-IN-Lite: `ai.loadModel("/path/to/model.tflite")`
5. Check: `ai.isModelLoaded()` returns `true`

The ModelLoader class handles:
- Reading .tflite file into ByteBuffer
- Version tracking
- Input/output size configuration
- Model info display

---

## Sketchware Installation

1. Open Sketchware project
2. Library Manager, add JitPack: `maven { url 'https://jitpack.io' }`
3. Add dependency: `com.github.apexiotiver-gif:T-IN-Lite:v1.0.0`
4. In code: `import ai.tinlite.*;`
5. Use: `new TINLite("API-KEY-AVKtyhhjkuaaweruubhh").ask("hello")`

---

## Key Facts

- **API Key:** `API-KEY-AVKtyhhjkuaaweruubhh` (validated locally)
- **Base URL:** `https://offlineAi.com` (reference only)
- **Version:** T-IN-Lite-v1.0.0
- **Chat:** Rule-based, offline, Hinglish responses
- **Wikipedia:** Real API calls, needs internet, English + Hindi
- **Trading:** 10 stock symbols, 15 trading terms, 10 general tips
- **Model:** Load .tflite via ModelLoader (trained on Google GPU)
- **Tokens:** Tracked per TINLite instance, reset on clearHistory()

## Links

- GitHub: https://github.com/apexiotiver-gif/T-IN-Lite
- JitPack: https://jitpack.io/#apexiotiver-gif/T-IN-Lite
- Dependency: `com.github.apexiotiver-gif:T-IN-Lite:v1.0.0`
