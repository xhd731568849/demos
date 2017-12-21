package com.btzh.transfer.entity;

import com.btzh.consts.BusinessType;
import com.btzh.consts.Protocol;
import com.btzh.util.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.CharsetUtil;

/**
 * Text
 * @author wanglidong
 * @date 2017/9/25
 */
public class Text extends AbstractMessage {

    public static final int LEN_TEXT_LENGTH = 4;

    @Override
    public int getHeaderLength() {
        return super.getHeaderLength() + LEN_TEXT_LENGTH + this.textLength;
    }

    @Override
    public void encode(ByteBuf out) {
        out.writeInt(this.textLength);
        out.writeBytes(Unpooled.copiedBuffer(this.text, CharsetUtil.UTF_8));
    }

    @Override
    public boolean decode(ByteBuf in) {
        if (null == this.textLength) {
            if (in.readableBytes() < LEN_TEXT_LENGTH) {
                return false;
            }
            this.textLength = in.readInt();
        }
        if (null == this.text) {
            if (in.readableBytes() < this.textLength) {
                return false;
            }
            ByteBuf byteBuf = in.readBytes(this.textLength);
            this.text = byteBuf.toString(CharsetUtil.UTF_8);
            byteBuf.release();
        }

        if (in.isReadable()) {
            throw new TooLongFrameException("Frame too big!");
        }

        return true;
    }

    protected Integer textLength;
    protected String text;

    public Text() {
        this.protocol = Protocol.Text;
    }

    public Text(BusinessType businessType, String text) {
        this.protocol = Protocol.Text;
        this.businessType = businessType;
        this.textLength = StringUtil.getByteLength(text);
        this.text = text;
    }

    public Integer getTextLength() {
        return textLength;
    }

    public void setTextLength(Integer textLength) {
        this.textLength = textLength;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}