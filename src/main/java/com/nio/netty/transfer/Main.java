package com.nio.netty.transfer;

import com.nio.netty.transfer.config.Config;

/**
 * @author xhd
 * @date 2017/12/12
 */
public class Main {
    private static volatile Config config = null;

    //public static void main(String[] args) throws Exception {
    //    config = Config.resolve();
    //    final Server server = new Server();
    //    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
    //        @Override
    //        public void run() {
    //            server.shutdown();
    //        }
    //    }));
    //    server.start(config);
    //}
    public static Config getConfig(){
        return config;
    }
}
