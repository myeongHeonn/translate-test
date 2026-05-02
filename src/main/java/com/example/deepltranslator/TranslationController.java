package com.example.deepltranslator;

import com.deepl.api.DeepLException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/translate")
public class TranslationController {

    private final TranslationService translationService;

    // 재난 알림 샘플 메시지 (한국어)
    private static final Map<String, String> DISASTER_SAMPLES = new LinkedHashMap<>();

    static {
        DISASTER_SAMPLES.put("earthquake", "[긴급재난문자] 경상북도 포항시 규모 5.8 지진 발생. 안전한 곳으로 대피하고 여진에 대비하십시오.");
        DISASTER_SAMPLES.put("flood", "[긴급재난문자] 강원도 일대 집중호우로 홍수 경보 발령. 하천 및 저지대 주민은 즉시 대피하십시오.");
        DISASTER_SAMPLES.put("fire", "[긴급재난문자] 강릉시 산불 발생. 해당 지역 주민은 즉시 대피하고 화재 확산에 주의하십시오.");
        DISASTER_SAMPLES.put("typhoon", "[긴급재난문자] 태풍 카눈 북상 중. 외출을 자제하고 창문 및 시설물을 고정하십시오.");
    }

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    // 단일 번역
    @GetMapping
    public Map<String, String> translate(
            @RequestParam String text,
            @RequestParam(defaultValue = "KO") String targetLang
    ) throws DeepLException, InterruptedException {
        String translated = translationService.translate(text, targetLang);
        return Map.of(
                "original", text,
                "translated", translated,
                "targetLang", targetLang
        );
    }

    // 재난 알림 3개 언어 동시 번역 테스트
    @GetMapping("/disaster-test")
    public Map<String, Object> disasterTest(
            @RequestParam(defaultValue = "earthquake") String type
    ) throws DeepLException, InterruptedException {
        String original = DISASTER_SAMPLES.getOrDefault(type, DISASTER_SAMPLES.get("earthquake"));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("type", type);
        result.put("original_KO", original);
        result.put("EN", translationService.translate(original, "EN-US"));
        result.put("ZH", translationService.translate(original, "ZH-HANS"));
        result.put("JA", translationService.translate(original, "JA"));

        return result;
    }

    // 사용 가능한 재난 유형 목록
    @GetMapping("/disaster-types")
    public Map<String, String> disasterTypes() {
        return DISASTER_SAMPLES;
    }
}
