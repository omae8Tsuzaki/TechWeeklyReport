package com.techreport.common.logic;

import com.techreport.common.AppConfig;
import com.techreport.common.LogicException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarkdownGeneratorImpl implements MarkdownGenerator {

    @Autowired
    AppConfig config;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    Logger logger = Logger.getLogger(MarkdownGeneratorImpl.class.getName());

    /**
     * <p>Markdown ファイルの保存。</p>
     *
     * @param markdownContent マークダウンファイルに出力するテキスト
     * @throws LogicException 例外が発生した場合
     */
    @Override
    public void saveMarkdownFile(String markdownContent) throws LogicException {

        try {
            // 保存先ディレクトリを確認・作成
            File dir = new File(config.getReportDir());
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成する Markdown ファイル名
            String fileName = LocalDateTime.now().format(DATE_FORMATTER) + "-weekly-summary.md";
            File output = new File(dir, fileName);

            // ファイルに書き込み (UTF-8)
            Files.writeString(output.toPath(), markdownContent);

            logger.log(Level.INFO, "ファイルの書き込み成功");
        } catch (IOException e) {
            throw new LogicException("", e);
        }
    }

    /**
     * <p>生成された　Markdownファイルの保存。</p>
     *
     */
    @Override
    public void saveMarkdownFile(String markdownContent, Path savePath) throws IOException {

        // ディレクトリが存在しない場合は作成
        Files.createDirectories(savePath);

        // ファイルに書き込み (UTF-8)
        Files.writeString(savePath, markdownContent);

        System.out.println("Markdown report saved to: " + savePath.toAbsolutePath());
    }
}
