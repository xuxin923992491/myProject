package net.xdclass.xdvideo.exception;

import net.xdclass.xdvideo.domain.JsonData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理控制器
 */
@ControllerAdvice//标记为处理异常的控制器
public class XdExceptionHandler {

    @ExceptionHandler(value = Exception.class)//处理这个value值得异常
    @ResponseBody
    public JsonData Handler(Exception e){
        if (e instanceof XdException){
            XdException xdException = (XdException) e;
            return JsonData.buildError(xdException.getMsg(),xdException.getCode());
        } else {
            return JsonData.buildError("全局异常，位置错误");
        }
    }
}
