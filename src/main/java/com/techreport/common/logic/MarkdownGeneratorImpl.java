package com.techreport.common.logic;

import com.techreport.common.AppConfig;
import com.techreport.common.LogicException;
import com.techreport.common.model.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Markdown生成用ロジックの実装クラス。</p>
 */
@Service
public class MarkdownGeneratorImpl implements MarkdownGenerator {

    @Autowired
    private AppConfig config;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final Logger LOGGER = Logger.getLogger(MarkdownGeneratorImpl.class.getName());

    // コンストラクタ（Springインジェクションを想定）
    public MarkdownGeneratorImpl() {
    }

    /**
     * <p>Markdown ファイルの保存。</p>
     *
     * @param markdownContent マークダウンファイルに出力するテキスト
     * @throws LogicException 例外が発生した場合
     */
    @Override
    public void saveMarkdownFile(String markdownContent) throws LogicException {

        try {
            // 保存先ディレクトリを確認・作成
            File dir = new File(config.getReportDir());
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成する Markdown ファイル名
            String fileName = LocalDateTime.now().format(DATE_FORMATTER) + "-weekly-summary.md";
            File output = new File(dir, fileName);

            // ファイルに書き込み (UTF-8)
            Files.writeString(output.toPath(), markdownContent);

            LOGGER.log(Level.INFO, "ファイルの書き込み成功");
        } catch (IOException e) {
            throw new LogicException("Failed to save markdown", e);
        }
    }

    /**
     * <p>生成された　Markdownファイルの保存。</p>
     *
     * @param markdownContent マークダウンファイルに出力するテキスト
     * @param savePath 保存先のパス
     */
    @Override
    public void saveMarkdownFile(String markdownContent, Path savePath) throws IOException {

        try {
            Path parent = savePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            // 既存ファイルが存在して読み取り専用なら属性解除を試みる
            File file = savePath.toFile();
            if (file.exists() && !file.canWrite()) {
                try {
                    // Windows の DOS 属性を試す（対応していないファイルシステムでは例外になる）
                    Files.setAttribute(savePath, "dos:readonly", Boolean.FALSE);
                } catch (UnsupportedOperationException | IOException ignored) {
                    // フォールバック: File#setWritable
                    file.setWritable(true);
                }
            }

            // ファイル書き込み
            Files.writeString(savePath, markdownContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            LOGGER.info("Saved report to " + savePath.toAbsolutePath());

        } catch (AccessDeniedException ade) {
            System.err.println("Permission denied writing to " + savePath + ": " + ade.getMessage());
            // フォールバック: ユーザーホーム配下へ保存を試みる
            try {
                Path fallbackDir = Paths.get(System.getProperty("user.home"), "reports", "Tech-News");
                Files.createDirectories(fallbackDir);
                Path fallbackFile = fallbackDir.resolve(savePath.getFileName());
                Files.writeString(fallbackFile, markdownContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                System.out.println("Saved report to fallback path " + fallbackFile.toAbsolutePath());
            } catch (IOException ex) {
                throw new RuntimeException("Failed to save markdown to fallback path", ex);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save markdown", e);
        }
    }

    /**
     * <p>収集した全記事をまとめて週次レポートのMarkdownテキストを生成します。</p>
     *
     * @param articles 処理済みの記事リスト
     * @return 生成されたMarkdownコンテンツと保存パスのペア (Map.Entry)
     */
    @Override
    public Map.Entry<String, Path> generateWeeklyMarkdown(List<Article> articles) {
        if (articles == null || articles.isEmpty()) {
            LOGGER.info("No articles to generate report.");
            return null;
        }

        // 記事をカテゴリと公開日でソート
        List<Article> sortedArticles = articles.stream()
                .sorted(Comparator
                        .<Article, String>comparing(a -> a.getAiCategory() != null ? a.getAiCategory() : "その他")
                        .thenComparing(Article::getLocalDateTime).reversed()
                )
                .toList();

        // レポートの日付を決定 (最も新しい記事のLocalDateTimeを基準)
        LocalDateTime latestLdt = sortedArticles.stream()
                .map(Article::getLocalDateTime)
                .max(Comparator.naturalOrder())
                .orElseGet(LocalDateTime::now);

        // レポートの日付を決定 (最も新しい記事の日付をJSTに変換)
        ZonedDateTime reportDateJST = latestLdt
                .atZone(ZoneOffset.UTC) // LocalDateTimeをUTCの日時として扱う
                .withZoneSameInstant(ZoneId.of("Asia/Tokyo")); // JSTに変換

        // ファイル名とディレクトリパスの決定
        String filename = reportDateJST.format(DATE_FORMATTER) + "-weekly-summary.md";
        String yearDir = reportDateJST.format(DateTimeFormatter.ofPattern("yyyy"));
        String monthDir = reportDateJST.format(DateTimeFormatter.ofPattern("MM"));

        Path savePath = Paths.get(config.getReportDir(), yearDir, monthDir, filename);

        // Markdownコンテンツ生成
        StringBuilder markdownContent = new StringBuilder();

        // 1. YAMLフロントマター
        markdownContent.append(String.format("""
                ---
                date: %s
                tags: weekly-report, tech-news, auto-generated
                ---
                
                # 📅 %s 週の技術系ニュースまとめ
                
                ## 概要
                
                - 過去1週間（%d件）の主要な技術系ニュースをまとめました。
                
                ---
                
                ## 記事一覧 (カテゴリ別)
                """,
                reportDateJST.format(DATE_FORMATTER),
                reportDateJST.format(DATE_FORMATTER),
                articles.size()
        ));

        // 2. 記事のカテゴリ別リスト
        String currentCategory = null;
        for (Article article : sortedArticles) {
            String category = article.getAiCategory() != null ? article.getAiCategory() : "その他";

            if (!category.equals(currentCategory)) {
                markdownContent.append(String.format("\n## 🚀 %s\n\n", category));
                currentCategory = category;
            }

            markdownContent.append(formatArticle(article));
        }

        return Map.entry(markdownContent.toString(), savePath);
    }

    /**
     * <p>単一の記事をMarkdown形式でフォーマットします。</p>
     *
     * @param article 記事データオブジェクト
     * @return Markdown形式の文字列
     */
    private String formatArticle(Article article) {
        String category = article.getAiCategory() != null ? article.getAiCategory() : "その他";

        // ArticleのLocalDateTime (UTC相当) をJSTに変換して表示
        ZonedDateTime publishedJST = article.getLocalDateTime()
                .atZone(ZoneOffset.UTC) // UTCとして扱う
                .withZoneSameInstant(ZoneId.of("Asia/Tokyo")); // JSTに変換

        return String.format("""
                ### 🌐 %s
                
                - **カテゴリ**: `%s`
                - **公開日**: %s (%s)
                - **URL**: [記事を読む](%s)
                - 📰 **AI要約**: %s
                - 💡 **自分のコメント欄**:
                
                """,
                article.getNewsTitle(),
                category,
                publishedJST.format(DATETIME_FORMATTER),
                article.getSiteName(),
                article.getUrl(),
                article.getAiSummary().trim()
        );
    }
}
