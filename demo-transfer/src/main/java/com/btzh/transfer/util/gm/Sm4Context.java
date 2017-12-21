package com.btzh.transfer.util.gm;

/**
 * Sm4Context
 * @author huangganquan
 * @date 2017/9/25
 */
public class Sm4Context {

    /**
     * 模式 1表示加密 0表示解密
     */
    public int mode;

    /**
     * 秘钥
     */
    public long[] sk;

    /**
     * 是否进行补位
     */
    public boolean isPadding;

    public Sm4Context() {
        this.mode = 1;
        this.isPadding = true;
        this.sk = new long[32];
    }
}
