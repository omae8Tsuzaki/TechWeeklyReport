package com.techreport.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <p>アプリケーションで使用する設定値を定義したクラス。</p>
 */
@Configuration
public class AppConfig {
    // 環境変数からAPIキーを取得
    @Value("${techReport.openai.api-key}")
    public String openAIApiKey;

    @Value("${techReport.prompt-template}")
    private String promptTemplate;

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

    /**
     * <p>プロンプトテンプレートを取得する。</p>
     *
     * @return プロンプトテンプレート
     */
    public String getPromptTemplate() {
        return promptTemplate;
    }

    /**
     * <p>OpenAI の API Keyの取得。</p>
     *
     * @return API Key
     */
    public String getOpenAiApiKey() {
        return openAIApiKey;
    }

    /**
     * <p>RSSフィードリストのパスの取得。</p>
     *
     * @return フィードリストのパス
     */
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

    /**
     * <p>生成したマークダウンのレポートを保存するディレクトリの取得。</p>
     *
     * @return ディレクトリのパス
     */
    public String getReportDir() {
        return reportDir;
    }

    /**
     * <p>使用するLLMモデル名の取得。</p>
     *
     * @return モデル名
     */
    public String getLlmGpt(){
        return llmGpt;
    }

    /**
     * <p>レポートを保存するディレクトリの設定。</p>
     *
     * @param reportDir ディレクトリのパス
     */
    public void setReportDir(String reportDir) {
        this.reportDir = reportDir;
    }

}
