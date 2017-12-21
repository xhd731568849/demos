package com.btzh.consts;

/**
 * 业务类型
 * @author tanzhibo
 * @date 2017/10/22 15:03.
 */
public enum BusinessType {
    /**
     * 公钥传输
     */
    PUBLIC_KEY_FILE {
        @Override
        public String getProcessor() {
            return "publicKeyFileProcessor";
        }
    },
    /**
     * sql传输
     */
    SQL {
        @Override
        public String getProcessor() {
            return "sqlProcessor";
        }
    },
    /**
     * 文件传输
     */
    FILE {
        @Override
        public String getProcessor() {
            return "fileProcessor";
        }
    };

    public abstract String getProcessor();
}