package com.techreport.common.model;

import java.time.LocalDateTime;

/**
 * <p>記事の情報を保持するクラス。</p>
 */
public class Article {

    // ニュースタイトル
    private String newsTitle = "";
    // サイトの URL
    private String url = "";
    // 記事の出典元のサイト
    private String siteName = "";
    // 記事の公開日時 (UTC)
    private LocalDateTime localDateTime = null;
    // 元の記事から取得した概要
    private String originalSummary = "";

    // AIによる処理の情報
    // AI による要約
    private String aiSummary = "";
    // AI が分類したカテゴリ
    private String aiCategory = "";

    /**
     * <p>デフォルトコンストラクタ。</p>
     */
    public Article() {
    }

    /**
     * <p>記事の基本情報を設定するコンストラクタ。</p>
     *
     * @param newsTitle ニュースタイトル
     * @param url サイトの URL
     * @param siteName 記事の出典元のサイト
     * @param localDateTime 記事の公開日時
     * @param originalSummary 元の記事から取得した概要
     */
    public Article(String newsTitle, String url, String siteName, LocalDateTime localDateTime, String originalSummary) {
        this.newsTitle = newsTitle;
        this.url = url;
        this.siteName = siteName;
        this.localDateTime = localDateTime;
        this.originalSummary = originalSummary;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public String getOriginalSummary() {
        return originalSummary;
    }

    public void setOriginalSummary(String originalSummary) {
        this.originalSummary = originalSummary;
    }

    public String getAiSummary() {
        return aiSummary;
    }

    public void setAiSummary(String aiSummary) {
        this.aiSummary = aiSummary;
    }

    public String getAiCategory() {
        return aiCategory;
    }

    public void setAiCategory(String aiCategory) {
        this.aiCategory = aiCategory;
    }

    @Override
    public String toString() {
        return "Article{" +
                "newsTitle='" + newsTitle + '\'' +
                ", url='" + url + '\'' +
                ", siteName='" + siteName + '\'' +
                ", localDateTime=" + localDateTime +
                ", originalSummary='" + originalSummary + '\'' +
                ", aiSummary='" + aiSummary + '\'' +
                ", aiCategory='" + aiCategory + '\'' +
                '}';
    }
}
