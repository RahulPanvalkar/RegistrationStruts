package com.struts.services;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class TempService {

    public static void main(String[] args) throws IOException {
        //createImageAndSave();
        createAndView();
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

