package com.struts.services;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class TempService {
    private static final int WIDTH = 150;
    private static final int HEIGHT = 50;

    public static void main(String[] args) throws Exception {
        //createImageAndSave();
        createCaptchaImage();
    }

    public static BufferedImage createCaptchaImage() throws Exception {
        try {
            String captcha = getCaptchaText();

            int fontSize = 30;
            int xGap = 12;
            int yGap = 22;
            String fontName = "Arial";
            Color gradiantStartColor = Color.DARK_GRAY;
            Color gradiantEndColor = Color.DARK_GRAY;
            Color textColor = new Color(255, 255, 255);
            BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedImage.createGraphics();

            RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHints(rh);

            GradientPaint gp = new GradientPaint(0, 0, gradiantStartColor, 0, HEIGHT / 2, gradiantEndColor, true);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
            for (int i = 0; i < WIDTH - 10; i = i + 25) {
                int q = Math.abs(genrateSecureRandomNumber()) % WIDTH;
                int colorIndex = Math.abs(genrateSecureRandomNumber()) % 200;
                g2d.setColor(new Color(colorIndex, colorIndex, colorIndex));
                g2d.drawLine(i, q, WIDTH, HEIGHT);
                g2d.drawLine(q, i, i, HEIGHT);
            }
            g2d.setColor(textColor);

            int x = 0;
            int y = 0;
            for (int i = 0; i < captcha.length(); i++) {
                Font font = new Font(fontName, Font.BOLD, fontSize);
                g2d.setFont(font);
                x += xGap + (Math.abs(genrateSecureRandomNumber()) % 7);
                y = yGap + Math.abs(genrateSecureRandomNumber()) % 12;
                g2d.drawChars(captcha.toCharArray(), i, 1, x, y);
            }
            g2d.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);

            // Show in a JFrame
            JFrame frame = new JFrame("Image Viewer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(WIDTH + 20, HEIGHT + 40);

            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(bufferedImage, 0, 0, null);
                }
            };
            frame.add(panel);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int genrateSecureRandomNumber() {
        SecureRandom secureRandomGenerator = null;
        int sr = 0;
        try {
            // For windows machine
            if (System.getProperty("os.name").startsWith("Windows")) {
                secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
                sr = secureRandomGenerator.nextInt(1000000);
            }
            // for Linux machine
            else {
                secureRandomGenerator = SecureRandom.getInstance("NativePRNG");
                sr = secureRandomGenerator.nextInt(1000000);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sr;
    }

    public static void createImageAndSave() throws IOException {
        int width = 150;
        int height = 150;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, width, height);
        graphics2D.setColor(Color.BLACK); // Text color
        graphics2D.drawString("RAHUL", 50, 75);
        graphics2D.dispose();

        // Save to file
        ImageIO.write(image, "png", new File("rahul.png"));
        System.out.println("Image saved as rahul.png");
    }

    public static void createAndView() {
        int width = 150;
        int height = 50;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(Color.LIGHT_GRAY);
        graphics2D.fillRect(0, 0, width, height);
        graphics2D.setColor(Color.BLACK);

        // Draw some noise lines
        graphics2D.setColor(Color.BLACK);
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int x1 = rand.nextInt(width);
            int y1 = rand.nextInt(height);
            int x2 = rand.nextInt(width);
            int y2 = rand.nextInt(height);
            graphics2D.drawLine(x1, y1, x2, y2);
        }
        graphics2D.drawString(getCaptchaText(), 50, 30);
        graphics2D.dispose();

        // Show in a JFrame
        JFrame frame = new JFrame("Image Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width + 20, height + 40);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        frame.add(panel);
        frame.setVisible(true);
    }

    private static String getCaptchaText(){
        int captchaLength = 5;
        String chars = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789";
        int bound = chars.length();
        StringBuilder sb = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < captchaLength; i++) {
            char ch = chars.charAt(random.nextInt(bound));
            sb.append(ch);
        }
        return sb.toString();
    }
}

