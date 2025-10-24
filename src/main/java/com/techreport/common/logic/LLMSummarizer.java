package com.techreport.common.logic;

import com.techreport.common.model.Article;

import java.util.List;

public interface LLMSummarizer {

    public List<Article>  processArticles(List<Article> articles);
}
