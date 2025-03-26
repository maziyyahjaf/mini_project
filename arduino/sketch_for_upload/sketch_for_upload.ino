#include <EspMQTTClient.h>
#include <Adafruit_NeoPixel.h>
#include <ArduinoJson.h>
#include <Wire.h>
#include <Adafruit_MPRLS.h>

#define RESET_PIN -1
#define EOC_PIN -1
#define NEOPIXEL_PIN 6
#define NUM_PIXELS 16
#define VIBRATION_PIN 12

Adafruit_MPRLS mpr = Adafruit_MPRLS(RESET_PIN, EOC_PIN);
Adafruit_NeoPixel pixels(NUM_PIXELS, NEOPIXEL_PIN, NEO_GRBW + NEO_KHZ800);


const char* ssid = "Pixel_7318";
const char* password = "XXXXXX";

const char* mqttServer = "XXXX";
const int mqttPort = 8883;
const char* mqttUsername = "hivemq";
const char* mqttPassword = "XXXXXX";

EspMQTTClient client(ssid, password, mqttServer, mqttUsername, mqttPassword, "ESP32_Ping_Client_USER_A", mqttPort);

bool emotionReceived = false;
String partnerEmotion = "neutral";
String ownEmotion = "neutral";
int ownIntensity = 3;
int partnerIntensity = 3;

const float PRESSURE_THRESHOLD = 50.0;  // Increase threshold to reduce sensitivity
const int PRESSURE_SAMPLES = 3;  // Number of samples to average
float pressureSamples[PRESSURE_SAMPLES] = {0};
int sampleIndex = 0;
bool effectRunning = false;  // Flag for when effects are running

float lastPressure = 0;
unsigned long lastHugTime = 0;             // Stores last hug detection time
const unsigned long hugCooldownMs = 5000;  // 5-second cooldown between hugs
bool pendingNotification = false;          // track notification state
const int maxNotificationPulses = 5;       // number of times to pulse before stopping

// 🚀 Centralized Emotion Mapping
uint32_t getColorFromEmotion(String mood) {
  if (mood.equals("happy")) return pixels.gamma32(pixels.ColorHSV(42 * 256, 255, 255));     // Yellow 🌟
  if (mood.equals("excited")) return pixels.gamma32(pixels.ColorHSV(24576, 255, 255));   // Green 🍏
  if (mood.equals("calm")) return pixels.gamma32(pixels.ColorHSV(35000, 128, 255));     // Light Pastel Cyan 🌊 (soothing and serene)
  if (mood.equals("sad")) return pixels.gamma32(pixels.ColorHSV(49852, 255, 250));      // Deep Purple 💜 (dimmed for a melancholic feel)
  if (mood.equals("stressed")) return pixels.gamma32(pixels.ColorHSV(21 * 256, 255, 255));  // Orange 🍊 (intense and urgent)
  if (mood.equals("love")) return pixels.gamma32(pixels.ColorHSV(58950, 175, 200));        // Soft Magenta/Pink 💖 (romantic and warm)
  if (mood.equals("longing")) return pixels.gamma32(pixels.ColorHSV(255 * 256, 92, 255));  // Pink 💗 (nostalgic)
  if (mood.equals("anxious")) return pixels.gamma32(pixels.ColorHSV(10 * 256, 255, 255));   // Red-Orange 🔥
  return pixels.gamma32(pixels.ColorHSV(0, 0, 255));  // Default: White 🔲
}

