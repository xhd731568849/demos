package com.btzh;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.btzh.transfer.Server;
import com.btzh.transfer.keyload.KeyLoader;

/**
 * 程序入口
 * @author wanglidong
 * @date 2017/9/25
 */
public class Main {

    private static volatile ApplicationContext application_context = null;
    private static volatile String remote_public_key = null;
    private static volatile String local_private_key = null;

    public static void main(String[] args) throws Exception {
        final ClassPathXmlApplicationContext application = new ClassPathXmlApplicationContext(new String[] {"classpath*:applicationContext.xml"});
        setApplicationContext(application);

        new Thread(new KeyLoader(), "KeyLoader").start();

        final Server server = new Server();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                application.close();
                server.shutdown();
            }
        }));
        server.start();
    }

    private static void setApplicationContext(ApplicationContext applicationContext) {
        Main.application_context = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return Main.application_context;
    }

    public static String getRemotePublicKey() {
        return Main.remote_public_key;
    }

    public static void setRemotePublicKey(String publicKey) {
        Main.remote_public_key = publicKey;
    }

    public static String getLocalPrivateKey() {
        return Main.local_private_key;
    }

    public static void setLocalPrivateKey(String localPrivateKey) {
        Main.local_private_key = localPrivateKey;
    }
}