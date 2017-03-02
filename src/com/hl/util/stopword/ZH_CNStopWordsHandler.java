package com.hl.util.stopword;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.hl.misc.ServiceStatus;

public class ZH_CNStopWordsHandler implements StopWords{
	private static String stopWordsList[] ={"��", "����","Ҫ","�Լ�","֮","��","��","��","��","��","��","��","Ӧ","��","ĳ","��","��","��","λ","��","һ","��","��","��","��","��","��","��",""};
	private static List<String> stopWordList;
	private static ZH_CNStopWordsHandler instance;
	
	public static ZH_CNStopWordsHandler getInstance(){
		if(instance == null){
			instance = new ZH_CNStopWordsHandler();
			instance.reload();
		}
		return instance;
	}
	
	public static ZH_CNStopWordsHandler getInstance(File file){
		if(instance == null && file.exists()){
			instance = new ZH_CNStopWordsHandler();
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
