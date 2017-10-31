package com.java2word;

/**
 * @author xhd
 * @date 2017/10/31
 */
public class Catalog {
    private String restitle;
    private String deptName;
    private String resProviderDeptName;
    private String resProviderCode;
    private String resFormartClassify;
    private String resFormartType;
    private String otherTypeResFormatDesc;
    private String shareStateText;
    private String isOpenText;
    private String resUpdateFrequencyText;
    private Integer rn;

    public Integer getRn() {
        return rn;
    }

    public void setRn(Integer rn) {
        this.rn = rn;
    }

    public String getResFormartType() {
        return resFormartType;
    }

    public void setResFormartType(String resFormartType) {
        this.resFormartType = resFormartType;
    }

    public String getRestitle() {
        return restitle;
    }

    public void setRestitle(String restitle) {
        this.restitle = restitle;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getResProviderDeptName() {
        return resProviderDeptName;
    }

    public void setResProviderDeptName(String resProviderDeptName) {
        this.resProviderDeptName = resProviderDeptName;
    }

    public String getResProviderCode() {
        return resProviderCode;
    }

    public void setResProviderCode(String resProviderCode) {
        this.resProviderCode = resProviderCode;
    }

    public String getResFormartClassify() {
        return resFormartClassify;
    }

    public void setResFormartClassify(String resFormartClassify) {
        this.resFormartClassify = resFormartClassify;
    }

    public String getOtherTypeResFormatDesc() {
        return otherTypeResFormatDesc;
    }

    public void setOtherTypeResFormatDesc(String otherTypeResFormatDesc) {
        this.otherTypeResFormatDesc = otherTypeResFormatDesc;
    }

    public String getShareStateText() {
        return shareStateText;
    }

    public void setShareStateText(String shareStateText) {
        this.shareStateText = shareStateText;
    }

    public String getIsOpenText() {
        return isOpenText;
    }

    public void setIsOpenText(String isOpenText) {
        this.isOpenText = isOpenText;
    }

    public String getResUpdateFrequencyText() {
        return resUpdateFrequencyText;
    }

    public void setResUpdateFrequencyText(String resUpdateFrequencyText) {
        this.resUpdateFrequencyText = resUpdateFrequencyText;
    }
}
