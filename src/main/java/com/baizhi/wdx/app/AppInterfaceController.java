package com.baizhi.wdx.app;


import com.baizhi.wdx.common.CommonResult;
import com.baizhi.wdx.service.VideoService;
import com.baizhi.wdx.util.AliyunSendPhoneUtil;
import com.baizhi.wdx.vo.VideoVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("app")
public class AppInterfaceController {

        @Resource
    VideoService videoService;
        @RequestMapping("getPhoneCode")
        public CommonResult getPhoneCode(String phone){
            String random = AliyunSendPhoneUtil.getRandom(6);
            System.out.println("存储验证码：："+random);

            String message = AliyunSendPhoneUtil.sendCode(phone,random);

            if(message.equals("发送成功")){
                return new CommonResult().success("100",message,phone);
            }else {
                return new CommonResult().failed("发送失败:"+message,null);
            }
        }

    @RequestMapping("queryByReleaseTime")
    public CommonResult queryByReleaseTime(){
        try {
            //查询数据
            List<VideoVo> videoVos = videoService.queryByReleaseTime();
            return new CommonResult().success(videoVos);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult().failed();
        }
    }
}
