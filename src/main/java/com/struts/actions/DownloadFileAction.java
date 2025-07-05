package com.struts.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.struts.models.User;
import com.struts.services.DownloadService;
import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DownloadFileAction extends ActionSupport {

    private final DownloadService downloadService = new DownloadService();

    private final Logger logger = LoggerUtil.getLogger(DownloadFileAction.class);

    public boolean downloadError = false;
    public String message;

    public boolean isDownloadError() {
        return downloadError;
    }

    public String getMessage() {
        return message;
    }

    public String execute() throws Exception {

        String filePath;
        try {
            filePath = downloadService.updateFileData(User.class);
            File file = new File(filePath);

            if (!file.exists()) {
                downloadError = true;
                message = "Download failed, File not found";
                logger.error("File not found >> filePath : [{}]", filePath);

                // Store message in session or request
                ActionContext.getContext().getSession().put("downloadError", true);
                ActionContext.getContext().getSession().put("message", message);
                return ERROR;
            }

            /*try (FileInputStream in = new FileInputStream(file);
                 ServletOutputStream out = response.getOutputStream()) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            } catch (IOException e) {
                logger.error("Error streaming file to response", e);
                return ERROR;
            }*/

            HttpServletResponse response = ServletActionContext.getResponse();
            try (FileInputStream inputStream = new FileInputStream(file);
                 ServletOutputStream outputStream = response.getOutputStream()) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=\"All-users-details.xlsx\"");

                inputStream.transferTo(outputStream); // for JDK 9 and above
                outputStream.flush();
            }
        } catch (Exception e) {
            logger.error("Error processing file download", e);
            downloadError = true;
            message = "Error occurred, please try again later";
            // Store message in session or request
            ActionContext.getContext().getSession().put("downloadError", true);
            ActionContext.getContext().getSession().put("message", message);
            return ERROR;
        }

        return NONE;
    }

}

