package com.struts.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.services.UserActionService;
import com.struts.models.User;
import com.struts.util.CommonUtil;
import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

public class UpdateUserAction extends ActionSupport {

    private static Logger logger = LoggerUtil.getLogger(UpdateUserAction.class);

    private User user = new User();
    private String userid;
    private String fname;
    private String lname;
    private String email;
    private String dob;
    private String gender;

    private String message;
    private boolean success;
    private boolean error = false;

    private String formattedDob;

    @Override
    public String execute() throws Exception {
        // check if all the fields are valid
        if (!validateUserFields()) {
            error = true;
            return ERROR;
        }

        logger.debug("user object >> {}",user);
        user.setUserId(Integer.parseInt(this.userid));
        user.setFirstName(this.fname);
        user.setLastName(this.lname);
        user.setEmail(this.email);

        char genderChar = (this.gender == null || this.gender.trim().isEmpty()) ? 'O' : this.gender.charAt(0);
        user.setGender(genderChar);

        // Convert DOB string to LocalDate
        user.setDob(CommonUtil.parseDate(dob));

        logger.debug("user object value >>> {}", user);

        UserActionService helper = new UserActionService();
        Map<String, Object> result = helper.updateUser(user);

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public boolean validateUserFields() {
        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("User", user);
        fields.put("First name", fname);
        fields.put("Last name", lname);
        fields.put("Email", email);
        fields.put("Gender", gender);
        fields.put("Date of birth", dob);

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            if (entry.getValue() == null) {
                error = true;
                message = entry.getKey() + " is required!";
                return false;
            }
        }

        return true;
    }
}
