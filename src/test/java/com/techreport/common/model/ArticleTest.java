package com.techreport.common.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>{@link Article} のテストを行う。</p>
 *
 * <ul>
 *     <li>toString の正常系。</li>
 *     <li>getter 系の正常系。</li>
 *     <li>setter 系の正常系。</li>
 * </ul>
 */
public class ArticleTest {

    /**
     * <p>toString の正常系。</p>
     */
    @Test
    public void toStringSuccess01() {
        // 事前準備
        LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 1, 10, 30, 0);

        // Article の生成
        Article article = new Article(
                "ニューステスト",
                "https://abehiroshi.la.coocan.jp/",
                "ニュースサイト",
                localDateTime,
                "これはテストです。"
        );

        String expected = "Article{" +
                "newsTitle='ニューステスト', " +
                "url='https://abehiroshi.la.coocan.jp/', " +
                "siteName='ニュースサイト', " +
                "localDateTime=2025-01-01T10:30, " +
                "originalSummary='これはテストです。', " +
                "aiSummary='', " +
                "aiCategory=''}";

        // 検証
        assertEquals(expected, article.toString());

    }

    /**
     * <p>getter 系の正常系。</p>
     */
    @Test
    public void getterSuccess01() {
        // 事前準備
        LocalDateTime localDateTime = LocalDateTime.of(2025, 1, 1, 10, 30, 0);

        // Article の生成
        Article article = new Article(
                "ニューステスト",
                "https://abehiroshi.la.coocan.jp/",
                "ニュースサイト",
                localDateTime,
                "これはテストです。"
        );

        // 検証
        assertEquals("ニューステスト", article.getNewsTitle());
        assertEquals("https://abehiroshi.la.coocan.jp/", article.getUrl());
        assertEquals("ニュースサイト", article.getSiteName());
        assertEquals(localDateTime, article.getLocalDateTime());
        assertEquals("これはテストです。", article.getOriginalSummary());
        assertEquals("", article.getAiSummary());
        assertEquals("", article.getAiCategory());
    }

    @Test
    public void setterSuccess01() {

        // Article の生成
        Article article = new Article(
                "ニューステスト",
                "https://abehiroshi.la.coocan.jp/",
                "ニュースサイト",
                LocalDateTime.of(2025, 1, 1, 10, 30, 0),
                "これはテストです。"
        );

        // 実行
        article.setNewsTitle("更新ニュース");
        article.setUrl("https://ja.wikipedia.org/wiki/阿部寛のホームページ");
        article.setSiteName("ホームページ");
        article.setLocalDateTime(LocalDateTime.of(2026, 2, 2, 11, 31, 0));
        article.setOriginalSummary("映画出演情報");
        article.setAiSummary("AAA");
        article.setAiCategory("AI");

        String expected = "Article{" +
                "newsTitle='更新ニュース', " +
                "url='https://ja.wikipedia.org/wiki/阿部寛のホームページ', " +
                "siteName='ホームページ', " +
                "localDateTime=2026-02-02T11:31, " +
                "originalSummary='映画出演情報', " +
                "aiSummary='AAA', " +
                "aiCategory='AI'}";

        // 検証
        assertEquals(expected, article.toString());
    }

}