// 🚀 Adjust Vibration Intensity Based on Emotion
void applyVibration(String mood) {
  int pulseCount = 0;
  int pulseDuration = 0;

  if (mood.equals("happy")) {
    pulseCount = 2;
    pulseDuration = 200;
  } else if (mood.equals("excited")) {
    pulseCount = 5;
    pulseDuration = 100;
  } else if (mood.equals("calm")) {
    pulseCount = 1;
    pulseDuration = 1500;
  } else if (mood.equals("sad")) {
    pulseCount = 2;
    pulseDuration = 700;
  } else if (mood.equals("stressed")) {
    pulseCount = 4;
    pulseDuration = 300;
  } else if (mood.equals("loving")) {
    pulseCount = 1;
    pulseDuration = 2000;
  } else if (mood.equals("longing")) {
    pulseCount = 3;
    pulseDuration = 1000;
  } else if (mood.equals("love")) {
    pulseCount = 1;
    pulseDuration = 2500;
  } else if (mood.equals("anxious")) {
    pulseCount = 8;
    pulseDuration = 300;  // Rapid, intense pulses
  } else if (mood.equals("comfort")) {
    pulseCount = 3;
    pulseDuration = 800;
  } else {
    pulseCount = 1;
    pulseDuration = 500;  // Default gentle pulse
  }

  for (int i = 0; i < pulseCount; i++) {
    digitalWrite(VIBRATION_PIN, HIGH);
    delay(pulseDuration);
    digitalWrite(VIBRATION_PIN, LOW);
    delay(200);
  }
}

uint32_t getReassuranceColorFromEmotion(String mood) {
  if (mood.equals("happy")) return pixels.gamma32(pixels.ColorHSV(42 * 256, 255, 255));     // Warm Yellow Glow
  if (mood.equals("excited")) return pixels.gamma32(pixels.ColorHSV(85 * 256, 255, 255));   // Playful Green Flickers
  if (mood.equals("calm")) return pixels.gamma32(pixels.ColorHSV(43520, 150, 255));     // Soft Breathing Blue
  if (mood.equals("sad")) return pixels.gamma32(pixels.ColorHSV(58950, 100, 200));      // Gentle Pink Comfort
  if (mood.equals("stressed")) return pixels.gamma32(pixels.ColorHSV(170 * 256, 150, 200));  // Stress → Calm Blue
  if (mood.equals("love")) return pixels.gamma32(pixels.ColorHSV(59950, 175, 200));         // Slow Red Heartbeat Pulse
  if (mood.equals("longing")) return pixels.gamma32(pixels.ColorHSV(255 * 256, 100, 255));  // Soft Pink Glow
  if (mood.equals("anxious")) return pixels.gamma32(pixels.ColorHSV(170 * 256, 150, 200));   // Anxiety → Calming Blue
  return pixels.gamma32(pixels.ColorHSV(0, 0, 255));  // Default: White 🔲
}


// reassurance effect
void applyReassuranceEffect(String mood) {
  Serial.println("Starting reassurance effect");
  uint32_t reassuranceColor = getReassuranceColorFromEmotion(mood);

    // Explicitly set brightness to maximum
  pixels.setBrightness(255);
  pixels.show();
  
  Serial.print("Using color for mood: ");
  Serial.println(mood);

  // Apply reassurance lighting effect (gentle pulsing)
 // Apply reassurance lighting effect (gentle pulsing)
  for (int i = 0; i < 3; i++) {
    Serial.print("Pulse ON: ");
    Serial.println(i+1);
    
    // Explicitly set each pixel instead of using fill
    for (int p = 0; p < NUM_PIXELS; p++) {
      pixels.setPixelColor(p, reassuranceColor);
    }
    pixels.show();
    delay(500);
    
    Serial.print("Pulse OFF: ");
    Serial.println(i+1);
    
    // Explicitly turn off each pixel
    for (int p = 0; p < NUM_PIXELS; p++) {
      pixels.setPixelColor(p, pixels.Color(0, 0, 0, 0));
    }
    pixels.show();
    delay(500);
  }
  
  Serial.println("Reassurance effect completed");

}



