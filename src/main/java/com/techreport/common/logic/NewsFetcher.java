package com.techreport.common.logic;

import com.techreport.common.model.Article;

import java.util.List;

public interface NewsFetcher {

    public List<Article> fetchRecentArticles();
}
