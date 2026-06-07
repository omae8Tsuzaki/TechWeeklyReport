# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## 言語設定

すべての応答は日本語で行ってください。

## プロジェクト概要

TechWeeklyReport は、RSS フィードから技術系ニュースを自動収集し、OpenAI API を使って記事を要約し、週次レポートを Markdown 形式で生成する Java/Spring Boot アプリケーションです。GitHub Actions で毎週実行され、生成されたレポートをリポジトリにコミットします。

## ビルドコマンド

### ビルドとパッケージング

```bash
./gradlew bootJar
```

アプリケーションをビルドし、`build/libs/` に実行可能なJARファイルを生成します。

### アプリケーションの実行

```bash
java -jar build/libs/*.jar
```

環境変数 `OPENAI_API_KEY` の設定が必要です。

### テストの実行

```bash
./gradlew test
```

### 単一テストの実行

```bash
./gradlew test --tests "com.techreport.common.logic.NewsFetcherImplTest"
```

## アーキテクチャ

### パイプラインフロー

アプリケーションは `MainApplication.java:42` で制御される3段階のパイプラインアーキテクチャに従っています：

1. **記事取得** (`NewsFetcherImpl`)
   - `src/main/resources/feed_list.json` から RSS フィードの URL を読み込む
   - 過去7日間の記事を取得（`techReport.interval.days` で設定可能）
   - Rome ライブラリで RSS/Atom をパース
   - 重複排除された `Article` オブジェクトを返す

2. **AI 処理** (`LLMSummarizerImpl`)
   - 各記事に対して OpenAI API を呼び出す
   - 日本語で要約を生成（最大100文字）
   - 事前定義されたカテゴリに記事を分類（AI/ML, Cloud/Infrastructure, Programming/Frontend, Security, Hardware/IoT, Business/Policy, RAG, MCP, AI駆動開発, その他）
   - デフォルトで `gpt-4o-mini` を使用（`techReport.llm.gpt` で設定可能）

3. **Markdown 生成** (`MarkdownGeneratorImpl`)
   - カテゴリと公開日で記事をソート
   - UTC タイムスタンプを JST に変換して表示
   - YAML フロントマターを含む Markdown を生成
   - `reports/Tech-News/YYYY/MM/YYYY-MM-DD-weekly-summary.md` にレポートを保存

### 主要な設計パターン

- **コンストラクタインジェクション**: すべてのサービスはコンストラクタベースの依存性注入を使用
- **インターフェース分離**: ロジックインターフェース（`NewsFetcher`, `LLMSummarizer`, `MarkdownGenerator`）が契約と実装を分離
- **単一責任**: 各サービスクラスはパイプラインの1段階のみを処理

### データモデル

`Article` (com.techreport.common.model.Article:8) が中心的なデータモデルで、以下を含みます：
- 基本メタデータ: title, URL, siteName, localDateTime (UTC)
- 元の RSS 要約
- AI 生成の要約とカテゴリ

### 設定

すべての設定は `AppConfig.java:10` に集約され、`application.properties` から読み込まれます：
- `techReport.openai.api-key`: 環境変数 `OPENAI_API_KEY` から注入
- `techReport.prompt-template`: 記事要約用の日本語プロンプトテンプレート
- `techReport.feedList.path`: RSS フィードリスト JSON のパス
- `techReport.interval.days`: 記事収集期間（デフォルト: 7日間）
- `techReport.report.dir`: 生成されたレポートの出力ディレクトリ
- `techReport.llm.gpt`: 使用する OpenAI モデル

### 時刻の取り扱い

アプリケーションは特定の時刻変換戦略を使用しています：
- RSS フィードの時刻は UTC としてパース
- `Article.localDateTime` に `LocalDateTime` として保存（タイムゾーン情報なし）
- Markdown レポート表示時のみ JST（Asia/Tokyo）に変換
- この処理は `MarkdownGeneratorImpl:208` と `MarkdownGeneratorImpl:146` で実装

## テスト

テストは `src/test/java/com/techreport/common/` 配下にクラスごとに整理されています：
- ロジックテストは Spring Boot Test フレームワークを使用
- テストリソースには RSS フィードパーステスト用の `feed_list_test.json` を含む

## GitHub Actions ワークフロー

`.github/workflows/weekly_summary.yml` ワークフロー：
- 毎週日曜日の22:00 JST（13:00 UTC）に実行
- `./gradlew bootJar -x test` で JAR をビルド
- `OPENAI_API_KEY` シークレットを使用してアプリケーションを実行
- 生成されたレポートを `reports/**/*.md` にコミット
- 自動コミットに `stefanzweifel/git-auto-commit-action@v5` を使用

## 新しいRSSフィードの追加

`src/main/resources/feed_list.json` を以下の構造で編集してください：
```json
[
  {
    "name": "Site Name",
    "url": "https://example.com/feed.xml"
  }
]
```

## 重要な注意事項

- アプリケーションは OpenAI API のレスポンスが JSON 形式で `summary` と `category` フィールドを含むことを想定
- `application.properties` のプロンプトテンプレートは Unicode エスケープシーケンスでエンコードされている
- ファイル書き込みには権限エラーに対応するフォールバックロジックがある（`~/reports/Tech-News/` にフォールバック）
- アプリケーションは RSS 取得と OpenAI API 呼び出しの両方に `HttpClient` (Java 11+) を使用
