package example.ggApi;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.Scanner;

public class EnglishToVietnameseTranslator {

    private static final String API_KEY = "AIzaSyDcUp9EYX7nqduEfoqoLa-WiceAVPbzeXY";

    public static String translateText(String text, String targetLanguage) {
        Translate translate = TranslateOptions.newBuilder().setApiKey(API_KEY).build().getService();

        Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage(targetLanguage));
        return translation.getTranslatedText();
    }

//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("Enter the sentence to translate from English to Vietnamese:");
//        String textToTranslate = scanner.nextLine();
//
//        String translatedText = translateText(textToTranslate, "vi");
//
//        System.out.println("Original sentence (in English): " + textToTranslate);
//        System.out.println("Translated sentence (in English): " + translatedText);
//    }
}