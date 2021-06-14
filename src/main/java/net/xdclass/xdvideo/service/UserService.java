package net.xdclass.xdvideo.service;

import net.xdclass.xdvideo.domain.User;
import org.springframework.stereotype.Repository;

/**
 * 用户业务接口类
 */
@Repository
public interface UserService {
    User saveWechatUser(String code);
}
