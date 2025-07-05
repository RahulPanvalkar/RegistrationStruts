package com.struts.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.services.UserActionService;
import com.struts.models.User;
import com.struts.util.CommonUtil;
import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class EditUserAction extends ActionSupport {
    private static final Logger logger = LoggerUtil.getLogger(EditUserAction.class);

    private User user;
    private String userid;

    private String message;
    private boolean success;
    private boolean error;

    private String formattedDob;

    @Override
    public String execute() throws Exception {
        logger.debug("user object >> {}",user);
        logger.debug("userid value >>> [{}]", userid);

        UserActionService helper = new UserActionService();
        Map<String, Object> result = helper.getUser(userid);

        if (result == null || result.get("success") == null) {
            error = true;
            message = "Something went wrong!";
            return ERROR;
        } else if (!(Boolean) result.get("success")) {
            error = true;
            message = (String) result.get("message");
            return ERROR;
        }
        logger.debug("formattedDob : [{}]",formattedDob);
        this.user = (User)result.get("user");
        // set formatted dob
        formattedDob = CommonUtil.convertToString(user.getDob());

        success = true;
        message = (String) result.get("message");
        return SUCCESS;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getFormattedDob() {
        return formattedDob;
    }

    public void setFormattedDob(String formattedDob) {
        this.formattedDob = formattedDob;
    }
}
