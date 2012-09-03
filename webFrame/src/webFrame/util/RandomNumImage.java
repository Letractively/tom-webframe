package webFrame.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import webFrame.report.Log;

/**
 * 验证码
 * @author Administrator
 *
 */
public class RandomNumImage {
	
	// 图像输入流
	private ByteArrayInputStream image;

	// 验证码
	private String str;

	private RandomNumImage() {
		// 初始化属性
		init();
	}

	// 取得RandomNumImage实例
	public static RandomNumImage getInstance() {
		return new RandomNumImage();
	}

	// 取得验证码图片
	public ByteArrayInputStream getImage() {
		return this.image;
	}
	

	// 取得图片的验证码
	public String getString() {
		return this.str;
	}
	
	private void init() {
		
		// 在内存中创建图象
		int width = 75, height = 24;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// 获取图形上下文
		Graphics g = image.getGraphics();
		
		// 生成随机类
		Random random = new Random();
		
		// 设定背景色
		g.setColor(getRandColor(200, 250));
		
		g.fillRect(0, 0, width, height);
		
		// 设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 22));
		
		g.setColor(getRandColor(160, 200));
		
		// 随机产生155条干扰线，使图象中的验证码不易被其它程序探测到
		for (int i = 0; i < 100; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		
		// 随机产生验证码(4位数字)
		String ranStr = "ABCDEFGHJKLmnPQRStuvwxyz23456789";
		String ranNum = "";
		for (int i = 0; i < 4; i++) {
			String ran = ranStr.charAt(random.nextInt(ranStr.length()))+"";
			ranNum += ran;
			
			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.setColor(new Color(20 + random.nextInt(100), 20 + random.nextInt(100), 20 + random.nextInt(110)));
			
			// 将验证码显示到图象中
			g.drawString(ran, 13 * i + 12, 20);
		}
		
		// 赋值验证码
		this.str = ranNum;

		// 图象生效
		g.dispose();
		
		ByteArrayInputStream input = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		
		try {
			ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
			ImageIO.write(image, "JPEG", imageOut);
			imageOut.close();
		} catch (IOException e) {
			Log.writeLog("验证码图片产生出现错误：" + e.toString());
		}
		
		input = new ByteArrayInputStream(output.toByteArray());
		
		// 赋值图像
		this.image = input;
		
	}

	// 给定范围获得随机颜色
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) fc = 255;
		if (bc > 255) bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	
}
