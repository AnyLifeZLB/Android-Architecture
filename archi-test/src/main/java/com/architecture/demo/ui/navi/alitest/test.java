package com.architecture.demo.ui.navi.alitest;

import com.architecture.demo.http.beans.AliParam;

import java.util.ArrayList;
import java.util.List;

@Deprecated
class test {
//https://p8.itc.cn/q_70/images03/20220218/982b02f3d9d7448d8ae5bc0298f4d55c.jpeg
    public void test(){
        List<AliParam.Input.Message> messages=new ArrayList<>();
        List<AliParam.Input.Message.Content> contents=new ArrayList<>();
        contents.add(new AliParam.Input.Message.Content("https://p8.itc.cn/q_70/images03/20220218/982b02f3d9d7448d8ae5bc0298f4d55c.jpeg","这个图片是哪里？"));
        messages.add(new AliParam.Input.Message(contents,"user"));

        AliParam.Input input=new AliParam.Input(messages);
        AliParam aliParam=new AliParam(input,"qwen-vl-plus");


    }


}
