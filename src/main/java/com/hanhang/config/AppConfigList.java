package com.hanhang.config;

import lombok.Data;

import java.util.List;

/**
 * @author hanhang
 */
@Data
public class AppConfigList {
    private List<AppConfig> configs;

    @Data
    public static class AppConfig{
        private String name;

        private String file;
    }
}
