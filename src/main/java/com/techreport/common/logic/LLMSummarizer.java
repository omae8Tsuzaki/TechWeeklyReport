package com.techreport.common.logic;

import com.techreport.common.model.Article;

import java.util.List;

/**
 * <p>AIを使用して記事の要約とカテゴリ分類を行うロジックのインターフェース。</p>
 */
public interface LLMSummarizer {

    public List<Article>  processArticles(List<Article> articles);
}
