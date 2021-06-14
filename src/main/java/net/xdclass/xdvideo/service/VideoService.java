package net.xdclass.xdvideo.service;

import net.xdclass.xdvideo.domain.Video;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 视频业务类接口
 */
public interface VideoService {
    List<Video> findAll();

    Video findById(int id);

    int update(Video Video);

    int delete(int id);

    int save(Video video);

}
