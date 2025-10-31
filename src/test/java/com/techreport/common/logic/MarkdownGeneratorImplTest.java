package com.techreport.common.logic;

import com.techreport.common.AppConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * <p>{@link MarkdownGenerator} の実装のテストを行う。</p>
 *
 * <ul>
 *     <li>{@link #saveMarkdownFileSuccess01} 正常系：マークダウンファイルの保存。</li>
 * </ul>
 */
@SpringBootTest
public class MarkdownGeneratorImplTest {
    @Autowired
    MarkdownGenerator logic;
    @Autowired
    private AppConfig appConfig;
    // テスト用
    @TempDir
    private File temporaryFolder;
    // 日付フォーマット
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * <p>マークダウンファイルを保存するメソッドの正常系。</p>
     *
     * @throws Exception 例外が発生した場合
     */
    @Test
    public void saveMarkdownFileSuccess01() throws Exception {

        //
        // 事前準備
        //

        // テスト用のマークダウン文字列
        String markdownString = "# テスト";
        // 期待されるファイル名
        String expectedFileName = LocalDateTime.now().format(DATE_FORMATTER) + "-weekly-summary.md";
        // テスト用の保存先ディレクトリを設定
        appConfig.setReportDir(temporaryFolder.getAbsolutePath());

        //
        // 実行
        //

        logic.saveMarkdownFile(markdownString);

        //
        // 検証
        //

        File expectedFile = new File(temporaryFolder, expectedFileName);
        assert(expectedFile.exists());
    }
}