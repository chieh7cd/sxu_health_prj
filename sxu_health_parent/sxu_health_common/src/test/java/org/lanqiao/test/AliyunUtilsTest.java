package org.lanqiao.test;

import org.junit.Test;
import org.lanqiao.utils.AliyunUtils;

public class AliyunUtilsTest {

//    @Test
    public void upload2Aliyun(){
        AliyunUtils.upload2Aliyun("D:\\15990\\MyFiles\\杂烩\\壁纸", "mn.jpg");
    }


//    @Test
    public void deleteFileFromAliyun(){
        AliyunUtils.deleteFileFromAliyun("mn.jpg");
    }

}
