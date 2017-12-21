package com.btzh.transfer.keyload;

import java.io.BufferedReader;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.btzh.consts.BusinessType;
import com.btzh.consts.PrivatePublicKey;
import com.btzh.transfer.Client;
import com.btzh.transfer.entity.BaseFile;
import com.btzh.transfer.util.gm.GmUtil;
import com.btzh.util.RepositoryUtil;

/**
 * 公钥文件发送
 * @author tanzhibo
 * @date 2017/9/25
 */
public class PublicKeySender implements Runnable {

    @Override
    public void run() {
        // 判断当前的公钥是否已经发送
        Path localKeyPath = RepositoryUtil.getLocalKeyPath();
        Path localPublicKeyFile = localKeyPath.resolve(PrivatePublicKey.PUBLIC_KEY_FILE);
        Path localPublicKeyDigestFile = localKeyPath.resolve(PrivatePublicKey.PUBLIC_KEY_DIGEST_FILE);
        String currentPublicKeyDigest = GmUtil.getSM3Digest(localPublicKeyFile);
        if (Files.exists(localPublicKeyDigestFile)) {
            try (BufferedReader reader = Files.newBufferedReader(localPublicKeyDigestFile, Charset.forName("UTF-8"))) {
                // 已发送的publickey摘要与当前的publickey摘要相同则公钥没有变化不需要再发送
                if (currentPublicKeyDigest.equals(reader.readLine())) {
                    return;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        BaseFile publicKeyFile = new BaseFile(BusinessType.PUBLIC_KEY_FILE, File.separator + PrivatePublicKey.PUBLIC_KEY_FILE, localPublicKeyFile.toFile());
        int count = 1;
        Logger logger = LoggerFactory.getLogger(PublicKeySender.class);
        while (true) {
            try {
                logger.info("公钥发送第" + count++ + "次尝试");
                new Client().send(publicKeyFile);
                logger.info("公钥发送成功！");
                break;
            } catch (Exception e) {
                logger.error("公钥发送失败:" + e.getMessage());
            }
            
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
            }
        }

        // 当前的公钥发送出去后保存当前公钥的摘要
        try (OutputStream fis = Files.newOutputStream(localPublicKeyDigestFile, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            fis.write(currentPublicKeyDigest.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}