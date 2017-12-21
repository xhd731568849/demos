package com.btzh.transfer.util.gm;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

/**
 * 加密工具类
 * @author huangganquan
 * @date 2017/9/25
 */
public class GmUtil {

    /**
     * 生成一个sm4对称加密的秘钥
     */
    public static String getRandomSm4Key() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
    }

    /**
     * 根据明文长度计算密文长度
     *
     * @param plaintextLength 明文长度
     */
    public static long getSm4CipherTextLength(long plaintextLength) {
        return plaintextLength + (16 - plaintextLength % 16);
    }

    /**
     * 国密sm4对称加密
     *
     * @param plaintextFile 明文文件
     * @param destDir       加密后的密文文件的存放路径
     * @param secretKey     秘钥，必须是16个字节
     */
    public static void sm4EncryptFile(File plaintextFile, String destDir, String secretKey) {
        if (secretKey.getBytes().length != SECRET_KEY_LENGTH) {
            throw new RuntimeException("秘钥的字节长度必须为16！");
        }

        File file = null;
        try {
            Path dirPath = Paths.get(destDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            Path filePath = dirPath.resolve(plaintextFile.getName() + ".sm4");
            if (Files.exists(filePath)) {
                throw new FileAlreadyExistsException(filePath.toString());
            }

            file = Files.createFile(filePath).toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] bytes = new byte[1024];
        try (FileInputStream fis = new FileInputStream(plaintextFile); FileOutputStream fos = new FileOutputStream(file)) {
            int len = -1;
            while ((len = fis.read(bytes)) != -1) {
                // 读到最后一组数据时需要补位操作
                if (fis.available() == 0) {
                    fos.write(Sm4Utils.encryptDataECB(Arrays.copyOf(bytes, len), secretKey, true));
                    break;
                }

                fos.write(Sm4Utils.encryptDataECB(bytes, secretKey, false));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 国密sm4对称加密
     *
     * @param bytes     需要加密的数据
     * @param secretKey 秘钥，必须是16个字节
     * @param isPadding 是否需要补位,读到文件末尾时传入true
     */
    public static byte[] sm4EncryptData(byte[] bytes, String secretKey, boolean isPadding) {
        if (secretKey.getBytes().length != SECRET_KEY_LENGTH) {
            throw new RuntimeException("秘钥必须为16个字节！");
        }

        return Sm4Utils.encryptDataECB(bytes, secretKey, isPadding);
    }

    /**
     * 国密sm4对称加密
     *
     * @param plainText 明文
     * @param secretKey 秘钥，必须是16个字节
     */
    public static String sm4EncryptString(String plainText, String secretKey) {
        return Sm4Utils.encryptDataECB(plainText, secretKey);
    }

    /**
     * 国密sm4解密
     *
     * @param ciphertextFile 密文文件
     * @param destDir        解密后的明文文件的存放路径
     * @param secretKey      秘钥，必须是16个字节
     */
    public static void sm4DecryptFile(File ciphertextFile, String destDir, String secretKey) {
        if (secretKey.getBytes().length != SECRET_KEY_LENGTH) {
            throw new RuntimeException("秘钥必须为16个字节！");
        }

        File file = null;
        try {
            Path dirPath = Paths.get(destDir);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            Path filePath = dirPath.resolve(ciphertextFile.getName().substring(0, ciphertextFile.getName().lastIndexOf(".sm4")));
            if (Files.exists(filePath)) {
                throw new FileAlreadyExistsException(filePath.toString());
            }

            file = Files.createFile(filePath).toFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] bytes = new byte[1024];
        try (FileInputStream fis = new FileInputStream(ciphertextFile); FileOutputStream fos = new FileOutputStream(file)) {
            int len = -1;
            while ((len = fis.read(bytes)) != -1) {
                if (fis.available() == 0) {
                    fos.write(Sm4Utils.decryptDataECB(Arrays.copyOf(bytes, len), secretKey, true));
                    break;
                }

                fos.write(Sm4Utils.decryptDataECB(bytes, secretKey, false));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 国密sm4解密
     *
     * @param bytes     需要解密的数据
     * @param secretKey 秘钥，必须是16个字节
     * @param isPadding 是否需要补位,读到文件末尾时传入true
     */
    public static byte[] sm4DecryptData(byte[] bytes, String secretKey, boolean isPadding) {
        if (secretKey.getBytes().length != SECRET_KEY_LENGTH) {
            throw new RuntimeException("秘钥必须为16个字节！");
        }
        return Sm4Utils.decryptDataECB(bytes, secretKey, isPadding);
    }

    /**
     * 国密sm4解密
     *
     * @param cipherText 密文
     * @param secretKey  秘钥，必须是16个字节
     */
    public static String sm4DecryptString(String cipherText, String secretKey) {
        return Sm4Utils.decryptDataECB(cipherText, secretKey);
    }

    /**
     * 生成密钥对
     */
    public static String[] generateKeyPair() {
        AsymmetricCipherKeyPair key = new Sm2().ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();

        return new String[]{Util.byteToHex(ecpub.getQ().getEncoded()), Util.byteToHex(ecpriv.getD().toByteArray())};
    }

    /**
     * 公钥加密
     */
    public static String encryptByPublickey(String publicKey, String plaintext) {
        if (publicKey == null || plaintext == null) {
            return null;
        }

        byte[] ketBytes = Util.hexToByte(publicKey);
        if (ketBytes.length == 0) {
            return null;
        }

        byte[] data = plaintext.getBytes();
        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);

        Sm2 sm2 = new Sm2();
        ECPoint userKey = sm2.ecc_curve.decodePoint(ketBytes);

        Cipher cipher = new Cipher();
        ECPoint c1 = cipher.initEnc(sm2, userKey);
        cipher.encrypt(source);
        byte[] c3 = new byte[32];
        cipher.dofinal(c3);

        return Util.byteToHex(c1.getEncoded()) + Util.byteToHex(source) + Util.byteToHex(c3);
    }

    /**
     * 私钥解密
     */
    public static String decryptByPrivatekey(String privateKey, String cipherText) {
        byte[] keyBytes = Util.hexToByte(privateKey);
        byte[] encryptedData = Util.hexToByte(cipherText);
        if (keyBytes.length == 0 || encryptedData.length == 0) {
            return null;
        }

        String data = Util.byteToHex(encryptedData);
        byte[] c1Bytes = Util.hexToByte(data.substring(0, 130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = Util.hexToByte(data.substring(130, 130 + 2 * c2Len));
        byte[] c3 = Util.hexToByte(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));

        Cipher cipher = new Cipher();
        cipher.initDec(new BigInteger(1, keyBytes), new Sm2().ecc_curve.decodePoint(c1Bytes));
        cipher.decrypt(c2);
        cipher.dofinal(c3);

        return new String(c2);
    }

    /**
     * 生成比特数组的摘要
     */
    public static String getSM3Digest(byte[] data) {
        if (data.length == 0) {
            return null;
        }

        byte[] md = new byte[32];
        Sm3DigestAbstract sm3 = new Sm3DigestAbstract();
        sm3.update(data, 0, data.length);
        sm3.doFinal(md, 0);

        return new String(Hex.encode(md)).toUpperCase();
    }

    /**
     * 生成文件摘要
     */
    public static String getSM3Digest(Path file) {
        if (!Files.exists(file)) {
            throw new RuntimeException("File not found: " + file.toString());
        }

        byte[] md = new byte[32];
        byte[] buffer = new byte[1024 * 1];
        Sm3DigestAbstract sm3 = new Sm3DigestAbstract();

        try (InputStream fis = Files.newInputStream(file)) {
            int len = 0;
            while ((len = fis.read(buffer)) > 0) {
                sm3.update(buffer, 0, len);
            }

            sm3.doFinal(md, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new String(Hex.encode(md));
    }

    private final static int SECRET_KEY_LENGTH = 16;
}