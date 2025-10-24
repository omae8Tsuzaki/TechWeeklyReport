package com.techreport.common.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreport.common.AppConfig;
import com.techreport.common.model.Article;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class LLMSummarizerImpl implements LLMSummarizer {

    private final AppConfig appConfig;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";


    public LLMSummarizerImpl(AppConfig appConfig, ObjectMapper objectMapper) {
        this.appConfig = appConfig;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * 記事リストを順次処理し、AI要約とカテゴリ分類を追加します。
     * @param articles 処理前の記事リスト
     * @return 処理後の記事リスト
     */
    @Override
    public List<Article> processArticles(List<Article> articles) {
        if (appConfig.getOpenAiApiKey() == null || appConfig.getOpenAiApiKey().isEmpty()) {
            System.err.println("Warning: OpenAI API Key is missing. Skipping LLM summarization.");
            return articles;
        }

        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);

            if (i > 0 && i % 10 == 0) {
                System.out.printf("  - Processing article %d/%d...%n", i + 1, articles.size());
            }

            try {
                Map<String, String> aiResult = summarizeArticle(article);
                article.setAiSummary(aiResult.getOrDefault("summary", article.getOriginalSummary().substring(0, Math.min(200, article.getOriginalSummary().length())) + "..."));
                article.setAiCategory(aiResult.getOrDefault("category", "その他"));

            } catch (Exception e) {
                System.err.printf("  - AI summarization failed for '%s': %s%n", article.getNewsTitle(), e.getMessage());
                article.setAiSummary(article.getOriginalSummary().substring(0, Math.min(200, article.getOriginalSummary().length())) + "...");
                article.setAiCategory("要約エラー");
            }
        }
        return articles;
    }

    private Map<String, String> summarizeArticle(Article article) throws IOException, InterruptedException {
        String prompt = appConfig.getPromptTemplate()
                .replace("{title}", article.getNewsTitle())
                .replace("{summary_excerpt}", article.getAiSummary().substring(0, Math.min(500, article.getAiSummary().length())));

        // 1. リクエストボディの構築 (Jacksonを使用)
        String requestBody = objectMapper.writeValueAsString(Map.of(
                "model", appConfig.getLlmGpt(),
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful assistant that responds only in JSON format."),
                        Map.of("role", "user", "content", prompt)
                )
        ));

        // 2. HTTPリクエストの構築
        HttpRequest request = HttpRequest.newBuilder(URI.create(OPENAI_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + appConfig.getOpenAiApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .timeout(Duration.ofSeconds(30))
                .build();

        // 3. APIコールの実行
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("API call failed with status " + response.statusCode() + ": " + response.body());
        }

        // 4. JSONレスポンスのパース
        // レスポンス構造: { choices: [{ message: { content: "..." } }] }
        Map<String, Object> responseMap = objectMapper.readValue(response.body(), new TypeReference<>() {});

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");

        if (choices == null || choices.isEmpty()) {
            throw new IOException("API response has no choices.");
        }

        @SuppressWarnings("unchecked")
        Map<String, String> message = (Map<String, String>) choices.getFirst().get("message");
        String content = message.get("content");

        // LLMが出力したJSON文字列を最終的にパース
        return objectMapper.readValue(content, new TypeReference<>() {});
    }
}
