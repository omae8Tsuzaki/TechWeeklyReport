package com.techreport.common.logic;

import com.techreport.common.LogicException;

import java.io.IOException;
import java.nio.file.Path;

/**
 * <p>Markdown の生成を行うクラス。</p>
 */
public interface MarkdownGenerator {

    public void saveMarkdownFile(String markdownContent) throws LogicException;

    public void saveMarkdownFile(String markdownContent, Path savePath) throws IOException;
}
