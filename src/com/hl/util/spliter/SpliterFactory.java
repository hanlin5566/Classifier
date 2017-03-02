package com.hl.util.spliter;

import java.io.File;

import com.hl.misc.LangConst;
import com.hl.util.spliter.cn.ZH_CNSpliterHandler;

public class SpliterFactory {
	public static Spliter getStopWordInstance(String lang){
		Spliter handler = null;
		switch (lang) {
		case LangConst.LANGUAGE_COUNTRY_ZHCN:
			handler = ZH_CNSpliterHandler.getInstance();
			break;

		default:
			break;
		}
		return handler;
	}
	
	public static Spliter getStopWordInstance(String lang,File file){
		Spliter handler = null;
		switch (lang) {
		case LangConst.LANGUAGE_COUNTRY_ZHCN:
			handler = ZH_CNSpliterHandler.getInstance(file);
			break;

		default:
			break;
		}
		return handler;
	}
}
