package com.struts.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.services.CaptchaService;
import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

public class CaptchaAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private final Logger logger = LoggerUtil.getLogger(CaptchaAction.class);

    private HttpServletRequest request;
    private HttpServletResponse response;

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    private static final int WIDTH = 150;
    private static final int HEIGHT = 50;

    @Override
    public String execute() throws Exception {
        try {
            CaptchaService captchaService = new CaptchaService();
            BufferedImage captchaImage = captchaService.createCaptchaImage(request);

            // Write the image as PNG to response
            response.setContentType("image/png");
            // for no-cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            OutputStream out = response.getOutputStream();
            ImageIO.write(captchaImage, "png", out);
            out.flush();      // ensure all bytes sent
            out.close();      // close the stream

        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return NONE because response is handled
        return null;
    }

}
