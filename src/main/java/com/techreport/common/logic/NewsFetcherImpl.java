package com.techreport.common.logic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.techreport.common.AppConfig;
import com.techreport.common.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class NewsFetcherImpl implements NewsFetcher {

    // ログ出力の設定
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsFetcherImpl.class);
    // アプリケーション設定
    private final AppConfig appConfig;
    // JSON処理用
    private final ObjectMapper objectMapper;
    // HTTPクライアント
    private final HttpClient httpClient;

    /**
     * <p>コンストラクタ。</p>
     *
     * @param appConfig アプリケーション設定
     * @param objectMapper JSON処理用オブジェクトマッパー
     */
    public NewsFetcherImpl(AppConfig appConfig, ObjectMapper objectMapper) {
        this.appConfig = appConfig;
        this.objectMapper = objectMapper;
        // HttpClientはスレッドセーフなので、一度作成して再利用する
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * <p>feed_list.jsonからフィード情報をロードする。</p>
     *
     * @param filePath フィードリストJSONファイルのパス
     * @return フィード情報のリスト
     */
    List<Map<String, String>> loadFeedList(String filePath) {
        try {
            // クラスパス（GitHub Actions実行環境）またはファイルシステムからロード
            return objectMapper.readValue(new File(filePath), new TypeReference<>() {});
        } catch (IOException e) {
            LOGGER.error("Error loading feed list from {}: {}", filePath, e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * 複数のRSSフィードから過去N日間の記事を取得し、重複を排除して返します。
     *
     * @return 過去N日間に公開されたユニークな記事のリスト
     */
    @Override
    public List<Article> fetchRecentArticles() {
        List<Map<String, String>> feedList = loadFeedList(appConfig.getFeedListPath());
        if (feedList.isEmpty()) {
            return Collections.emptyList();
        }

        Instant oneWeekAgo = Instant.now().minus(appConfig.getWeeklyDays(), ChronoUnit.DAYS);
        Set<Article> allArticles = new HashSet<>();
        SyndFeedInput input = new SyndFeedInput();

        LOGGER.info("Fetching articles from {}...", oneWeekAgo);

        for (Map<String, String> feedInfo : feedList) {
            String feedUrl = feedInfo.get("url");
            String feedName = feedInfo.get("name");

            try {
                // 1. HttpClientを使用してフィードコンテンツを取得
                HttpRequest request = HttpRequest.newBuilder(URI.create(feedUrl))
                        .header("Accept", "application/xml, application/rss+xml, application/atom+xml")
                        .timeout(java.time.Duration.ofSeconds(10))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() != 200) {
                    throw new IOException("Failed to fetch feed. Status code: " + response.statusCode());
                }

                // 2. 取得した文字列をStringReaderを介してRomeに渡す
                try (StringReader reader = new StringReader(response.body())) {
                    SyndFeed feed = input.build(reader);
                    System.out.println("  - Fetched: " + feedName);

                    for (SyndEntry entry : feed.getEntries()) {
                        Date publishedDate = entry.getPublishedDate();

                        if (publishedDate == null || publishedDate.toInstant().isBefore(oneWeekAgo)) {
                            continue;
                        }

                        // ZonedDateTime -> LocalDateTime への変換
                        LocalDateTime publishedLdt = publishedDate.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();

                        Article article = new Article(
                                entry.getTitle(),
                                entry.getLink(),
                                feedName,
                                publishedLdt,
                                entry.getDescription() != null ? entry.getDescription().getValue() : ""
                        );

                        allArticles.add(article); // HashSetにより重複排除（要Articleのequals/hashCode実装）
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Network/IO Error for feed " + feedName + ": " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Parsing Error for feed " + feedName + ": " + e.getMessage());
            }
        }

        List<Article> result = new ArrayList<>(allArticles);
        System.out.println("Total unique articles collected: " + result.size());
        return result;
    }
}
