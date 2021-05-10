package net.xdclass.xdvideo.intercepter;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import net.xdclass.xdvideo.domain.JsonData;
import net.xdclass.xdvideo.utils.JwtUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginInterceptor implements HandlerInterceptor {
    private static final Gson gson = new Gson();

    //JDK1.8之前接口里面是不能写方法的，1.8后可以，就比如这个接口，即使不实现接口也不会报错

    /**
     * 进入Controller之前进行拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("token");
        if (token==null){
            token = request.getParameter("token");
        }
        if (token!=null){
            Claims claims = JwtUtils.checkJWT(token);
            if (claims!=null){
                Integer userId = (Integer)claims.get("id");
                String name = (String)claims.get("name");
                //因为可能在Controller中会用到获取到的参数，因此可以先保存到当前请求当中
                request.setAttribute("user_id",userId);
                request.setAttribute("name",name);

                return true;
            }
        }
        sendJsonMessage(response, JsonData.buildError("请登录"));
        return false;
    }

    /**
     * 响应数据给前端
     * @param response
     * @param obj
     */
    public static void sendJsonMessage(HttpServletResponse response,Object obj) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(gson.toJson(obj));
        writer.close();
        response.flushBuffer();
    }
}
