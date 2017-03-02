package com.hl.util.spliter.cn.mmseg4j.example;

import java.io.IOException;

import com.hl.util.spliter.cn.mmseg4j.MaxWordSeg;
import com.hl.util.spliter.cn.mmseg4j.Seg;

public class MaxWord extends Complex {

	protected Seg getSeg() {

		return new MaxWordSeg(dic);
	}

	public static void main(String[] args) throws IOException {
		new MaxWord().run(args);
	}
}
