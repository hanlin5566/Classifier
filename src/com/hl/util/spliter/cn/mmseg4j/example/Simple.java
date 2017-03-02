package com.hl.util.spliter.cn.mmseg4j.example;

import java.io.IOException;

import com.hl.util.spliter.cn.mmseg4j.Seg;
import com.hl.util.spliter.cn.mmseg4j.SimpleSeg;

/**
 * 
 * @author chenlb 2009-3-14 上午12:38:40
 */
public class Simple extends Complex {
	
	protected Seg getSeg() {

		return new SimpleSeg(dic);
	}

	public static void main(String[] args) throws IOException {
		new Simple().run(args);
	}

}
