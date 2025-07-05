package com.struts.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.models.User;
import com.struts.services.CaptchaService;
import com.struts.services.UserActionService;
import com.struts.util.CommonUtil;
import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

public class RegisterAction extends ActionSupport implements ServletRequestAware {

    private static final Logger logger = LoggerUtil.getLogger(RegisterAction.class);

    private HttpServletRequest request;

    private User user = new User();
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String gender;
    private String dob;

    public String captchaTxt;

    private String message;
    private boolean success;
    private boolean error;
    private boolean repeatRequest = false;

    @Override
    public String execute() throws Exception {
        // check if all the fields are valid
        if (!validateUserFields()) {
            return ERROR;
        }

        // check if CAPTCHA is valid
        CaptchaService captchaService = new CaptchaService();
        boolean isCaptchaValid = captchaService.validateCaptchaCode(captchaTxt, request);
        if (!isCaptchaValid) {
            logger.debug("Invalid CAPTCHA >> setting repeatRequest == true");
            setRepeatRequest(true);
            error = true;
            message = "Invalid CAPTCHA code, try again";
            return ERROR;
        }

        logger.debug("user object >> {}", user);
        user.setFirstName(this.fname);
        user.setLastName(this.lname);
        user.setEmail(this.email);
        user.setPassword(this.password);

        char genderChar = (this.gender == null || this.gender.trim().isEmpty()) ? 'O' : this.gender.charAt(0);
        user.setGender(genderChar);

        // Convert DOB string to LocalDate
        user.setDob(CommonUtil.parseDate(dob));

        logger.debug("user object value >>> {}", user);

        UserActionService userActionService = new UserActionService();
        Map<String, Object> result = userActionService.saveUser(user);

        // Reset form fields
        this.fname = null;
        this.lname = null;
        this.email = null;
        this.gender = null;
        this.password = null;
        this.dob = null;
        this.captchaTxt = null;

        if (result == null || result.get("success") == null) {
            error = true;
            message = "Something went wrong!";
            return ERROR;
        } else if (!(Boolean) result.get("success")) {
            error = true;
            message = (String) result.get("message");
            return ERROR;
        }

        success = true;
        message = (String) result.get("message");

        return SUCCESS;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCaptcha(String captchaTxt) {
        this.captchaTxt = captchaTxt;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isError() {
        return error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isRepeatRequest() {
        return repeatRequest;
    }

    public void setRepeatRequest(boolean repeatRequest) {
        this.repeatRequest = repeatRequest;
    }

    public boolean validateUserFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("User", user);
        fields.put("First name", fname);
        fields.put("Last name", lname);
        fields.put("Email", email);
        fields.put("Password", password);
        fields.put("Gender", gender);
        fields.put("Date of birth", dob);
        fields.put("captcha", captchaTxt);

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            if ("captcha".equals(entry.getKey()) && (entry.getValue() == null)) {
                error = true;
                message = "Invalid CAPTCHA code, try again";
                return false;
            }
            if (entry.getValue() == null) {
                error = true;
                message = entry.getKey() + " is required!";
                return false;
            }
        }

        return true;
    }

}

