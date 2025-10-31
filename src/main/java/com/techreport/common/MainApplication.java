package com.techreport.common;

import com.techreport.common.logic.LLMSummarizer;
import com.techreport.common.logic.MarkdownGenerator;
import com.techreport.common.logic.NewsFetcher;
import com.techreport.common.model.Article;
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
        System.out.println("--- Starting Weekly News Summary Pipeline ---");

        try {
            // 1. 記事収集
            List<Article> articles = newsFetcher.fetchRecentArticles();

            if (articles.isEmpty()) {
                System.out.println("No recent articles found. Exiting.");
                return;
            }

            // 2. AIによる要約とカテゴリ分類
            System.out.printf("Starting AI summarization for %d articles...%n", articles.size());
            List<Article> processedArticles = llmSummarizer.processArticles(articles);

            // 3. Markdown生成と保存
            Map.Entry<String, Path> reportEntry = markdownGenerator.generateWeeklyMarkdown(processedArticles);

            if (reportEntry != null) {
                markdownGenerator.saveMarkdownFile(reportEntry.getKey(), reportEntry.getValue());
            }

        } catch (IOException e) {
            System.err.println("File I/O Error during pipeline execution: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("--- Pipeline Execution Finished ---");
        }
    }
}
