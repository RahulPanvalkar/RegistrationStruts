package com.struts.services;

import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class CaptchaService {
    private final Logger logger = LoggerUtil.getLogger(CaptchaService.class);

    private static final int WIDTH = 150;
    private static final int HEIGHT = 50;
    private static final int CAPTCHA_LENGTH = 5;

    public BufferedImage createCaptchaImage(HttpServletRequest request) throws Exception {
        // Generate random 5-letter captcha text
        String captchaText = generateRandomText();
        logger.debug("captchaText : [{}]", captchaText);

        // Store it in session for validation
        HttpSession session = request.getSession();
        session.setAttribute("captcha", captchaText);

        // Create image with text
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        // Background color
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, WIDTH, HEIGHT);

        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHints(rh);

        // Draw some noise lines
        graphics2D.setColor(Color.LIGHT_GRAY);
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int x1 = rand.nextInt(WIDTH);
            int y1 = rand.nextInt(HEIGHT);
            int x2 = rand.nextInt(WIDTH);
            int y2 = rand.nextInt(HEIGHT);
            graphics2D.drawLine(x1, y1, x2, y2);
        }

        // Draw the captcha text
        graphics2D.setFont(new Font("Arial", Font.BOLD, 30));
        graphics2D.setColor(Color.WHITE);
        FontMetrics fm = graphics2D.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(captchaText)) / 2;  // center the text horizontally
        int y = ((HEIGHT - fm.getHeight()) / 2) + fm.getAscent();   // center the text vertically
        graphics2D.drawString(captchaText, x, y);
        graphics2D.dispose();

        return bufferedImage;
    }

    // Method to generate random text for CAPTCHA
    private String generateRandomText() {
        String chars = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < CAPTCHA_LENGTH; i++) {
            char ch = chars.charAt(random.nextInt(chars.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    // Method to validate captcha code
    public boolean validateCaptchaCode(String captcha, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String captchaTxt = (String) session.getAttribute("captcha");
        logger.debug("captcha : [{}], captchaTxt : [{}]", captcha, captchaTxt);
        return captcha.equals(captchaTxt);
    }

}
