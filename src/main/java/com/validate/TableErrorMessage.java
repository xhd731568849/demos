package com.validate;

import java.util.List;

/**
 * Created by xuhandong on 2017/8/18/018.
 */
public class TableErrorMessage {
    private String tableId;
    private List<FieldErrorMessage> fieldErrorMessages;

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
