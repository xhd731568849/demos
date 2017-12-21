package com.btzh.service.impl;

import com.btzh.service.FileProcessor;
import com.btzh.transfer.entity.BaseFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 文件处理
 * @author tanzhibo
 * @date 2017/9/25
 */
@Service("fileProcessor")
public class FileProcessorImpl implements FileProcessor {
    private final static Logger logger = LoggerFactory.getLogger(FileProcessorImpl.class);
    @Override
    public void process(BaseFile baseFile) {
        logger.info("Protocol:" + baseFile.getProtocol().name() + " - BusinessType:" +
                baseFile.getBusinessType().name() + " - FilePath: " + baseFile.getFilePath());
    }
}