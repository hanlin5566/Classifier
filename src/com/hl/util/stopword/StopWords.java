package com.hl.util.stopword;

import java.io.File;

public interface StopWords {
	public int reload(File file);
	public int reload();
	public int remStopWord(String cxt);
	public boolean isStopWord(String word);
}
