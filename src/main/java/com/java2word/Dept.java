package com.java2word;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xhd
 * @date 2017/10/31
 */
public class Dept {
    private String deptName;

    private List<Catalog> resList = new ArrayList<Catalog>();

    private Integer rn;

    private String rnText;

    public Integer getRn() {
        return rn;
    }

    public void setRn(Integer rn) {
        this.rn = rn;
    }

    public String getRnText() {
        return rnText;
    }

    public void setRnText(String rnText) {
        this.rnText = rnText;
    }

    public List<Catalog> getResList() {
        return resList;
    }

    public void setResList(List<Catalog> resList) {
        this.resList = resList;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
