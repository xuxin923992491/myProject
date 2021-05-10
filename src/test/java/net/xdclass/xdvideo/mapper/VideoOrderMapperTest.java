package net.xdclass.xdvideo.mapper;

import net.xdclass.xdvideo.domain.VideoOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoOrderMapperTest {
    @Autowired
    private VideoOrderMapper videoOrderMapper;

    @Test
    public void insert() {
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setDel(0);
        videoOrder.setTotalFee(111);
        videoOrder.setHeadImg("xxxxxxxasdada");
        videoOrder.setVideoTitle("springboot高级");
        int insert = videoOrderMapper.insert(videoOrder);
        assertNotNull(videoOrder.getId());
    }

    @Test
    public void findById() {
        VideoOrder videoOrder = videoOrderMapper.findById(1);
        assertNotNull(videoOrder);
    }

    @Test
    public void findByOutTradeNp() {
    }

    @Test
    public void del() {
    }

    @Test
    public void findMyOrderList() {
    }

    @Test
    public void updateVideoOrderByOutTradeNo() {

        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setOpenid("oiNKG0xTHkUX0Op_desUm7hXV6AI");
        videoOrder.setOutTradeNo("f281ad4578be49d1a065b6a119d8e69f");
        videoOrder.setNotifyTime(new Date());
        videoOrder.setState(1);
        int rows = videoOrderMapper.updateVideoOrderByOutTradeNo(videoOrder);
        System.out.println(rows);
    }
}