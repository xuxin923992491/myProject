package net.xdclass.xdvideo.service.impl;

import net.xdclass.xdvideo.domain.Video;
import net.xdclass.xdvideo.mapper.VideoMapper;
import net.xdclass.xdvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    @Cacheable(value = "videoList",keyGenerator = "keyGenerator")
    public List<Video> findAll() {
        return videoMapper.findAll();
    }

    @Override
    //@Cacheable(value = "OneVideo",keyGenerator = "keyGenerator")
    public Video findById(int id) {
        return videoMapper.findById(id);
    }

    @Override
    public int update(Video video) {
        return videoMapper.update(video);
    }

    @Override
    public int delete(int id) {
        return videoMapper.delete(id);
    }

    @Override
    @CacheEvict(value = "videoList", allEntries=true)
    public int save(Video video) {
        int rows = videoMapper.save(video);
        //System.out.println("保存对象的id= " + video.getId());
        //如果不加上Option注解的话这个位置是无法返回新增行的id的
        return rows;
    }
}
