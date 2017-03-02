package com.hl.util.spliter.cn;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.hl.misc.ServiceStatus;
import com.hl.util.spliter.Spliter;

public class ZH_CNSpliterHandler implements Spliter{
	private static String stopWordsList[] ={"的", "我们","要","自己","之","将","“","”","，","（","）","后","应","到","某","后","个","是","位","新","一","两","在","中","或","有","更","好",""};
	private static List<String> stopWordList;
	private static ZH_CNSpliterHandler instance;
	
	public static ZH_CNSpliterHandler getInstance(){
		if(instance == null){
			instance = new ZH_CNSpliterHandler();
			instance.reload();
		}
		return instance;
	}
	
	public static ZH_CNSpliterHandler getInstance(File file){
		if(instance == null && file.exists()){
			instance = new ZH_CNSpliterHandler();
			instance.reload(file);
		}
		return instance;
	}
	
	@Override
	public int reload(File file) {
		return 0;
	}

	@Override
	public int reload() {
		stopWordList = Arrays.asList(stopWordsList);
		return ServiceStatus.SUCCESS;
	}

	@Override
	public int remStopWord(String cxt) {
		return 0;
	}

	@Override
	public boolean isStopWord(String word) {
		stopWordList.contains(word);
		return false;
	}

}
