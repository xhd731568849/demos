package com.btzh.transfer.util.gm;

import org.bouncycastle.crypto.Digest;

/**
 *
 * @author hgq
 * @date 17-10-22 下午2:57 
 */
public abstract class AbstractGeneralDigest implements Digest {
    private static int BYTE_LENGTH = 64;

    private byte[] xBuf;
    private int xBufOff;

    private long byteCount;

    public AbstractGeneralDigest() {
        xBuf = new byte[4];
    }

    public AbstractGeneralDigest(AbstractGeneralDigest t) {
        xBuf = new byte[t.xBuf.length];
        System.arraycopy(t.xBuf, 0, xBuf, 0, xBuf.length);
        xBufOff = t.xBufOff;
        byteCount = t.byteCount;
    }

    @Override
    public void update(byte input) {
        xBuf[xBufOff++] = input;
        if (xBufOff == xBuf.length) {
            processWord(xBuf, 0);
            xBufOff = 0;
        }
        byteCount++;
    }

    @Override
    public void update(byte[] input, int inOff, int length) {
        //
        // fill the current word
        //
        while ((xBufOff != 0) && (length > 0)) {
            update(input[inOff]);
            inOff++;
            length--;
        }

        //
        // process whole words.
        //
        while (length > xBuf.length) {
            processWord(input, inOff);
            inOff += xBuf.length;
            length -= xBuf.length;
            byteCount += xBuf.length;
        }

        //
        // load in the remainder.
        //
        while (length > 0) {
            update(input[inOff]);
            inOff++;
            length--;
        }
    }

    public void finish() {
        long bitLength = (byteCount << 3);

        //
        // add the pad bytes.
        //
        update((byte) 128);

        while (xBufOff != 0) {
            update((byte) 0);
        }
        processLength(bitLength);
        processBlock();
    }

    @Override
    public void reset() {
        byteCount = 0;
        xBufOff = 0;
        for (int i = 0; i < xBuf.length; i++) {
            xBuf[i] = 0;
        }
    }

    public int getByteLength() {
        return BYTE_LENGTH;
    }

    /**
     * 文字处理
     * @param input
     * @param inOff
     */
    protected abstract void processWord(byte[] input, int inOff);

    /**
     * 加工长度
     * @param bitLength
     */
    protected abstract void processLength(long bitLength);

    /**
     * 程序块
     */
    protected abstract void processBlock();

    /**
     * 得到算法名称
     * @return
     */
    @Override
    public abstract String getAlgorithmName();

    /**
     * 得到摘要大小
     * @return
     */
    @Override
    public abstract int getDigestSize();

    /**
     * 最后处理
     * @param output
     * @param outOff
     * @return
     */
    @Override
    public abstract int doFinal(byte[] output, int outOff);
}
