package com.techreport.common.logic;

import com.techreport.common.AppConfig;
import com.techreport.common.model.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>{@link LLMSummarizer} の実装のテストを行う。</p>
 *
 * <h4>{@link LLMSummarizer#processArticles} メソッド</h4>
 * <ul>
 *     <li>{@link #processArticlesSuccess01} 正常系：openai.api.keyが null の場合。</li>
 *     <li>{@link #processArticlesSuccess02} 正常系：openai.api.keyが 空文字 の場合。</li>
 * </ul>
 */
@SpringBootTest
public class LLMSummarizerImplTest {

    @Autowired
    LLMSummarizer logic;
    @Autowired
    private AppConfig appConfig;

    /**
     * <p>正常系：openai.api.keyが null の場合。</p>
     *
     * @throws Exception 例外が発生した場合
     */
    @Test
    public void processArticlesSuccess01() throws Exception {

        //
        // 事前準備
        //

        LocalDateTime localDateTime = LocalDateTime.of(2024, 6, 1, 10, 0, 0);
        Article article = new Article(
                "ニュースタイトル01",
                "http://example.com/news1",
                "サイト名01",
                localDateTime,
                "オリジナルの要約01"
        );
        ArrayList<Article> articleList = new ArrayList<>();
        articleList.add(article);

        // APIキーをnullに設定
        appConfig.setOpenAIApiKey(null);

        //
        // 実行
        //

        List<Article> result = logic.processArticles(articleList);

        //
        // 検証
        //

        assertEquals(
                "[Article{" +
                        "newsTitle='ニュースタイトル01', " +
                        "url='http://example.com/news1', " +
                        "siteName='サイト名01', " +
                        "localDateTime=2024-06-01T10:00, " +
                        "originalSummary='オリジナルの要約01', " +
                        "aiSummary='', " +
                        "aiCategory=''}]"
                , result.toString());
    }

    /**
     * <p>正常系：openai.api.keyが 空文字 の場合。</p>
     *
     * @throws Exception 例外が発生した場合
     */
    @Test
    public void processArticlesSuccess02() throws Exception {

        //
        // 事前準備
        //

        LocalDateTime localDateTime = LocalDateTime.of(2024, 6, 1, 10, 0, 0);
        Article article = new Article(
                "ニュースタイトル01",
                "http://example.com/news1",
                "サイト名01",
                localDateTime,
                "オリジナルの要約01"
        );
        ArrayList<Article> articleList = new ArrayList<>();
        articleList.add(article);

        // API キーを空文字に設定
        appConfig.setOpenAIApiKey("");

        //
        // 実行
        //

        List<Article> result = logic.processArticles(articleList);

        //
        // 検証
        //

        assertEquals(
                "[Article{" +
                        "newsTitle='ニュースタイトル01', " +
                        "url='http://example.com/news1', " +
                        "siteName='サイト名01', " +
                        "localDateTime=2024-06-01T10:00, " +
                        "originalSummary='オリジナルの要約01', " +
                        "aiSummary='', " +
                        "aiCategory=''}]"
                , result.toString());
    }

}