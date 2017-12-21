package com.btzh.transfer.util.gm;

/**
 * Sm3DigestAbstract
 * @author huangganquan
 * @date 2017/9/25
 */
public class Sm3DigestAbstract extends AbstractGeneralDigest {
    private final int DIGEST_LENGTH = 32;

    private final static int[] V0 = new int[]{(int) 0x7380166f,
            (int) 0x4914b2b9, (int) 0x172442d7, (int) 0xda8a0600,
            (int) 0xa96f30bc, (int) 0x163138aa, (int) 0xe38dee4d,
            (int) 0xb0fb0e4e};

    private int[] v = new int[8];
    private int[] v1 = new int[8];
    private static int[] X0 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0};
    private int[] x = new int[68];
    private int xOff;
    private int t0015 = 0x79cc4519;
    private int t1663 = 0x7a879d8a;

    public Sm3DigestAbstract() {
        reset();
    }

    public Sm3DigestAbstract(Sm3DigestAbstract t) {
        super(t);
        System.arraycopy(t.x, 0, x, 0, t.x.length);
        xOff = t.xOff;
        System.arraycopy(t.v, 0, v, 0, t.v.length);
    }

    @Override
    public void reset() {
        super.reset();
        System.arraycopy(V0, 0, v, 0, V0.length);
        xOff = 0;
        System.arraycopy(X0, 0, x, 0, X0.length);
    }

    @Override
    protected void processBlock() {
        int i;

        int[] ww = x;
        int[] wwTemp = new int[64];

        for (i = LENGTH_SIXTEEN; i < LENGTH_SIXTY_EIGHT; i++) {
            ww[i] = p1(ww[i - 16] ^ ww[i - 9] ^ (rotate(ww[i - 3], 15)))
                    ^ (rotate(ww[i - 13], 7)) ^ ww[i - 6];
        }

        for (i = 0; i < LENGTH_SIXTY_FOUR; i++) {
            wwTemp[i] = ww[i] ^ ww[i + 4];
        }

        int[] vv = v;
        int[] vvTemp = v1;

        System.arraycopy(vv, 0, vvTemp, 0, vv.length);

        int ss1, ss2, tt1, tt2, aaa;
        for (i = 0; i < LENGTH_SIXTEEN; i++) {
            aaa = rotate(vvTemp[0], 12);
            ss1 = aaa + vvTemp[4] + rotate(t0015, i);
            ss1 = rotate(ss1, 7);
            ss2 = ss1 ^ aaa;

            tt1 = ff0015(vvTemp[0], vvTemp[1], vvTemp[2]) + vvTemp[3] + ss2 + wwTemp[i];
            tt2 = gg0015(vvTemp[4], vvTemp[5], vvTemp[6]) + vvTemp[7] + ss1 + ww[i];
            vvTemp[3] = vvTemp[2];
            vvTemp[2] = rotate(vvTemp[1], 9);
            vvTemp[1] = vvTemp[0];
            vvTemp[0] = tt1;
            vvTemp[7] = vvTemp[6];
            vvTemp[6] = rotate(vvTemp[5], 19);
            vvTemp[5] = vvTemp[4];
            vvTemp[4] = p0(tt2);
        }
        for (i = LENGTH_SIXTEEN; i < LENGTH_SIXTY_FOUR; i++) {
            aaa = rotate(vvTemp[0], 12);
            ss1 = aaa + vvTemp[4] + rotate(t1663, i);
            ss1 = rotate(ss1, 7);
            ss2 = ss1 ^ aaa;

            tt1 = ff1663(vvTemp[0], vvTemp[1], vvTemp[2]) + vvTemp[3] + ss2 + wwTemp[i];
            tt2 = gg1663(vvTemp[4], vvTemp[5], vvTemp[6]) + vvTemp[7] + ss1 + ww[i];
            vvTemp[3] = vvTemp[2];
            vvTemp[2] = rotate(vvTemp[1], 9);
            vvTemp[1] = vvTemp[0];
            vvTemp[0] = tt1;
            vvTemp[7] = vvTemp[6];
            vvTemp[6] = rotate(vvTemp[5], 19);
            vvTemp[5] = vvTemp[4];
            vvTemp[4] = p0(tt2);
        }
        for (i = 0; i < LENGTH_EIGHT; i++) {
            vv[i] ^= vvTemp[i];
        }

        // Reset
        xOff = 0;
        System.arraycopy(X0, 0, x, 0, X0.length);

    }

    @Override
    protected void processWord(byte[] inRenamed, int inOff) {
        int n = inRenamed[inOff] << 24;
        n |= (inRenamed[++inOff] & 0xff) << 16;
        n |= (inRenamed[++inOff] & 0xff) << 8;
        n |= (inRenamed[++inOff] & 0xff);
        x[xOff] = n;
        if (++xOff == LENGTH_SIXTEEN) {
            processBlock();
        }
    }

    @Override
    protected void processLength(long bitLength) {
        if (xOff > LENGTH_FOURTEEN) {
            processBlock();
        }
        x[14] = (int) (SupportClass.urShift(bitLength, 32));
        x[15] = (int) (bitLength & (int) 0xffffffff);
    }

    @Override
    public String getAlgorithmName() {
        return "SM3";
    }

    @Override
    public int getDigestSize() {
        return DIGEST_LENGTH;
    }

    @Override
    public int doFinal(byte[] outRenamed, int outOff) {
        finish();
        for (int i = 0; i < LENGTH_EIGHT; i++) {
            intToBigEndian(v[i], outRenamed, outOff + i * 4);
        }
        reset();
        return DIGEST_LENGTH;
    }

    public static void intToBigEndian(int n, byte[] bs, int off) {
        bs[off] = (byte) (SupportClass.urShift(n, 24));
        bs[++off] = (byte) (SupportClass.urShift(n, 16));
        bs[++off] = (byte) (SupportClass.urShift(n, 8));
        bs[++off] = (byte) (n);
    }

    private int rotate(int x, int n) {
        return (x << n) | (SupportClass.urShift(x, (32 - n)));
    }

    private int p0(int x) {
        return ((x) ^ rotate((x), 9) ^ rotate((x), 17));
    }

    private int p1(int x) {
        return ((x) ^ rotate((x), 15) ^ rotate((x), 23));
    }

    private int ff0015(int x, int y, int z) {
        return (x ^ y ^ z);
    }

    private int ff1663(int x, int y, int z) {
        return ((x & y) | (x & z) | (y & z));
    }

    private int gg0015(int x, int y, int z) {
        return (x ^ y ^ z);
    }

    private int gg1663(int x, int y, int z) {
        return ((x & y) | (~x & z));
    }

    private final static int LENGTH_EIGHT = 8;
    private final static int LENGTH_FOURTEEN = 14;
    private final static int LENGTH_SIXTEEN = 16;
    private final static int LENGTH_SIXTY_FOUR = 64;
    private final static int LENGTH_SIXTY_EIGHT = 68;
}
