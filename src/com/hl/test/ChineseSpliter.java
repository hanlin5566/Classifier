package com.hl.test;
import java.io.IOException;

import com.hl.util.spliter.cn.mmseg4j.example.Complex;      

/**
* 中文分词器
*/
public class ChineseSpliter 
{
    /**
    * 对给定的文本进行中文分词
    * @param text 给定的文本
    * @param splitToken 用于分割的标记,如"|"
    * @return 分词完毕的文本
    */
    public static String split(String text,String splitToken)
    {
        String result = null;
        Complex analyzer = new Complex();      
        try      
        {
            result = analyzer.segWords(text, splitToken);    
        }      
        catch (IOException e)      
        {     
            e.printStackTrace();     
        }     
        return result;
    }
}