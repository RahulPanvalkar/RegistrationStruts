package com.struts.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.struts.services.UserActionService;
import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

import java.util.Map;
public class GetAllRecords extends ActionSupport {
    private static final Logger logger = LoggerUtil.getLogger(GetAllRecords.class);

    private String page;
    private String size;
    private Map<String, Object> result;

    @Override
    public String execute() throws Exception {
        try {
            UserActionService userActionService = new UserActionService();
            this.result = userActionService.getUsers(page, size);

            if (result == null || result.get("success") == null) {
                return ERROR;
            } else if (!(Boolean) result.get("success")) {
                return ERROR;
            }
            return SUCCESS;
        } catch (Exception e) {
            logger.error("Error fetching user records", e);
            return ERROR;
        }
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}

