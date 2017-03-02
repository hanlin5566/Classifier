package com.hl.util.stopword;

import java.io.File;

import com.hl.misc.LangConst;

public class StopWordFactory {
	public static StopWords getStopWordInstance(String lang){
		StopWords handler = null;
		switch (lang) {
		case LangConst.LANGUAGE_COUNTRY_ZHCN:
			handler = ZH_CNStopWordsHandler.getInstance();
			break;

		default:
			break;
		}
		return handler;
	}
	
	public static StopWords getStopWordInstance(String lang,File file){
		StopWords handler = null;
		switch (lang) {
		case LangConst.LANGUAGE_COUNTRY_ZHCN:
			handler = ZH_CNStopWordsHandler.getInstance(file);
			break;

		default:
			break;
		}
		return handler;
	}
}
