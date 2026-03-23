package com.techreport.common;

import com.techreport.common.logic.LLMSummarizer;
import com.techreport.common.logic.MarkdownGenerator;
import com.techreport.common.logic.NewsFetcher;
import com.techreport.common.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * <p>アプリケーションを起動するメインクラス。</p>
 */
@SpringBootApplication(scanBasePackages = "com.techreport.common")
public class MainApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    // ロジックをコンストラクタインジェクションで取得
    private final NewsFetcher newsFetcher;
    private final LLMSummarizer llmSummarizer;
    private final MarkdownGenerator markdownGenerator;

    public MainApplication(NewsFetcher newsFetcher, LLMSummarizer llmSummarizer, MarkdownGenerator markdownGenerator) {
        this.newsFetcher = newsFetcher;
        this.llmSummarizer = llmSummarizer;
        this.markdownGenerator = markdownGenerator;
    }

    public static void main(String[] args) {
        // Spring Bootアプリケーションを起動
        SpringApplication.run(MainApplication.class, args);
    }

    /**
     * <p>アプリケーション起動後の実行ロジック。</p>
     */
    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("--- Starting Weekly News Summary Pipeline ---");

        try {
            // 1. 記事収集
            List<Article> articles = newsFetcher.fetchRecentArticles();

            if (articles.isEmpty()) {
                LOGGER.error("No recent articles found. Exiting.");
                return;
            }

            // 2. AIによる要約とカテゴリ分類
            LOGGER.info("Starting AI summarization for {} articles..." , articles.size());
            List<Article> processedArticles = llmSummarizer.processArticles(articles);

            // 3. Markdown生成と保存
            Map.Entry<String, Path> reportEntry = markdownGenerator.generateWeeklyMarkdown(processedArticles);

            if (reportEntry != null) {
                markdownGenerator.saveMarkdownFile(reportEntry.getKey(), reportEntry.getValue());
            }

        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred: {}",  e.getMessage());
        } finally {
            LOGGER.info("--- Pipeline Execution Finished ---");
        }
    }
}
