package com.hl.util.spliter;

import java.io.File;

public interface Spliter {
	public int reload(File file);
	public int reload();
	public int remStopWord(String cxt);
	public boolean isStopWord(String word);
}
