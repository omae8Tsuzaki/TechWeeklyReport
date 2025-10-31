package com.techreport.common.logic;

import com.techreport.common.AppConfig;
import com.techreport.common.model.Article;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>{@link MarkdownGenerator} の実装のテストを行う。</p>
 *
 * <ul>
 *     <li>{@link #saveMarkdownFileSuccess01} 正常系：マークダウンファイルの保存。</li>
 *     <li>{@link #generateWeeklyMarkdownSuccess01} 正常系：引数に null を渡した場合。</li>
 *     <li>{@link #generateWeeklyMarkdownSuccess02} 正常系：引数に空のエンティティを渡した場合。</li>
 *     <li>{@link #generateWeeklyMarkdownSuccess03} 正常系：記事リストを渡した場合。</li>
 * </ul>
 */
@SpringBootTest
public class MarkdownGeneratorImplTest {

    @Autowired
    MarkdownGenerator logic;
    @Autowired
    private AppConfig appConfig;
    // テスト用
    @TempDir
    private File temporaryFolder;
    // 日付フォーマット
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * <p>マークダウンファイルを保存するメソッドの正常系。</p>
     *
     * @throws Exception 例外が発生した場合
     */
    @Test
    public void saveMarkdownFileSuccess01() throws Exception {

        //
        // 事前準備
        //

        // テスト用のマークダウン文字列
        String markdownString = "# テスト";
        // 期待されるファイル名
        String expectedFileName = LocalDateTime.now().format(DATE_FORMATTER) + "-weekly-summary.md";
        // テスト用の保存先ディレクトリを設定
        appConfig.setReportDir(temporaryFolder.getAbsolutePath());

        //
        // 実行
        //

        logic.saveMarkdownFile(markdownString);

        //
        // 検証
        //

        File expectedFile = new File(temporaryFolder, expectedFileName);
        assertTrue(expectedFile.exists());
    }

    /**
     * <p>正常系：引数に null を渡した場合。</p>
     */
    @Test
    public void generateWeeklyMarkdownSuccess01() {

        //
        // 実行・検証
        //

        assertNull(logic.generateWeeklyMarkdown(null));
    }

    /**
     * <p>正常系：引数に空のエンティティを渡した場合。</p>
     */
    @Test
    public void generateWeeklyMarkdownSuccess02() {
        List<Article> articles = new ArrayList<>();

        //
        // 実行・検証
        //

        assertNull(logic.generateWeeklyMarkdown(articles));
    }

    /**
     * <p>正常系：記事リストを渡した場合。</p>
     */
    @Test
    public void generateWeeklyMarkdownSuccess03() {

        //
        // 事前準備
        //
        LocalDateTime localDateTime = LocalDateTime.of(2024, 6, 1, 10, 0, 0);

        Article article01 = new Article(
                "ニュースタイトル01",
                "http://example.com/news1",
                "サイト名01",
                localDateTime,
                "オリジナルの要約01"
        );
        article01.setAiSummary("AIによる要約01");
        article01.setAiCategory("AI");

        Article article02 = new Article(
                "ニュースタイトル02",
                "http://example.com/news2",
                "サイト名02",
                localDateTime,
                "オリジナルの要約02"
        );
        article02.setAiSummary("AIによる要約02");
        article02.setAiCategory("AI");

        List<Article> articleList = new ArrayList<>();
        articleList.add(article01);
        articleList.add(article02);

        //
        // 実行
        //
        Map.Entry<String, Path> result = logic.generateWeeklyMarkdown(articleList);

        //
        // 検証
        //
        assertEquals(
                """
                        ---
                        date: 2024-06-01
                        tags: weekly-report, tech-news, auto-generated
                        ---
                        
                        # 📅 2024-06-01 週の技術系ニュースまとめ
                        
                        ## 概要
                        
                        - 過去1週間（2件）の主要な技術系ニュースをまとめました。
                        
                        ---
                        
                        ## 記事一覧 (カテゴリ別)
                        
                        ## 🚀 AI
                        
                        ### 🌐 ニュースタイトル01
                        
                        - **カテゴリ**: `AI`
                        - **公開日**: 2024-06-01 19:00 (サイト名01)
                        - **URL**: [記事を読む](http://example.com/news1)
                        - 📰 **AI要約**: AIによる要約01
                        - 💡 **自分のコメント欄**:
                        
                        ### 🌐 ニュースタイトル02
                        
                        - **カテゴリ**: `AI`
                        - **公開日**: 2024-06-01 19:00 (サイト名02)
                        - **URL**: [記事を読む](http://example.com/news2)
                        - 📰 **AI要約**: AIによる要約02
                        - 💡 **自分のコメント欄**:
                        
                        """,
                result.getKey());

        assertEquals("2024-06-01-weekly-summary.md", result.getValue().getFileName().toString());
    }
}