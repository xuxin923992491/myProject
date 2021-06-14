package net.xdclass.xdvideo.mapper;

import net.xdclass.xdvideo.domain.Video;
import net.xdclass.xdvideo.provider.VideoProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * video数据访问层
 */
@Repository
public interface VideoMapper {

    @Select("select * from video")
    List<Video> findAll();

    @Select("SELECT * FROM video WHERE id = #{id}")
    Video findById(int id);

    //@Update("UPDATE video SET title=#{title} WHERE id =#{id}")
    @UpdateProvider(type = VideoProvider.class,method = "updateVideo")
    int update(Video video);

    @Delete("DELETE FROM video WHERE id =#{id}")
    int delete(int id);

    @Insert("INSERT INTO `video` ( `title`, `summary`, " +
            "`cover_img`, `view_num`, `price`, `create_time`," +
            " `online`, `point`)" +
            "VALUES" +
            "(#{title}, #{summary}, #{coverImg}, #{viewNum}, #{price},#{createTime}" +
            ",#{online},#{point});")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")//前面的id是对象id，后面是数据库里面的id
    //上述注解可以获取到添加数据的自增id，否则在控制层想要输出id是不可以的
    int save(Video video);
}
