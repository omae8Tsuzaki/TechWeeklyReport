package com.techreport.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>アプリケーションを起動するメインクラス。</p>
 */
@SpringBootApplication(scanBasePackages = "com.techreport.common")
public class MainApplication {

    /**
     * <p>アプリケーションのエントリーポイント。</p>
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        // Spring Bootアプリケーションを起動
        SpringApplication.run(MainApplication.class, args);
    }

}
