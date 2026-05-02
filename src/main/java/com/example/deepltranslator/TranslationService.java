package com.example.deepltranslator;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    private final Translator translator;

    public TranslationService(@Value("${deepl.api.key}") String apiKey) {
        this.translator = new Translator(apiKey);
    }

    public String translate(String text, String targetLang) throws DeepLException, InterruptedException {
        TextResult result = translator.translateText(text, null, targetLang);
        return result.getText();
    }
}
