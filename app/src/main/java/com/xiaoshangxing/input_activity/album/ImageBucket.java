package com.xiaoshangxing.input_activity.album;

import java.io.Serializable;
import java.util.List;

public class ImageBucket implements Serializable{
	public int count = 0;
	public String bucketName;
	public List<ImageItem> imageList;

}
