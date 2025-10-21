package com.techreport.common.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreport.common.AppConfig;
import com.techreport.common.model.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>{@link NewsFetcherImpl} のテストを行う。</p>
 */
@SpringBootTest(classes = {NewsFetcherImpl.class, AppConfig.class, ObjectMapper.class})
@TestPropertySource(properties = {
        // テスト用のfeedリスト
        "techReport.feedList.path=src/test/resources/feed_list_test.json",
        // 期間を7から3に変更
        "techReport.interval.days=3",
})
public class NewsFetcherImplTest {

    // 設定の上書き確認用
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private ObjectMapper objectMapper;

    private NewsFetcher newsFetcher;

    /**
     * <p>各テストケースが呼ばれるごとに行う初期化。</p>
     */
    @BeforeEach
    void setUp() {

    }

    /**
     * <p>AppConfig の設定が TestPropertySource で上書きされているか確認</p>
     */
    @Test
    public void appConfigOverrideTest() {
        //
        assertEquals(3, appConfig.getWeeklyDays());
        assertEquals("src/test/resources/feed_list_test.json", appConfig.getFeedListPath());
    }

    /**
     * <p>正常系：ステータスコードが200出ない場合。</p>
     */
    @Test
    public void fetchRecentArticlesSuccess01() {

        // 実行

        newsFetcher = new NewsFetcherImpl(appConfig, objectMapper);
        List<Article> result = newsFetcher.fetchRecentArticles();


        // 検証
    }

    /**
     * <p>loadFeedList メソッドの正常系</p>
     */
    @Test
    public void loadFeedListSuccess01(){

        NewsFetcherImpl newsFetcher = new NewsFetcherImpl(appConfig, objectMapper);

        // 実行
        List<Map<String, String>> result = newsFetcher.loadFeedList(appConfig.getFeedListPath());

        // 検証
        assertEquals(1, result.size());
        assertEquals("{name=Test Feed 1, url=http://test.com/feed1}", result.getFirst().toString());

    }
}