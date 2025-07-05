package com.struts.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.services.UserActionService;
import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class DeleteUserAction extends ActionSupport {
    private static final Logger logger = LoggerUtil.getLogger(DeleteUserAction.class);

    private String userid;
    private Map<String, Object> result;

    @Override
    public String execute() throws Exception {
        logger.debug("userid value >>> [{}]", userid);

        UserActionService userActionService = new UserActionService();
        this.result = userActionService.deleteUser(userid);

        if (result == null || result.get("success") == null) {
            return ERROR;
        } else if (!(Boolean) result.get("success")) {
            return ERROR;
        }
        return SUCCESS;
    }

    public Map<String, Object> getResult() {  // This will be serialized to JSON
        return result;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
