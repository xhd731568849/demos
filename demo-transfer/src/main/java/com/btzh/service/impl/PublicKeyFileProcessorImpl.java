package com.btzh.service.impl;

import com.btzh.Main;
import com.btzh.consts.PrivatePublicKey;
import com.btzh.service.PublicKeyFileProcessor;
import com.btzh.transfer.entity.BaseFile;
import com.btzh.util.RepositoryUtil;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 公钥文件处理
 * @author tanzhibo
 * @date 2017/9/25
 */
@Service("publicKeyFileProcessor")
public class PublicKeyFileProcessorImpl implements PublicKeyFileProcessor {

    @Override
    public void process(BaseFile publicKeyFile) {
        Path newPublicKeyPath = null;
        try {
            Path target = RepositoryUtil.getRemoteKeyPath().resolve(PrivatePublicKey.PUBLIC_KEY_FILE);
            newPublicKeyPath = Files.move(publicKeyFile.getFile().toPath(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (BufferedReader reader = Files.newBufferedReader(newPublicKeyPath, CharsetUtil.UTF_8)) {
            Main.setRemotePublicKey(reader.readLine());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}