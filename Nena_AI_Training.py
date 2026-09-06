"""
Nena AI - T-IN-Lite Model Training Script
==========================================
Run on Google Colab with GPU enabled.

Instructions:
1. Open https://colab.research.google.com
2. Runtime -> Change runtime type -> GPU (T4)
3. Upload this file or copy-paste into a cell
4. Run all cells

This script:
- Trains a real chat model using TensorFlow/Keras (Seq2Seq LSTM + Attention)
- Fetches Wikipedia data for training augmentation
- Converts to TFLite format for Android
- Includes free Wikipedia search API + DuckDuckGo search (no API key)
- Saves .tflite model file for T-IN-Lite library
"""

# ============================================================
# CELL 1: Install Dependencies
# ============================================================
# !pip install tensorflow==2.15.0
# !pip install wikipedia
# !pip install requests

# ============================================================
# CELL 2: Import Libraries
# ============================================================
import tensorflow as tf
import numpy as np
import json
import os
import requests
import wikipedia
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Input, LSTM, Dense, Embedding, Attention
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences

print(f"TensorFlow version: {tf.__version__}")
print(f"GPU available: {tf.config.list_physical_devices('GPU')}")
print("Nena AI - Training Started")

# ============================================================
# CELL 3: Training Data - Hinglish Chat + Q&A
# ============================================================
training_data = [
    ("hello", "Namaste! Main Nena AI hoon. Kaise help karu?"),
    ("hi", "Hi there! Main Nena hoon. Batao kya help chahiye?"),
    ("namaste", "Namaste! Nena AI yahan hai. Kya puchna hai?"),
    ("hey", "Hey! Nena AI ready hai. Kya baat karni hai?"),
    ("good morning", "Good morning! Nena AI aapka swagat hai."),
    ("who are you", "Main Nena AI hoon - T-IN-Lite ka offline AI assistant."),
    ("what is your name", "Mera naam Nena AI hai. T-IN-Lite library ka model hoon."),
    ("kaun ho tum", "Main Nena AI hoon. Offline AI jo bina internet ke kaam karta hai."),
    ("what can you do", "Main chat, Wikipedia search, trading info, aur questions ka answer de sakta hai."),
    ("kaise ho", "Main bilkul thik hoon! Aap kaise ho?"),
    ("how are you", "I am doing great! How about you?"),
    ("kya kar rahe ho", "Main aapki help karne ke liye ready hoon!"),
    ("khana khaaya", "Main AI hoon, khana nahi khata! Lekin aapne khaaya?"),
    ("free ho kya", "Haan bilkul free hoon! Batao kya help chahiye?"),
    ("sad hoon", "Aap sad mat ho. Main yahan hoon. Batao kya hua?"),
    ("dukhi hoon", "Sab theek ho jayega. Main aapki sun sakta hai."),
    ("happy hoon", "Great! Khushi baantne ke liye shukriya!"),
    ("angry hoon", "Gussa mat ho. Deep breath lo. Batao kya hua?"),
    ("help", "Main help kar sakta hai: Chat, Wikipedia, Trading, Questions."),
    ("madad", "Bilkul! Batao kya problem hai?"),
    ("bye", "Bye! Phir milenge. Take care!"),
    ("tata", "Tata! Khayal rakhna!"),
    ("goodbye", "Goodbye! Nena AI yahan rahega jab bhi zaroorat ho."),
    ("thank you", "You are welcome! Koi aur help chahiye?"),
    ("shukriya", "Shukriya! Koi baat nahi. Aur kuch?"),
    ("thanks", "Anytime! Main hamesha ready hoon."),
    ("what is nifty", "NIFTY represents top 50 companies on NSE. India ka main index."),
    ("what is sensex", "SENSEX is BSE top 30 companies. India ka benchmark index."),
    ("trading tips", "Always use stop-loss. Never invest more than you can lose."),
    ("stock market", "Stock market mein shares buy/sell karte hai. NSE aur BSE India ke main exchanges."),
    ("what is india", "India ek democratic country hai South Asia mein. Capital New Delhi."),
    ("what is ai", "AI (Artificial Intelligence) ek technology hai jisse machines think kar sakte hai."),
    ("what is machine learning", "Machine Learning AI ka part hai jisse computers data se seekhte hai."),
    ("what is python", "Python ek programming language hai. AI/ML ke liye popular."),
    ("what is android", "Android Google ka mobile OS hai. Linux based, smartphones mein chalta hai."),
    ("tell me a joke", "Why do programmers prefer dark mode? Because light attracts bugs!"),
    ("joke sunao", "Teacher: What is 2+2? Student: 22! Teacher: Wrong! Student: Right in base 3!"),
    ("you are awesome", "Shukriya! Aap bhi awesome ho!"),
    ("you are great", "Thank you! Aapki baat karke accha lagta hai."),
    ("nice", "Thanks! Aur kuch puchna hai?"),
    ("api key", "API Key: API-KEY-AVKtyhhjkuaaweruubhh. Get it from https://offlineAi.com"),
    ("tokens", "Token system har response ka track rakhta hai."),
    ("what is bitcoin", "Bitcoin ek cryptocurrency hai. Digital currency, blockchain pe based."),
    ("what is cricket", "Cricket ek sport hai. India mein bahut popular hai. IPL sabse bada league."),
    ("what is bollywood", "Bollywood Hindi film industry hai. Mumbai based. Bahut bada industry."),
    ("what is hindi", "Hindi India ki official language hai. Devanagari script mein likhi jati hai."),
]

