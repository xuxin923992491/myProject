package net.xdclass.xdvideo.provider;

import net.xdclass.xdvideo.domain.Video;
import org.apache.ibatis.jdbc.SQL;

import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;

/**
 * video构建动态sql语句
 */
public class VideoProvider {
    /**
     * 更新video动态语句,保证只更新传入值的字段，其他字段不变，否则就出现了把原来已经有的值赋值为null的情况了
     * @param video
     * @return
     */
    public String updateVideo(final Video video){
        return new SQL(){{
            //删除和新增同理
            UPDATE("video");//更新的表

            //条件写法.
            if(video.getTitle()!= null){
                SET("title=#{title}");
            }
            if(video.getSummary()!= null){
                SET("summary=#{summary}");
            }
            if(video.getCoverImg()!= null){
                SET("cover_img=#{coverImg}");
            }
            if(video.getViewNum()!= null){
                SET("view_num=#{viewNum}");
            }
            if(video.getPrice()!= null){
                SET("price=#{price}");
            }
            if(video.getOnline()!= null){
                SET("online=#{online}");
            }
            if(video.getPoint()!= null){
                SET("point=#{point}");
            }

            WHERE("id=#{id}");
        }}.toString();
    }
}
