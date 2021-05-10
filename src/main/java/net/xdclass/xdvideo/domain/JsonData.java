package net.xdclass.xdvideo.domain;

/**
 * 包装类，对于前面的业务，响应回页面不应该只是方法返回值，应当还有其他的，比如状态码、相关描述等其他信息
 * 因此该包装类包装的是控制层的返回值类型
 * 前端会根据状态码来确定后端是否处理成功，成功的话就获取数据，否则其他
 */
public class JsonData {

    private static final long serialVersionUID = 1L;

    private Integer code;//状态码 0 表示成功，1表示处理中，-1表示失败
    private Object data;//数据
    private String msg;//描述

    public JsonData(){}

    public JsonData(Integer code,Object data,String msg){
        this.code = code;
        this.data = data;
        this.msg = msg;
    }
    //成功，传入数据
    public static JsonData buildSuccess(){return new JsonData(0,null,null);}
    //成功，传入数据
    public static JsonData buildSuccess(Object data){return new JsonData(0,data,null);}
    //失败，传入描述信息
    public static JsonData buildError(String msg){return new JsonData(-1,null,msg);}
    //失败，传入描述信息，状态码
    public static JsonData buildError(String msg,Integer code){return new JsonData(code,null,msg);}
    //成功，传入数据以及描述信息
    public static JsonData buildSuccess(Object data,String msg){return new JsonData(0,data,msg);}
    //成功，传入数据以及状态码
    public static JsonData buildSuccess(Object data,int code){return new JsonData(code,data,null);}

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "JsonData{" +
                "code=" + code +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
