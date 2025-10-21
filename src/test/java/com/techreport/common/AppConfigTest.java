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
        // 実行
        String reportDir = config.getReportDir();
        String openai = config.getLlmGpt();


        // 検証
        assertEquals("reports/Tech-News", reportDir);
        assertEquals("gpt-4o-mini-2024-07-18", openai);

    }
}