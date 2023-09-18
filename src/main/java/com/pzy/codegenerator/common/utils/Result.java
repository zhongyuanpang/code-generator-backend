package com.pzy.codegenerator.common.utils;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.pzy.codegenerator.common.enums.ResultCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("返回前端数据信息")
public class Result<T> {

    @ApiModelProperty("是否成功")
    private boolean success;//是否成功
    @ApiModelProperty("返回码")
    private Integer code;//返回码
    @ApiModelProperty("返回信息")
    private String message;//返回信息
    @ApiModelProperty("返回数据")
    private T data;//返回数据

    public Result(ResultCode code) {
        this.success = code.success();
        this.code = code.code();
        this.message = code.message();
    }

    public Result(ResultCode code, T data) {
        this.success = code.success();
        this.code = code.code();
        this.message = code.message();
        this.data = data;
    }

    public Result(Integer code, String message, boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public Result() {
    }

    public Result setSuccessMsg(String msg, T data) {
        this.code = 200;
        this.message = msg;
        this.data = data;
        return this;
    }

    /**
     * 返回成功的实体
     *
     * @param obj
     */
    public Result(T obj) {
        this.code = 200;
        this.data = obj;
        this.success = true;
    }

    public static Result SUCCESS() {
        return new Result(ResultCode.SUCCESS);
    }

    public static Result FAIL() {
        return new Result(ResultCode.FAIL);
    }

    /**
     * 成功后返回message
     *
     * @param resultCode
     * @return
     */
    public static Result SUCCESS(ResultCode resultCode) {
        return new Result(resultCode);
    }

    /**
     * 失败后返回message
     *
     * @param resultCode
     * @return
     */
    public static Result FAIL(ResultCode resultCode) {
        return new Result(resultCode);
    }

    /*public static Result FAIL(ResultCode resultCode){

        return new Result(resultCode);
    }*/

    /**
     * 成功直接返回数据和状态
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> SUCCESS(T data, ResultCode resultCode) {
        return new Result<T>(data, resultCode);
    }

    /**
     * 返回成功的实体和提示信息
     *
     * @param obj
     */
    public Result(T obj, ResultCode resultCode) {
        this.code = 200;
        this.data = obj;
        this.success = true;
        this.message = resultCode.getMessage();
    }

    /**
     * 成功直接返回数据
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> SUCCESS(T data) {
        return new Result<T>(data);
    }


    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("data")
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
