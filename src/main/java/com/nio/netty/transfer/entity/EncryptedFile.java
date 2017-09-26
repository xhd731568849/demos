package com.nio.netty.transfer.entity;

import java.beans.Transient;
import java.io.File;
import java.io.OutputStream;

/**
 * Created by xuhandong on 17-9-25.
 */
public class EncryptedFile {
    public enum Type{
        FILE,
        SQL,
        CLOB,
        BLOB
    }
    public static final int LEN_TYPE =32;
    public static final int LEN_KEY=32;
    public static final int LEN_LENGTH = 8;

    private Type type;
    private String key;
    private Long length;
    private File file;

    private OutputStream outputStream;

    public EncryptedFile(){}

    public EncryptedFile(String key ,File file){
        this(Type.FILE,key,file);
    }

    public EncryptedFile(Type type ,String key ,File file){
        this.type = type ;
        this.key = key;
        this.file = file;
    }

    public EncryptedFile(String key ,Long length ,File file){
        this(Type.FILE,key,length,file);
    }

    public EncryptedFile(Type type , String key ,Long length ,File file){
        this.type = type ;
        this.key = key;
        this.length = length;
        this.file = file;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    @Transient
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Transient
    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
