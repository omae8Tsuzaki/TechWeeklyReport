package com.techreport.common.logic;

import com.techreport.common.LogicException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

public class MarkdownGeneratorImplTest {
    // テスト用
    @TempDir
    private File temporaryFolder;

    MarkdownGenerator logic = new MarkdownGeneratorImpl();

    @Test
    public void saveMarkdownFileSuccess01() throws LogicException {
        String markdownString = "# テスト";

        System.out.println(temporaryFolder.getAbsolutePath());

        // 実行
        logic.saveMarkdownFile(markdownString);

        //
    }
}