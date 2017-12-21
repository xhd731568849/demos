package com.btzh.util;

import com.btzh.config.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Repository工具类
 * @author tanzhibo
 * @date 2017/9/25
 */
public class RepositoryUtil {

    public static Path getRemoteKeyPath() {
        return getRepositoryPath("keys", "remote");
    }

    public static Path getLocalKeyPath() {
        return getRepositoryPath("keys", "local");
    }

    public static Path getSendSuccessFilePath() {
        return getRepositoryPath("files", "sendSuccess");
    }

    public static Path getLocalFilePath() {
        return getRepositoryPath("files", Config.getConfig().getLocalFilePath());
    }

    public static Path getRemoteFilePath() {
        return getRepositoryPath("files", Config.getConfig().getRemoteFilePath());
    }

    private static Path getRepositoryPath(String... children) {
        Path path = Paths.get(Config.getConfig().getRepository(), children);
        if (!Files.exists(path)) {
            try {
                System.out.println(path.toString());
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return path;
    }
}