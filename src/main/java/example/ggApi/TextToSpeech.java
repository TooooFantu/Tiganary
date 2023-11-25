package example.ggApi;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TextToSpeech {
    private Voice voice;

    // Hàm tạo mặc định
    public TextToSpeech() {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice("kevin16");
        if (voice == null) {
            throw new IllegalStateException("Cannot find voice: kevin16");
        }
        voice.allocate();
    }

    // Hàm tạo mới để chấp nhận tham số String
    public TextToSpeech(String text) {
        this(); // Gọi hàm tạo mặc định để thiết lập giọng đọc tiếng Anh
        speak(text);
    }

    public void speak(String text) {
        voice.speak(text);
    }

        // Example usage
//        TextToSpeech englishTTS = new TextToSpeech();
//        englishTTS.speak("Account");
}
