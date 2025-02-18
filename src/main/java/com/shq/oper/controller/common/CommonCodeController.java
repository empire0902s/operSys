package com.shq.oper.controller.common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shq.oper.controller.BaseController;
import com.shq.oper.service.primarydb.AdminService;
import com.shq.oper.util.Constant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 图片验证码
 * @author tjun
 */
@Api(tags = "验证码")
@RestController
@RequestMapping(value = {"/common/code"})
public class CommonCodeController extends BaseController {
	@Autowired
	AdminService adminService;
	
	
	@ApiOperation(value = "发送带图片验证码", notes = "发送带图片验证码")
	@GetMapping(value = "/image")
	public String image(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		// 将验证码存入页面KEY值的SESSION里面
		String randCode = fourRandomCode();
		session.setAttribute(Constant.IMG_CODE, randCode);
		ServletOutputStream responseOutputStream = response.getOutputStream();
		// 输出图象到页面
		outputImage(64, 30, responseOutputStream, randCode);
		// 以下关闭输入流！
		responseOutputStream.flush();
		responseOutputStream.close();
		return null;
	}

	/**
	 * 输出指定验证码图片流
	 * @param w
	 * @param h
	 * @param os
	 * @param code
	 * @throws IOException
	 */
	public void outputImage(int w, int h, OutputStream os, String code) throws IOException{
		int verifySize = code.length();
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Random rand = new Random(System.currentTimeMillis());
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		Color[] colors = new Color[5];
		Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN,
				Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
				Color.PINK, Color.YELLOW };
		float[] fractions = new float[colors.length];
		for(int i = 0; i < colors.length; i++){
			colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
			fractions[i] = rand.nextFloat();
		}
		Arrays.sort(fractions);
		g2.setColor(Color.GRAY);// 设置边框色
		g2.fillRect(0, 0, w, h);
		
		Color c = getRandColor(rand, 200, 250);
		g2.setColor(c);// 设置背景色
		g2.fillRect(0, 2, w, h-4);
		
		//绘制干扰线
		Random random = new Random();
		g2.setColor(getRandColor(rand, 160, 200));// 设置线条的颜色
		for (int i = 0; i < 20; i++) {
			int x = random.nextInt(w - 1);
			int y = random.nextInt(h - 1);
			int xl = random.nextInt(6) + 1;
			int yl = random.nextInt(12) + 1;
			g2.drawLine(x, y, x + xl + 40, y + yl + 20);
		}
		
		// 添加噪点
		float yawpRate = 0.05f;// 噪声率
		int area = (int) (yawpRate * w * h);
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(w);
			int y = random.nextInt(h);
			int rgb = getRandomIntColor(rand);
			image.setRGB(x, y, rgb);
		}
		
		shear(rand, g2, w, h, c);// 使图片扭曲
		g2.setColor(getRandColor(rand, 100, 160));
		int fontSize = h-4;
		Font font = new Font(Font.SERIF, Font.ITALIC, fontSize);
		g2.setFont(font);
		
		char[] chars = code.toCharArray();
		for(int i = 0; i < verifySize; i++){
			AffineTransform affine = new AffineTransform();
			affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (w / verifySize) * i + fontSize/2, h/2);
			g2.setTransform(affine);
			g2.drawChars(chars, i, 1, ((w-10) / verifySize) * i + 5, h/2 + fontSize/2 - 10);
		}
		g2.dispose();
		ImageIO.write(image, "PNG", os);
	}
	
	private Color getRandColor(Random random, int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	
	private int getRandomIntColor(Random random) {
		int[] rgb = getRandomRgb(random);
		int color = 0;
		for (int c : rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}
	
	private static int[] getRandomRgb(Random random) {
		int[] rgb = new int[3];
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

	private void shear(Random random, Graphics g, int w1, int h1, Color color) {
		shearX(random, g, w1, h1, color);
		shearY(random, g, w1, h1, color);
	}
	
	private static void shearX(Random random, Graphics g, int w1, int h1, Color color) {

		int period = random.nextInt(2);

		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);

		for (int i = 0; i < h1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
							+ (6.2831853071795862D * (double) phase)
							/ (double) frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}

	}

	private static void shearY(Random random, Graphics g, int w1, int h1, Color color) {

		int period = random.nextInt(40) + 10; // 50;

		boolean borderGap = true;
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < w1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period
							+ (6.2831853071795862D * (double) phase)
							/ (double) frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}

		}

	}
	private String fourRandomCode() {
		Random radmon = new Random();
		Integer num = radmon.nextInt(9999);
		while(num < 1000){
			num = radmon.nextInt(9999);
		}

		return num.toString();
	}
	private String sixRandomCode() {
		Random radmon = new Random();
		Integer num = radmon.nextInt(999999);
		while(num < 100000){
			num = radmon.nextInt(999999);
		}

		return num.toString();
	}

}
