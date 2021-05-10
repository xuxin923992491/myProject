package net.xdclass.xdvideo.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.xdclass.xdvideo.domain.Video;
import net.xdclass.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 普通人只能查
 */
@RestController
@RequestMapping("/api/v1/video")
//@CrossOrigin
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 分页接口
     * @param page 当前第几页，默认是第一页
     * @param size 每页显示几条
     * @return
     */
    @GetMapping("page")
    public Object pageVideo(@RequestParam(value = "page",defaultValue = "1")int page,
                            @RequestParam(value = "size",defaultValue = "10")int size){
        //分页查询的核心原理是底层插入了一个拦截器，在原来sql语句基础上加上limit
        PageHelper.startPage(page,size);//相当于默认1页10个

        List<Video> list = videoService.findAll();
        //下面的方法能够实现分页后输出分页的详细信息（很多条信息，比如总数，是不是最后一页等很多）
        PageInfo<Video> pageInfo = new PageInfo<>(list);
        //将pageInfo中的一些常用信息拿出来放到map当中，遍历集合
        Map<String,Object> data = new HashMap<>();
        data.put("total_size",pageInfo.getTotal());//总条数
        data.put("total_page",pageInfo.getPages());//总页数
        data.put("current_page",page);//当前页
        data.put("data",pageInfo.getList());//数据

        return data;
        //return "hello world";
    }

    /**
     * 根据id找视频
     * @param videoId
     * @return
     */
    @GetMapping("find_by_id")
    public Object findById(@RequestParam(value = "video_id",required = true)int videoId){
        return videoService.findById(videoId);
    }

}
