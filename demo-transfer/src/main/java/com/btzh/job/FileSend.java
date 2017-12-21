package com.btzh.job;

import com.btzh.config.Config;
import com.btzh.transfer.Client;
import com.btzh.transfer.entity.BaseFile;
import com.btzh.transfer.entity.EncryptedFile;
import com.btzh.util.RepositoryUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 文件发送
 * @author lilin
 * @date 2017/9/26
 */
@Component("fileSend")
public class FileSend {

    private final static Logger logger = LoggerFactory.getLogger(FileSend.class);

    @Autowired
    private Config config;
    private ExecutorService executor = null;

    public void send() {
        logger.info("fileSend start");
        if (null == executor) {
            int businessThreadPoolSize = Config.getConfig().getBusinessThreadPoolSize();
            ThreadFactory namedThreadFactory = (Runnable r)-> new Thread(r, "fileSend_thread_pool_" + r.hashCode());
            executor = new ThreadPoolExecutor(businessThreadPoolSize, businessThreadPoolSize,
                    3, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(), namedThreadFactory);
        }
        long start = System.currentTimeMillis();
        List<Path> files = new LinkedList<Path>();
        Client client = new Client();
        try {
            Files.walkFileTree(RepositoryUtil.getLocalFilePath(), new FindVisitor(files));
            CountDownLatch latch = new CountDownLatch(files.size());
            for (Path path : files) {
                executor.execute(() -> {
                            //截取路径
                            String subPath = StringUtils.substringAfter(path.toString(), config.getLocalFilePath());
                            //密文传输
                            if (config.getEncrypted()) {
                                client.send(new EncryptedFile(subPath, path.toFile()));
                            } else {
                                client.send(new BaseFile(subPath, path.toFile()));
                            }

                            Path sendSuccessFilePath = RepositoryUtil.getSendSuccessFilePath().resolve(subPath.substring(1));
                            try {
                                if (!Files.exists(sendSuccessFilePath.getParent())) {
                                    Files.createDirectories(sendSuccessFilePath.getParent());
                                }
                                Files.move(path, sendSuccessFilePath, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                logger.error(e.toString());
                            }
                            latch.countDown();
                        }
                );
            }

            latch.await();
        } catch (Exception e) {
            logger.error(e.toString());
        }

        long end = System.currentTimeMillis();
        logger.info("fileSend end，files:" + files.size() + "，send time:" + (end - start) / 1000.0);
    }

    private class FindVisitor extends SimpleFileVisitor<Path> {
        private List<Path> result;

        public FindVisitor(List<Path> result) {
            this.result = result;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            result.add(file);
            return FileVisitResult.CONTINUE;
        }
    }
}