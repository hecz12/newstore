package com.store.web.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.store.utils.CookUtils;

public class CodeServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//设置不缓存图片  
        response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "No-cache");  
        response.setDateHeader("Expires", 0);  
		//使用java技术绘制图片，用到Graphis包
		int charName = 4;
		int width = 120;
		int height = 30;
		
		//绘制一张背景图
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//获取画笔
		Graphics  graphics = bufferedImage.getGraphics();
		
		//绘制背景颜色
		int r = (int)(255*Math.random());
		int g = (int)(255*Math.random());
		int b = (int)(255*Math.random());
		graphics.setColor(new Color(r, g, b));
		graphics.fillRect(0, 0, width, height);
		//绘制边框
		graphics.setColor(Color.BLUE);
		graphics.drawRect(0, 0, width - 1, height - 1);
		//输出字体
		graphics.setColor(Color.RED);
		graphics.setFont(new Font("宋体", Font.BOLD, 20));
		
		// 随机输出4个字符
			Graphics2D graphics2d = (Graphics2D) graphics;
			String s = "ABCDEFGHGKLMNPQRSTUVWXYZ23456789";
			Random random = new Random();
			//session中要用到
			String msg="";
			int x = 5;
			for (int i = 0; i < 4; i++) {
				int index = random.nextInt(32);
				String content = String.valueOf(s.charAt(index));
				msg+=content;
				double theta = random.nextInt(45) * Math.PI / 180;
				//让字体扭曲
	            graphics2d.rotate(theta, x, 18);
				graphics2d.drawString(content, x, 18);
				graphics2d.rotate(-theta, x, 18);
				x += 30;
			}
			request.getSession().setAttribute("ImageCode", msg);
			// 6、绘制干扰线
			graphics.setColor(Color.GRAY);
			for (int i = 0; i < 5; i++) {
				int x1 = random.nextInt(width);
				int x2 = random.nextInt(width);

				int y1 = random.nextInt(height);
				int y2 = random.nextInt(height);
				graphics.drawLine(x1, y1, x2, y2);
			}

			// 释放资源
			graphics.dispose();

			// 图片输出 ImageIO
			ImageIO.write(bufferedImage, "jpg", response.getOutputStream());
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}

}
