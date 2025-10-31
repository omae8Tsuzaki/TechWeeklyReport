package com.techreport.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>{@link LogicException}のテストを行う。</p>
 *
 * <ul>
 *     <li>{@link #constructorSuccess01} コンストラクタの正常系テスト</li>
 *     <li>{@link #constructorSuccess02} コンストラクタの正常系テスト（Throwable なし）</li>
 * </ul>
 */
public class LogicExceptionTest {

    /**
     * <p>コンストラクタの正常系テスト。</p>
     *
     * @throws Exception 例外が発生した場合
     */
    @Test
    public void constructorSuccess01() throws Exception {

        //
        //事前準備
        //

        Throwable cause = new Throwable("Root Cause");

        //
        // 実行
        //
        LogicException exception = new LogicException("Test Message", cause);

        //
        // 検証
        //
        assertEquals("Test Message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    /**
     * <p>コンストラクタの正常系テスト（Throwable なし）。</p>
     *
     * @throws Exception 例外が発生した場合
     */
    @Test
    public void constructorSuccess02() throws Exception {

        //
        // 実行
        //
        LogicException exception = new LogicException("Test Message Only");

        //
        // 検証
        //
        assertEquals("Test Message Only", exception.getMessage());
        assertNull(exception.getCause());
    }

}