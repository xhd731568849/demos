package com.nio.netty.transfer.util;

import java.util.UUID;

/**
 * @author xhd
 * @date 2017/12/12
 */
public final class StringUtil {
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
