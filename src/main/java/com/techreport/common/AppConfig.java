package com.techreport.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <p>アプリケーションで使用する設定値を定義したクラス。</p>
 */
@Configuration
public class AppConfig {
    // 環境変数からAPIキーを取得
    public static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");

    // RSSフィードリストのパス
    @Value("${techReport.feedList.path}")
    private String feedListPath;

    // レポートの間隔(1週間)
    @Value("${techReport.interval.days}")
    private long weeklyDays;

    // 生成したマークダウンのレポートを保存するディレクトリ
    @Value("${techReport.report.dir}")
    private String reportDir;

    // 使用するLLMモデル名(低コスト/高速なモデルを推奨)
    @Value("${techReport.llm.gpt}")
    private String llmGpt;

    public String getFeedListPath() {
        return feedListPath;
    }

    /**
     * <p>レポートの間隔。</p>
     *
     * @return 日付
     */
    public long getWeeklyDays() {
        return weeklyDays;
    }

    public String getReportDir() {
        return reportDir;
    }

    public String getLlmGpt(){
        return llmGpt;
    }

    public void setReportDir(String reportDir){
        this.reportDir = reportDir;
    }

}
