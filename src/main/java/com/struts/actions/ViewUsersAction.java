package com.struts.actions;


import com.opensymphony.xwork2.ActionSupport;
import com.struts.dao.UserDao;
import com.struts.models.User;
import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ViewUsersAction extends ActionSupport {
    private static final long serialVersionUID = 1L;

    private final Logger logger = LoggerUtil.getLogger(ViewUsersAction.class);

    private final UserDao userDao = new UserDao();

    private List<User> users;
    private int currentPage = 1;
    private int totalPages;
    private boolean success;

    public void setPage(int page) {
        this.currentPage = page;
    }


    public List<User> getUsers() {
        return users;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String execute() {

        logger.debug("currentPage : [{}]",currentPage);
        try {
            int usersPerPage = 10;

            List<User> allUsers = userDao.getAllUser();
            int totalUsers = allUsers.size();
            totalPages = (int) Math.ceil((double) totalUsers / usersPerPage);
            logger.debug("totalPages : [{}]",totalPages);

            int startIndex = (currentPage - 1) * usersPerPage;
            users = allUsers.subList(startIndex, Math.min(startIndex + usersPerPage, totalUsers));

            success = !users.isEmpty();
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
            return ERROR;
        }
    }
}

