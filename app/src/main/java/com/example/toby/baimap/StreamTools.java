package com.example.toby.baimap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by toby on 2017/4/21.
 */

class StreamTools {
    /*
     * 把一个流里面的内容转换成一个字符串
     * return 流的字符串 null 解析失败
     * */
    public static String readStream(InputStream is){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = is.read(buffer))!=-1) {
                baos.write(buffer,0,len);
            }
            baos.close();
            return new String(baos.toByteArray());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

}
