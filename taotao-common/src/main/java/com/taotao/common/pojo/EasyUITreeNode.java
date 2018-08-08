package com.taotao.common.pojo;

import java.io.Serializable;

/**
 * 
 * @describe 
 * @author yxd
 * @data 2018-08-08 15:42:01
 *
 */
public class EasyUITreeNode implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private long id;
    private String text;
    private String state;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}
