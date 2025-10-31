# TechWeeklyReport

- 技術系ニュース収集

## 概要

- TechWeeklyReportは、毎週の技術系ニュースを自動で収集しマークダウンファイルを生成します。

## 構成

```
├── .github/
│   └── workflows/
│       └── weekly_summary.yml  # Javaのビルドと実行コマンドに修正
├── src/
│   └── main/
│       ├── java/
│       │   └── com/techreport/common/
│       │       ├── logic/
│       │       │   ├── NewsFetcher.java
│       │       │   ├── NewsFetcherImpl.java
│       │       │   ├── LLMSummarizer.java
│       │       │   ├── LLMSummarizerImpl.java
│       │       │   ├── MarkdownGenerator.java
│       │       │   └── MarkdownGeneratorImpl.java
│       │       ├── model/
│       │       │   └── Article.java
│       │       ├── AppConfig.java
│       │       ├── LogicException.java
│       │       └── MainApplication.java
│       └── resources/
│           ├── application.properties
│           └── feed_list.json
├── reports/
│   └── Tech-News/
│       └── YYYY
│           └── MM/
│               └── tech_news_YYYY_MM_DD.md
├── build.gradle.kts
└── README.md
```
