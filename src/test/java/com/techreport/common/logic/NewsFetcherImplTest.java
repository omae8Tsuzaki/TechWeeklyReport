package com.techreport.common.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreport.common.AppConfig;
import com.techreport.common.model.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>{@link NewsFetcher} の実装のテストを行う。</p>
 *
 * <h4>{@link NewsFetcherImpl#loadFeedList} メソッド</h4>
 * <ul>
 *     <li>{@link #loadFeedListSuccess01} 正常系：存在するフィードリストファイルの場合。</li>
 *     <li>{@link #loadFeedListSuccess02} 正常系：存在しないフィードリストファイルの場合。</li>
 * </ul>
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
     *
     * @throws Exception 例外が発生した場合
     */
    @Test
    public void fetchRecentArticlesSuccess01() throws Exception {

        //
        // 実行
        //

        newsFetcher = new NewsFetcherImpl(appConfig, objectMapper);
        List<Article> result = newsFetcher.fetchRecentArticles();

        //
        // 検証
        //

        assertEquals(0, result.size());
    }

    /**
     * <p>正常系：存在するフィードリストファイルの場合。</p>
     *
     * @throws Exception 例外が発生した場合
     */
    @Test
    public void loadFeedListSuccess01() throws Exception {

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

    /**
     * <p>正常系：存在しないフィードリストファイルの場合。</p>
     *
     * @throws Exception 例外が発生した場合
     */
    @Test
    public void loadFeedListSuccess02() throws Exception {
        //
        // 事前準備
        //

        NewsFetcherImpl newsFetcher = new NewsFetcherImpl(appConfig, objectMapper);

        //
        // 実行
        //

        // 存在しないファイルパス
        List<Map<String, String>> result = newsFetcher.loadFeedList("path/that/does/not/exist/feed_list.json");

        //
        // 検証
        //

        assertEquals(0, result.size());
    }
}