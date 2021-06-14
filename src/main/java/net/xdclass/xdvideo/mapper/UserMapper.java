package net.xdclass.xdvideo.mapper;

import net.xdclass.xdvideo.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    /**
     * 根据主键id查找
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{id}")//${}会产生sql注入
    User findById(@Param("id") int userId);//注意，如果int这个参数名和sql语句中的#{}中的参数名不一样，要加该注解

    /**
     * 根据openid找用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")//${}会产生sql注入
    User findByOpenId(@Param("openid") String openid);//注意，如果int这个参数和sql语句中的参数名不一样，要加该注解

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    @Insert("INSERT INTO `user` ( `openid`,`name`,`head_img`,`phone`,`sign`,`sex`,`city`,`create_time`)"+
    "VALUES"+
    "(#{openid},#{name},#{headImg},#{phone},#{sign},#{sex},#{city},#{createTime});")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")//该注释是可以获取插入数据的主键信息的
    int save(User user);
}
