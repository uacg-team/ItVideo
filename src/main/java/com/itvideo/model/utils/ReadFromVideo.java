package com.itvideo.model.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

public class ReadFromVideo {

	public static void main(String[] args) throws IOException, JCodecException {
		int frameNumber = 50;
		Picture picture = FrameGrab.getFrameFromFile(new File("C:/res/1/videos/1.mp4"), frameNumber);
		
		BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
		ImageIO.write(bufferedImage, "png", new File("C:/res/1/videos/frame50.png"));
	}
	
}
