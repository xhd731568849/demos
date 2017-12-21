package com.btzh.transfer.keyload;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.btzh.Main;
import com.btzh.consts.PrivatePublicKey;
import com.btzh.transfer.util.gm.GmUtil;
import com.btzh.util.RepositoryUtil;

import io.netty.util.CharsetUtil;

/**
 * 加载公钥
 * @author huangganquan
 * @date 2017/9/25
 */
public class KeyLoader implements Runnable {

    @Override
    public void run() {
        Path localKeyPath = RepositoryUtil.getLocalKeyPath();
        Path localPublicKeyFile = localKeyPath.resolve(PrivatePublicKey.PUBLIC_KEY_FILE);
        Path localPrivateKeyFile = localKeyPath.resolve(PrivatePublicKey.PRIVATE_KEY_FILE);
        if (!Files.exists(localPublicKeyFile) || !Files.exists(localPrivateKeyFile)) {
            String[] keys = GmUtil.generateKeyPair();
            save(localPublicKeyFile, keys[0]);
            save(localPrivateKeyFile, keys[1]);
        }

        try (BufferedReader reader = Files.newBufferedReader(localPrivateKeyFile, CharsetUtil.UTF_8)) {
            Main.setLocalPrivateKey(reader.readLine());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        new Thread(new PublicKeySender(), "PublicKeySender").start();

        // 加载远程服务器的公钥
        Path remotePublicKeyFile = RepositoryUtil.getRemoteKeyPath().resolve(PrivatePublicKey.PUBLIC_KEY_FILE);
        if (!Files.exists(remotePublicKeyFile)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(remotePublicKeyFile, CharsetUtil.UTF_8)) {
            Main.setRemotePublicKey(reader.readLine());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void save(Path file, String content) {
        try {
            Path parentPath = file.getParent();
            if (Files.exists(parentPath)) {
                Files.deleteIfExists(file);
            } else {
                Files.createDirectories(parentPath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.CREATE_NEW)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}