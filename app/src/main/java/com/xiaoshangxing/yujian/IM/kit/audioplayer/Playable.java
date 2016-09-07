package com.xiaoshangxing.yujian.IM.kit.audioplayer;

public interface Playable {
	long getDuration();
	String getPath();
	boolean isAudioEqual(Playable audio);
}