package com.validate;

import java.util.List;

/**
 * Created by xuhandong on 2017/8/18/018.
 */
public class TableErrorMessage {
    private String tableId;
    private String msg;
    private List<FieldErrorMessage> fieldErrorMessages;

    public TableErrorMessage(){}

    public TableErrorMessage(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public List<FieldErrorMessage> getFieldErrorMessages() {
        return fieldErrorMessages;
    }

    public void setFieldErrorMessages(List<FieldErrorMessage> fieldErrorMessages) {
        this.fieldErrorMessages = fieldErrorMessages;
    }
}