// 🚀 Apply Light Effect & Vibration
void applyLightAndVibration(String mood, int intensity) {
  uint32_t color = getColorFromEmotion(mood);

  // convert intensity (1-5) to brightness (20-255)
  int brightnessLevel = map(intensity,1,5,20,255);

  // Smooth fade-in effect
  for (int brightness = 0; brightness <= brightnessLevel; brightness += 5) {
    // pixels.fill(pixels.Color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, brightness));
    // pixels.show();
    // delay(10);
    pixels.setBrightness(brightness);
    pixels.fill(color);
    pixels.show();
    delay(10);
  }

  applyVibration(mood);

  // Smooth fade-out effect
  for (int brightness = 255; brightness >= 0; brightness -= 5) {
    // pixels.fill(pixels.Color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, brightness));
    // pixels.show();
    // delay(10);
    pixels.setBrightness(brightness);
    pixels.fill(color);
    pixels.show();
    delay(10);
  }
  // Ensure the lights turn off completely
  pixels.setBrightness(0);
  pixels.clear();
  pixels.show();
}

// 🚀 Smarter Hug Detection with Debounce
void detectHug() {
  float pressure_hPa = mpr.readPressure();
  unsigned long currentMillis = millis();

   // Add to rolling average
  pressureSamples[sampleIndex] = pressure_hPa;
  sampleIndex = (sampleIndex + 1) % PRESSURE_SAMPLES;

   // Calculate average pressure
  float avgPressure = 0;
  for (int i = 0; i < PRESSURE_SAMPLES; i++) {
    avgPressure += pressureSamples[i];
  }
  avgPressure /= PRESSURE_SAMPLES;

  // Calculate pressure change using the averaged values
  float pressureChange = avgPressure - lastPressure;

  // 📌 Response Hug (If Emotion was Sent)
  // Check if it's been at least `hugCooldownMs` milliseconds since the last hug
  // Only detect hugs if pressure change exceeds threshold
  if (pressureChange > PRESSURE_THRESHOLD && (currentMillis - lastHugTime > hugCooldownMs)) {
    if (emotionReceived) {
      Serial.println("📩 Partner responded with a hug!");
      client.publish("toys/abcd1234/hug", "hug_detected");  // response hug topic
      applyLightAndVibration(partnerEmotion, partnerIntensity);
      emotionReceived = false;
      pendingNotification = false;
    } else {
      // 📌 Spontaneous Hug (If No Emotion Was Posted)
      Serial.println("💜 Spontaneous hug detected!");
      client.publish("toys/abcd1234/spontaneous_hug", "abcd1234 sent spontaneous hug");
    }
    
    lastHugTime = currentMillis;  // Update last hug timestamp
  }

  // Update last pressure with a smoothing factor to avoid small fluctuations
  lastPressure = lastPressure * 0.7 + avgPressure * 0.3;
}

// 🚀 MQTT Message Handling
void onMessageReceived(const String& topic, const String& payload) {
  Serial.print("MQTT Message: ");
  Serial.print(topic);
  Serial.print(" - ");
  Serial.println(payload);

    

  if (topic.equals("toys/abcd1234/mood")) {
    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, payload);

    if (error) {
      Serial.println("❌ Failed to parse JSON");
      return;
    }

    ownEmotion = doc["emotion"].as<String>();
    ownIntensity = doc["intensity"].as<int>();

    Serial.print("Published own Emotion: ");
    Serial.print(ownEmotion);
    Serial.print(" | Intensity: ");
    Serial.println(ownIntensity);

    applyLightAndVibration(ownEmotion, ownIntensity);
    client.publish("toys/efgh5678/notify", "emotion_received");
  }

  if (topic.equals("toys/efgh5678/mood")) {
    StaticJsonDocument<200> doc;
    DeserializationError error = deserializeJson(doc, payload);

    if (error) {
      Serial.println("❌ Failed to parse JSON");
      return;
    }

    partnerEmotion = doc["emotion"].as<String>();
    partnerIntensity = doc["intensity"].as<int>();

    Serial.print("Published partner Emotion: ");
    Serial.print(partnerEmotion);
    Serial.print(" | Intensity: ");
    Serial.println(partnerIntensity);

    // partnerEmotion = payload;
  }

  if (topic.equals("toys/efgh5678/hug")) {
    Serial.println("Partner acknowledged, sending reassurance..");
    //applyLightAndVibration("comfort"); // need to change to reassurance effect
    Serial.print("Current own emotion: ");
    Serial.print(ownEmotion);
    applyReassuranceEffect(ownEmotion);
  }

  if (topic.equals("toys/abcd1234/notify")) {
    Serial.println("Partner (User B) sent an emotion");
    // applyNotificationPulse(5);  // subtle notification effect
    pendingNotification = true;
    emotionReceived = true;
  }

  if (topic.equals("toys/simultaneous_hug")) {
    Serial.println("❤️ Simultaneous hug detected! Special effect activated.");
    applySimultaneousHugEffect();
  }

  if (topic.equals("toys/abcd1234/tele_hug")) {
    Serial.println("Receieved hug from telegram!");
    teleHugEffect();
  }
}