print(f"Training data: {len(training_data)} pairs")

# ============================================================
# CELL 4: Wikipedia Data Augmentation
# ============================================================
wiki_topics = [
    "India", "Artificial intelligence", "Machine learning", "Python programming",
    "Android operating system", "Stock market", "Cryptocurrency", "Bitcoin",
    "Cricket", "Bollywood", "Hindi language", "New Delhi", "Mumbai",
]

print(f"\nFetching Wikipedia summaries for {len(wiki_topics)} topics...")

for topic in wiki_topics:
    try:
        summary = wikipedia.summary(topic, sentences=2)
        question = f"what is {topic.lower()}"
        answer = summary[:200]
        training_data.append((question, answer))
        print(f"  + {topic}: {len(answer)} chars")
    except Exception as e:
        print(f"  - {topic}: {e}")

print(f"\nTotal training data: {len(training_data)} pairs")

# ============================================================
# CELL 5: Preprocess Data
# ============================================================
questions = [pair[0] for pair in training_data]
answers = [pair[1] for pair in training_data]
answers_with_tokens = ['<start> ' + ans + ' <end>' for ans in answers]

vocab_size = 2000
max_length = 50

tokenizer = Tokenizer(num_words=vocab_size, oov_token='<OOV>')
tokenizer.fit_on_texts(questions + answers_with_tokens)

question_seqs = tokenizer.texts_to_sequences(questions)
answer_seqs = tokenizer.texts_to_sequences(answers_with_tokens)

question_padded = pad_sequences(question_seqs, maxlen=max_length, padding='post')
answer_padded = pad_sequences(answer_seqs, maxlen=max_length, padding='post')

actual_vocab_size = min(vocab_size, len(tokenizer.word_index) + 1)
print(f"Vocabulary size: {actual_vocab_size}")
print(f"Question shape: {question_padded.shape}")
print(f"Answer shape: {answer_padded.shape}")

# ============================================================
# CELL 6: Build Model (Seq2Seq LSTM + Attention)
# ============================================================
embedding_dim = 128
latent_dim = 256

# Encoder
encoder_inputs = Input(shape=(max_length,))
encoder_embedding = Embedding(actual_vocab_size, embedding_dim, name='encoder_embedding')(encoder_inputs)
encoder_lstm = LSTM(latent_dim, return_state=True, return_sequences=True, name='encoder_lstm')
encoder_outputs, state_h, state_c = encoder_lstm(encoder_embedding)
encoder_states = [state_h, state_c]

# Decoder
decoder_inputs = Input(shape=(max_length,))
decoder_embedding = Embedding(actual_vocab_size, embedding_dim, name='decoder_embedding')(decoder_inputs)
decoder_lstm = LSTM(latent_dim, return_sequences=True, return_state=True, name='decoder_lstm')
decoder_outputs, _, _ = decoder_lstm(decoder_embedding, initial_state=encoder_states)

# Attention
attention = Attention(name='attention')([decoder_outputs, encoder_outputs])
decoder_concat = tf.keras.layers.Concatenate(axis=-1)([decoder_outputs, attention])

# Output
decoder_dense = Dense(actual_vocab_size, activation='softmax', name='output_dense')
decoder_outputs = decoder_dense(decoder_concat)

model = Model([encoder_inputs, decoder_inputs], decoder_outputs)
model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])

model.summary()
print(f"Total parameters: {model.count_params():,}")

# ============================================================
# CELL 7: Prepare Training Data
# ============================================================
decoder_input_data = answer_padded[:, :-1]
decoder_target_data = answer_padded[:, 1:]
decoder_target_data = np.expand_dims(decoder_target_data, -1)
decoder_input_data = pad_sequences(decoder_input_data, maxlen=max_length, padding='post')

# ============================================================
# CELL 8: Train Model
# ============================================================
epochs = 100
batch_size = 32

print(f"\nTraining {epochs} epochs, batch {batch_size}...")

history = model.fit(
    [question_padded, decoder_input_data],
    decoder_target_data,
    batch_size=batch_size,
    epochs=epochs,
    validation_split=0.1,
    verbose=1
)

