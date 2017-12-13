package com.nio.netty.transfer.server;

import com.nio.netty.transfer.entity.EncryptedFile;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;

/**
 * Created by xuhandong on 17-9-26.
 */
public class EncryptedFileDecoder extends ByteToMessageDecoder {

    private String repository;
    private EncryptedFile encryptedFile = new EncryptedFile();
    private long readed = 0;

    public EncryptedFileDecoder(String repository){
        this.repository = repository;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        EncryptedFile encryptedFile = this.encryptedFile;

        if(null == encryptedFile.getType()){
            if(byteBuf.readableBytes()< EncryptedFile.LEN_TYPE){
                return;
            }
            encryptedFile.setType(EncryptedFile.Type.values()[byteBuf.readInt()]);
        }

        if(null == encryptedFile.getKey()){
            if(byteBuf.readableBytes() < EncryptedFile.LEN_KEY){
                return ;
            }
            encryptedFile.setKey(byteBuf.readBytes(EncryptedFile.LEN_KEY).toString(CharsetUtil.UTF_8));
        }

        if(null == encryptedFile.getLength()){
            if(byteBuf.readableBytes()<EncryptedFile.LEN_LENGTH){
                return;
            }
            encryptedFile.setLength(byteBuf.readLong());
        }

        if(null == encryptedFile.getFile()){
            File file = Files.createFile(Paths.get(repository,encryptedFile.getKey()) , PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rw-rw----"))).toFile();
        }

        long fileLength = encryptedFile.getLength().longValue();
        int readable = byteBuf.readableBytes();
        if(readable > 0 && readable<fileLength){
            list.add(encryptedFile);
            closeOutputStream();

            LoggerFactory.getLogger(EncryptedFileDecoder.class).info("Recieved"+encryptedFile.getType().name()+"at"+encryptedFile.getFile().getAbsolutePath()+", length : "+encryptedFile.getLength());
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LoggerFactory.getLogger(BusinessProcessor.class);
        closeOutputStream();
        ctx.close();
    }

    private void closeOutputStream() {
        OutputStream outputStream = encryptedFile.getOutputStream();
        if( null != outputStream){
            try{
                outputStream.close();
            }catch (IOException e){
                LoggerFactory.getLogger(EncryptedFileDecoder.class).error(e.getMessage(),e);
            }
        }
    }
}
