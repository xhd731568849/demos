package com.btzh.util;

import java.io.File;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * 字符串工具类
 * @author wanglidong
 * @date 2017/9/25
 */
public final class StringUtil {

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static int getByteLength(String str) {
        return getByteLength(str, Charset.forName("UTF-8"));
    }

    public static int getByteLength(String str, Charset charset) {
        if (null == str || str.isEmpty()) {
            return 0;
        }

        return str.getBytes(charset).length;
    }

    public static String getRealFilePath(String filePath) {
        if (filePath.startsWith(File.separator)) {
            return filePath;
        } else {
            if (System.getProperty(OS_NAME).toLowerCase().startsWith(WIN)) {
                return filePath.replace("/", File.separator);
            } else {
                return filePath.replace("\\", File.separator);
            }
        }
    }

    private final static String OS_NAME = "os.name";
    private final static String WIN = "win";
}