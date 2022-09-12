package com.example.ordermanager.configuration.handler;

public class StandardObjectException {

    private static final long serialVersionUID = 1L;

    private Integer status;
    private String msg;
    private String path;
    private Long timestamp;

    public StandardObjectException(Integer status, String msg, String path, Long timestamp) {
        this.status = status;
        this.msg = msg;
        this.path = path;
        this.timestamp = timestamp;
    }
    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getMsg() {
        return msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }


    public Long getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