void applySimultaneousHugEffect() {
  Serial.println("Starting simultaneous hug effect");
  uint32_t color = pixels.Color(255, 0, 255);  // Magenta for special effect
  for (int i = 0; i < 5; i++) {
    pixels.fill(color);
    pixels.show();
    delay(300);
    pixels.fill(0);
    pixels.show();
    delay(300);
  }
}

void teleHugEffect() {
  int pulseCount = 3;
  int pulseDuration = 500;

  for (int i = 0; i < pulseCount; i++) {
    digitalWrite(VIBRATION_PIN, HIGH);
    delay(pulseDuration);
    digitalWrite(VIBRATION_PIN, LOW);
    delay(200);
  }
}

void applyNotificationPulse(uint8_t wait) {

  static unsigned long lastPulseTime = 0;
  static int pulseCount = 0;

  if (!pendingNotification) return;

  if (pulseCount >= maxNotificationPulses) {
    Serial.println("⏳ No response detected, informing backend...");

    if (client.isConnected()) {
      Serial.println("✅ Publishing missed notification...");
      client.publish("toys/notifications/missed", "abcd1234");
      Serial.println("✅ Missed notification sent!");
    } else {
      Serial.println("⚠️ MQTT disconnected, reconnecting...");
    }

    pulseCount = 0;
    pendingNotification = false;
    emotionReceived = false;
    return;
  }

  unsigned long currentMillis = millis();
  if (currentMillis - lastPulseTime >= 5000) {  // Every 5 sec
    lastPulseTime = currentMillis;
    Serial.print("🔔 Pulse: ");
    Serial.println(pulseCount + 1);

    for (int j = 0; j < 256; j++) {  // Ramp up
      pixels.fill(pixels.Color(0, 0, 0, pixels.gamma8(j)));
      pixels.show();
    }
    for (int j = 255; j >= 0; j--) {  // Ramp down
      pixels.fill(pixels.Color(0, 0, 0, pixels.gamma8(j)));
      pixels.show();
    }

    pulseCount++;
  }
}


// 🚀 MQTT Connection Setup
void onConnectionEstablished() {
  Serial.println("Connected to MQTT!");
  client.subscribe("toys/abcd1234/mood", onMessageReceived);
  client.subscribe("toys/efgh5678/mood", onMessageReceived);
  client.subscribe("toys/efgh5678/hug", onMessageReceived);
  client.subscribe("toys/abcd1234/notify", onMessageReceived);
  client.subscribe("toys/simultaneous_hug", onMessageReceived);
  client.subscribe("toys/abcd1234/tele_hug", onMessageReceived);
}

void setup() {
  Serial.begin(115200);
  pinMode(VIBRATION_PIN, OUTPUT);
  pixels.begin();
  pixels.show();

  if (!mpr.begin()) {
    Serial.println("Failed to communicate with MPRLS sensor");
    while (1) { delay(10); }
  }
  Serial.println("MPRLS sensor found!");

  client.enableDebuggingMessages();
  client.enableHTTPWebUpdater();
  client.setKeepAlive(15);
  client.mWifiClient.setInsecure();
}

void loop() {
  client.loop();
  detectHug();

  if (pendingNotification) {
    applyNotificationPulse(5);
  }
}

