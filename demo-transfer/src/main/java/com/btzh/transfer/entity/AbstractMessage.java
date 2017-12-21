package com.btzh.transfer.entity;

import com.btzh.config.Config;
import com.btzh.consts.BusinessType;
import com.btzh.consts.Protocol;
import io.netty.buffer.ByteBuf;

import java.io.Closeable;
import java.io.IOException;

/**
 * 消息基类
 * @author wanglidong
 * @date 17-10-22 下午2:57
 */
public abstract class AbstractMessage implements Closeable {

    public static final String SEND_SPEED_SECOND = "second";
    public static final String SEND_SPEED_MILLSECOND = "millsecond";

    /**
     * 协议头:协议ID
     */
    public static final int LEN_PROTOCOL_ID = 1;
    /**
     * 协议头:业务ID
     */
    public static final int LEN_BUSINESS_ID = 1;

    /**
     * 获取协议头长度
     *
     * @return
     */
    public int getHeaderLength() {
        return (SEND_SPEED_MILLSECOND.equals(Config.getConfig().getMessageSendSpeed()) ? 8 : 4) + LEN_PROTOCOL_ID + LEN_BUSINESS_ID;
    }

    /**
     * 获取消息体
     *
     * @return
     */
    public Object getBody() {
        return null;
    }

    /**
     * 编码
     *
     * @param out
     */
    public abstract void encode(ByteBuf out);

    /**
     * 解码
     *
     * @param in
     * @return true代表消息解码结束
     */
    public abstract boolean decode(ByteBuf in);

    protected Long time;
    protected Protocol protocol;
    protected BusinessType businessType;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public void close() throws IOException {
    }
}