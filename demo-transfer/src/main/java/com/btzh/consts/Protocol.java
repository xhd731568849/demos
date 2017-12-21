package com.btzh.consts;

import com.btzh.transfer.entity.*;

/**
 * 传输协议
 * @author tanzhibo
 * @date 2017/9/25
 */
public enum Protocol {
    /**
     * 明文
     */
    Text {
        @Override
        public String getProtocol() {
            return Text.class.getName();
        }
    },
    /**
     * 密文
     */
    EncryptedText {
        @Override
        public String getProtocol() {
            return EncryptedText.class.getName();
        }
    },
    /**
     * 明文文件
     */
    BaseFile {
        @Override
        public String getProtocol() {
            return BaseFile.class.getName();
        }
    },
    /**
     * 密文文件
     */
    EncryptedFile {
        @Override
        public String getProtocol() {
            return EncryptedFile.class.getName();
        }
    };

    public abstract String getProtocol();
}