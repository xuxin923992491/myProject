package net.xdclass.xdvideo.controller.admin;

import net.xdclass.xdvideo.domain.Video;
import net.xdclass.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 增删改是只有管理员才能操作的
 */
@RestController
@RequestMapping("/admin/api/v1/video")
public class VideoAdminController {

    @Autowired
    private VideoService videoService;

    /**
     * 根据id删除视频
     * @param videoId
     * @return
     */
    @DeleteMapping("del_by_id")
    public Object delById(@RequestParam(value = "video_id",required = true)int videoId){
        return videoService.delete(videoId);
    }

    /**
     * 修改前端传过来的json信息
     * @param video json转为对象
     * @return
     */
    @PutMapping("update_by_id")
    public Object update(@RequestBody Video video){
        //使用上述注解后，就可以通过前端穿过来的json直接解析为Video对象，通常在postman里面写json
        return videoService.update(video);
    }

    /**
     * 保存对象
     * @param video 前端传来的json转为对象
     * @return
     */
    @PostMapping("save")
    public Object save(@RequestBody Video video){
        return videoService.save(video);
    }
}
