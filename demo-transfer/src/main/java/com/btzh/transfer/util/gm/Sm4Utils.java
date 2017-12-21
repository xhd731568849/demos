package com.btzh.transfer.util.gm;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.util.regex.Pattern;

/**
 * Sm4Utils
 * @author huangganquan
 * @date 2017/9/25
 */
@SuppressWarnings("restriction")
public class Sm4Utils {
    private static Pattern STRING_PATTERN = Pattern.compile("\\s*|\t|\r|\n");

    public static String encryptDataECB(String plainText, String secretKey) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_ENCRYPT;

            Sm4 sm4 = new Sm4();
            sm4.sm4SetkeyEnc(ctx, secretKey.getBytes());

            String cipherText = new BASE64Encoder().encode(sm4.sm4CryptEcb(ctx, plainText.getBytes("UTF-8")));
            if (cipherText.trim().length() > 0) {
                cipherText = STRING_PATTERN.matcher(cipherText).replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptDataECB(byte[] bytes, String secretKey, boolean isPadding) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = isPadding;
            ctx.mode = Sm4.SM4_ENCRYPT;

            Sm4 sm4 = new Sm4();
            sm4.sm4SetkeyEnc(ctx, secretKey.getBytes());

            return sm4.sm4CryptEcb(ctx, bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptDataECB(String cipherText, String secretKey) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = true;
            ctx.mode = Sm4.SM4_DECRYPT;

            Sm4 sm4 = new Sm4();
            sm4.sm4SetkeyDec(ctx, secretKey.getBytes());

            return new String(sm4.sm4CryptEcb(ctx, new BASE64Decoder().decodeBuffer(cipherText)), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decryptDataECB(byte[] bytes, String secretKey, boolean isPadding) {
        try {
            Sm4Context ctx = new Sm4Context();
            ctx.isPadding = isPadding;
            ctx.mode = Sm4.SM4_DECRYPT;

            Sm4 sm4 = new Sm4();
            sm4.sm4SetkeyDec(ctx, secretKey.getBytes());

            return sm4.sm4CryptEcb(ctx, bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}