print(f"Final accuracy: {history.history['accuracy'][-1]:.4f}")
print(f"Final loss: {history.history['loss'][-1]:.4f}")

# ============================================================
# CELL 9: Convert to TFLite
# ============================================================
print("Converting to TFLite...")

tokenizer_json = tokenizer.to_json()
with open('nena_tokenizer.json', 'w') as f:
    f.write(tokenizer_json)

model.save('nena_ai_model.h5')

converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
converter.target_spec.supported_types = [tf.float16]

tflite_model = converter.convert()

with open('nena_ai.tflite', 'wb') as f:
    f.write(tflite_model)

model_size = os.path.getsize('nena_ai.tflite')
print(f"TFLite saved: nena_ai.tflite ({model_size:,} bytes = {model_size/1024:.1f} KB)")

# ============================================================
# CELL 10: Wikipedia Search API (Free, no key needed)
# ============================================================
class NenaWikipediaSearch:
    def __init__(self, language='en'):
        self.api_url = f'https://{language}.wikipedia.org/w/api.php'
        self.summary_url = f'https://{language}.wikipedia.org/api/rest_v1/page/summary'
    
    def search(self, query, limit=5):
        params = {'action': 'query', 'list': 'search', 'srsearch': query, 'format': 'json', 'srlimit': str(limit)}
        try:
            r = requests.get(self.api_url, params=params, timeout=5)
            data = r.json()
            return [{'title': i['title'], 'snippet': i.get('snippet','')} for i in data.get('query',{}).get('search',[])]
        except Exception as e:
            return [{'error': str(e)}]
    
    def get_summary(self, title):
        try:
            url = f'{self.summary_url}/{title.replace(" ", "_")}'
            r = requests.get(url, timeout=5)
            data = r.json()
            return {'title': data.get('title',''), 'extract': data.get('extract',''), 'url': data.get('content_urls',{}).get('desktop',{}).get('page','')}
        except Exception as e:
            return {'error': str(e)}
    
    def search_and_summarize(self, query):
        results = self.search(query, limit=1)
        if results and 'error' not in results[0]:
            return self.get_summary(results[0]['title'])
        return {'error': 'No results'}

# Test
wiki = NenaWikipediaSearch()
result = wiki.search_and_summarize("Artificial Intelligence")
print(f"\nWiki test: {result.get('title','N/A')} - {result.get('extract','N/A')[:100]}...")

# ============================================================
# CELL 11: Free Search API (DuckDuckGo - no key needed)
# ============================================================
class NenaFreeSearch:
    def __init__(self):
        self.api_url = 'https://api.duckduckgo.com/'
    
    def search(self, query, limit=5):
        params = {'q': query, 'format': 'json', 'no_html': '1', 'skip_disambig': '1'}
        try:
            r = requests.get(self.api_url, params=params, timeout=5)
            data = r.json()
            results = []
            if data.get('AbstractText'):
                results.append({'title': data.get('Heading',''), 'text': data.get('AbstractText',''), 'url': data.get('AbstractURL','')})
            for topic in data.get('RelatedTopics', [])[:limit]:
                if isinstance(topic, dict) and topic.get('Text'):
                    results.append({'title': topic.get('Text','')[:50], 'text': topic.get('Text',''), 'url': topic.get('FirstURL','')})
            return results if results else [{'error': 'No results'}]
        except Exception as e:
            return [{'error': str(e)}]

# Test
search = NenaFreeSearch()
results = search.search("AI tools")
print(f"\nDuckDuckGo test: {len(results)} results")

# ============================================================
# CELL 12: Save Metadata
# ============================================================
metadata = {
    'model_name': 'Nena AI',
    'version': 'T-IN-Lite-v1.0',
    'api_key': 'API-KEY-AVKtyhhjkuaaweruubhh',
    'base_url': 'https://offlineAi.com',
    'vocab_size': actual_vocab_size,
    'max_length': max_length,
    'epochs': epochs,
    'accuracy': float(history.history['accuracy'][-1]),
    'loss': float(history.history['loss'][-1]),
    'training_pairs': len(training_data),
    'tflite_size': model_size,
}

with open('nena_metadata.json', 'w') as f:
    json.dump(metadata, f, indent=2)

print(json.dumps(metadata, indent=2))

# ============================================================
# CELL 13: Download Files
# ============================================================
from google.colab import files

print("\nDownloading files...")
files.download('nena_ai.tflite')
files.download('nena_tokenizer.json')
files.download('nena_metadata.json')

print("\n===== DONE =====")
print("1. nena_ai.tflite - Put in app assets folder")
print("2. nena_tokenizer.json - For text processing")
print("3. nena_metadata.json - Model info")
print("\nLoad in T-IN-Lite: ai.loadModel('/path/to/nena_ai.tflite')")
