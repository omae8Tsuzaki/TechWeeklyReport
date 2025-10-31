package com.techreport.common.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreport.common.AppConfig;
import com.techreport.common.model.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>{@link NewsFetcher} の実装のテストを行う。</p>
 */
@SpringBootTest(classes = {NewsFetcherImpl.class, AppConfig.class, ObjectMapper.class})
@ContextConfiguration(classes = {AppConfig.class})
public class NewsFetcherImplTest {

    // 設定の上書き確認用
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    NewsFetcher newsFetcher;

    /**
     * <p>各テストケースが呼ばれるごとに行う初期化。</p>
     */
    @BeforeEach
    void setUp() {

    }

    /**
     * <p>正常系：ステータスコードが200出ない場合。</p>
     */
    @Test
    public void fetchRecentArticlesSuccess01() {

        //
        // 実行
        //

        newsFetcher = new NewsFetcherImpl(appConfig, objectMapper);
        List<Article> result = newsFetcher.fetchRecentArticles();


        // 検証
    }

    /**
     * <p>loadFeedList メソッドの正常系</p>
     */
    @Test
    public void loadFeedListSuccess01(){

        //
        // 事前準備
        //
        NewsFetcherImpl newsFetcher = new NewsFetcherImpl(appConfig, objectMapper);

        //
        // 実行
        //
        List<Map<String, String>> result = newsFetcher.loadFeedList(appConfig.getFeedListPath());

        //
        // 検証
        //
        assertEquals(1, result.size());
        assertEquals("{name=Test Feed 1, url=http://test.com/feed1}", result.getFirst().toString());

    }

    @Test
    public void loadFeedListError01() {
        NewsFetcherImpl newsFetcher = new NewsFetcherImpl(appConfig, objectMapper);

        String errorPath = null;
        try {
            newsFetcher.loadFeedList(errorPath);
        } catch (Exception e) {
            assertEquals("", e.getMessage());
        }
    }
}