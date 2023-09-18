package com.pzy.codegenerator.common.enums;

public enum ResultCode {

    SUCCESS(true, 200, "操作成功！"),
    FAIL(true, 400, "操作失败！");

    boolean success;
    int code;
    String message;

    ResultCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    ResultCode(String msg) {
        this.message = msg;
        this.success = true;
        this.code = 200;
    }

    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

