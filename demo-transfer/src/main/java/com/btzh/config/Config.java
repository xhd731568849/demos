package com.btzh.config;

import com.btzh.Main;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * config
 * @author wanglidong
 * @date 2017/10/22 15:03.
 */
@Service("config")
public final class Config {

    public static Config getConfig() {
        return (Config) Main.getApplicationContext().getBean("config");
    }

    @Value("${encrypted}")
    private Boolean encrypted;
    @Value("${local.host}")
    private String localHost;
    @Value("${local.port}")
    private Integer localPort;
    @Value("${remote.host}")
    private String remoteHost;
    @Value("${remote.port}")
    private Integer remotePort;
    @Value("${repository}")
    private String repository;
    @Value("${message.send.speed}")
    /**
     * second->秒，millsecond->毫秒，未配置不做服务端重放攻击防护和客户端发送频率限制，这个配置在客户端和服务端要一致
     */
    private String messageSendSpeed;
    @Value("${message.expire.time}")
    /**
     * 毫秒
     */
    private Integer messageExpireTime;
    @Value("${file.chunk.size}")
    private Integer fileChunkSize;
    @Value("${business.thread.poolSize}")
    private Integer businessThreadPoolSize;
    @Value("${local.file.path}")
    private String localFilePath;
    @Value("${remote.file.path}")
    private String remoteFilePath;
    @Value("${db.page.size}")
    private Integer pageSize;

    public Boolean getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
    }

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String localHost) {
        this.localHost = localHost;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

    public String getMessageSendSpeed() {
        return messageSendSpeed;
    }

    public void setMessageSendSpeed(String messageSendSpeed) {
        this.messageSendSpeed = messageSendSpeed;
    }

    public Integer getMessageExpireTime() {
        return messageExpireTime;
    }

    public void setMessageExpireTime(Integer messageExpireTime) {
        this.messageExpireTime = messageExpireTime;
    }

    public Integer getFileChunkSize() {
        return fileChunkSize;
    }

    public void setFileChunkSize(Integer fileChunkSize) {
        this.fileChunkSize = fileChunkSize;
    }

    public Integer getBusinessThreadPoolSize() {
        return businessThreadPoolSize;
    }

    public void setBusinessThreadPoolSize(Integer businessThreadPoolSize) {
        this.businessThreadPoolSize = businessThreadPoolSize;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    public void setRemoteFilePath(String remoteFilePath) {
        this.remoteFilePath = remoteFilePath;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}