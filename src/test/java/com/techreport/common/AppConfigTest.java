package com.techreport.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>{@link AppConfig} のテストを行う。</p>
 * <ul>
 *     <li>{@link #getterSuccess01()} getter の正常系。</li>
 *     <li>{@link #setterSuccess01()} setter の正常系。</li>
 * </ul>
 */
@SpringBootTest
@ContextConfiguration(classes = {AppConfig.class})
public class AppConfigTest {

    @Autowired
    private AppConfig config;

    /**
     * <p>getter の正常系。</p>
     */
    @Test
    public void getterSuccess01() {

        //
        // 実行・検証
        //

        assertEquals(
                """
                        あなたはプロの技術系ニュース編集者です。以下のニュース記事の情報を基に、以下の2点をJSON形式で出力してください。
                        1. **要約 (summary)**: 記事の要点を100字以内の日本語で簡潔にまとめてください。
                        2. **カテゴリ (category)**: 記事が最も関連する技術カテゴリを一つだけ選択してください。選択肢: AI/ML, Cloud/Infrastructure, Programming/Frontend, Security, Hardware/IoT, Business/Policy, RAG, MCP, AI駆動開発, その他。
                        
                        ---
                        記事タイトル: {title}
                        記事概要（または一部本文）: {summary_excerpt}
                        ---""", config.getPromptTemplate());
        assertEquals("dummy-key", config.getOpenAiApiKey());
        assertEquals("src/test/resources/feed_list_test.json", config.getFeedListPath());
        assertEquals(3, config.getWeeklyDays());
        assertEquals("reports/Tech-News", config.getReportDir());
        assertEquals("gpt-4o-mini-2024-07-18", config.getLlmGpt());
    }

    /**
     * <p>setter の正常系。</p>
     */
    @Test
    public void setterSuccess01() {

        //
        // 事前準備
        //

        String testDir = "test/dir";
        config.setReportDir(testDir);
        String openaiKey = "openai-key-test";
        config.setOpenAIApiKey(openaiKey);

        //
        // 実行・検証
        //

        assertEquals(testDir, config.getReportDir());
        assertEquals(openaiKey, config.getOpenAiApiKey());
    }
}