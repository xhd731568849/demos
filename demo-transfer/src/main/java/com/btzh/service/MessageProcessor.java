package com.btzh.service;

import com.btzh.transfer.entity.AbstractMessage;

/**
 * 消息处理类
 * @author 王利东
 * @date 2017/9/25
 */
public interface MessageProcessor<T extends AbstractMessage> {

    /**
     * 消息处理
     * @param t
     */
    void process(T t);
}