package com.struts.services;

import com.struts.dao.UserDao;
import com.struts.models.User;
import com.struts.models.UserDTO;
import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class UserActionService {
    private static UserDao userDao = new UserDao();

    private static final Logger logger = LoggerUtil.getLogger(UserActionService.class);

    // method to save new user
    public Map<String, Object> saveUser(User user) {
        logger.debug("user object >>> " + user);
        Map<String, Object> result = new HashMap<>();
        try {
            // check if user input is valid
            String[] valResult = isAllDataValid(user, false);

            if ("false".equals(valResult[0])) {
                result.put("success", false);
                result.put("message", valResult[1]);
                return result;
            }

            userDao.saveUser(user);

            // get saved user from DB with userId
            Optional<User> savedUserOp = userDao.getUser(user.getEmail());

            savedUserOp.ifPresent(value -> result.put("user", value));

            result.put("success", true);
            result.put("message", "Registration Successful");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
            return result;
        }
    }

    // method to update existing user details
    public Map<String, Object> updateUser(User user) {
        logger.debug("UserServletHelper >> updateUser >>> " + user);
        Map<String, Object> result = new HashMap<>();
        try {

            // check if user input is valid
            String[] valResult = isAllDataValid(user, true);

            if ("false".equals(valResult[0])) {
                result.put("success", false);
                result.put("message", valResult[1]);
                return result;
            }

            userDao.updateUser(user);

            // get updated user from DB with userId
            /*
             * Optional<User> updateUserOp = userDao.getUser(user.getEmail());
             *
             * if (!updateUserOp.isEmpty()) { result.put("user", updateUserOp.get()); }
             */

            result.put("success", true);
            result.put("message", "User details updated successfully");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
            return result;
        }
    }

    // method to get user data
    public Map<String, Object> getUser(String username) {
        logger.debug("username: [{}]", username);
        Map<String, Object> result = new HashMap<>();
        try { // getting users details from DB
            Optional<User> userOp = userDao.getUser(username);
            if (userOp.isPresent()) {
                result.put("success", true);
                result.put("user", userOp.get());
                return result;
            }
            result.put("success", false);
            result.put("message", "User data fetched successfully");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
            return result;
        }
    }

    // method to get user data (paginated)
    public Map<String, Object> getUsers(String page, String size) {
        int pageNumber = 1;       // default to page 1
        int usersPerPage = 10;    // default page size

        try {
            if (page != null && !page.trim().isEmpty()) {
                pageNumber = Integer.parseInt(page);
                if (pageNumber < 1) pageNumber = 1;
            }

            if (size != null && !size.trim().isEmpty()) {
                usersPerPage = Integer.parseInt(size);
                if (usersPerPage < 1) usersPerPage = 10;
            }

            int offset = (pageNumber - 1) * usersPerPage;

            Map<String, Object> result = new HashMap<>();

            // Fetch paginated users from DAO
            List<User> users = userDao.getPaginatedUsers(usersPerPage, offset);
            logger.debug(users);
            if (users == null || users.isEmpty()) {
                result.put("success", false);
                result.put("message", "User data not found");
                return result;
            }

            // Ideally total user count should come from a separate DAO method
            int totalUsers = userDao.getTotalUserCount();
            int totalPages = (int) Math.ceil((double) totalUsers / usersPerPage);

            // convert UserList to UserDTO list
            List<UserDTO> userDTOList = users.stream()
                    .map(UserDTO::new)
                    .toList();

            result.put("success", true);
            result.put("message", "User data fetched successfully");
            result.put("users", userDTOList);
            result.put("totalPages", totalPages);
            result.put("currentPage", pageNumber);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "Error: " + e.getMessage());
            return errorResult;
        }
    }


    // method to delete the user
    public Map<String, Object> deleteUser(String userId) {
        logger.debug("UserServletHelper >> deleteUser >>> userId : " + userId);
        Map<String, Object> result = new HashMap<>();
        try {
            if (userId == null || userId.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "Invalid userId");
                return result;
            }

            userDao.deleteUser(userId);

            result.put("success", true);
            result.put("message", "User details deleted successfully");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", e.getMessage());
            return result;
        }
    }

    // method to validate all the user input data
    public String[] isAllDataValid(User user, boolean isUpdateReq) {

        String[] valResult = new String[2];

        if (user == null) {
            valResult[0] = "false";
            valResult[1] = "user value is null";
            return valResult;
        }

        valResult = validateFirstname(user.getFirstName());
        if ("false".equals(valResult[0])) {
            return valResult;
        }

        valResult = validateLastname(user.getLastName());
        if ("false".equals(valResult[0])) {
            return valResult;
        }

        valResult = validateEmail(user, isUpdateReq);
        if ("false".equals(valResult[0])) {
            return valResult;
        }

        valResult = validateDob(user.getDob());
        if ("false".equals(valResult[0])) {
            return valResult;
        }

        if (!isUpdateReq) {
            valResult = validatePassword(user.getPassword());
            if ("false".equals(valResult[0])) {
                return valResult;
            }
        }

        return valResult;
    }

    // Validate First Name
    public String[] validateFirstname(String fnameValue) {
        logger.debug("validateFirstname is called..");
        String[] res = new String[2];
        res[0] = "true";
        res[1] = "";
        // Regex to match only letters
        String regex = "^[A-Za-z]+$";
        if (fnameValue == null || fnameValue.isEmpty()) {
            logger.debug("First name is missing");
            res[0] = "false";
            res[1] = "First name is missing";
            return res;
        } else if (!fnameValue.matches(regex)) {
            logger.debug("Only letters are allowed");
            res[0] = "false";
            res[1] = "Only letters are allowed";
            return res;
        } else if (fnameValue.length() < 3 || fnameValue.length() > 15) {
            logger.debug("Length of first name must be between 3 and 15");
            res[0] = "false";
            res[1] = "Length of first name must be between 3 and 15";
            return res;
        }

        return res;
    }

    // Validate Last Name
    public String[] validateLastname(String lnameValue) {
        logger.debug("validateLastname is called..");

        String[] res = new String[2];
        res[0] = "true";
        res[1] = "";

        // Regex to match only letters
        String regex = "^[A-Za-z]+$";
        if (lnameValue == null || lnameValue.isEmpty()) {
            logger.debug("Last name is missing");
            res[0] = "false";
            res[1] = "Last name is missing";
            return res;
        } else if (!lnameValue.matches(regex)) {
            logger.debug("Only letters are allowed");
            res[0] = "false";
            res[1] = "Only letters are allowed";
            return res;
        } else if (lnameValue.length() < 3 || lnameValue.length() > 15) {
            logger.debug("Length of last name must be between 3 and 15");
            res[0] = "false";
            res[1] = "Length of last name must be between 3 and 15";
            return res;
        }

        return res;
    }

    // Validate Email
    public String[] validateEmail(User user, boolean isUpdateReq) {
        String emailValue = user.getEmail();
        logger.debug("validateEmail is called..");
        String[] res = new String[2];
        res[0] = "true";
        res[1] = "";

        // Regex for valid email format
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (emailValue == null || emailValue.isEmpty()) {
            logger.debug("Email id is missing");
            res[0] = "false";
            res[1] = "Email id is missing";
            return res;
        } else if (!emailValue.matches(regex)) {
            logger.debug("**Invalid email id");
            res[0] = "false";
            res[1] = "Invalid email id";
            return res;
        }

        // get existing user data with same email
        Optional<User> userOp = userDao.getUser(user.getEmail());
        // email should be unique, if user found with same email id return false
        if (userOp.isPresent()) {
            User user2 = userOp.get();
            logger.debug("isUpdateReq :: [{}]",isUpdateReq);
            logger.debug("user1Id :: [{}], user2Id :: [{}]",user.getUserId(), user2.getUserId());
            if (!isUpdateReq) {
                logger.debug("user with same email id already exist");
                res[0] = "false";
                res[1] = "User with same email id already exist";
                return res;
            }
            // In case of update request, if both userid are different return false
            if (!(user.getUserId() == user2.getUserId())) {
                logger.debug("update failed >> user with same email id already exist");
                res[0] = "false";
                res[1] = "User with same email id already exist";
                return res;
            }
        }

        return res;
    }

    // Validate Date of Birth
    public String[] validateDob(LocalDate dob) {
        logger.debug("validateDob is called..");

        String[] res = new String[2];
        res[0] = "true";
        res[1] = "";

        if (dob == null) {
            logger.debug("Date of birth is missing");
            res[0] = "false";
            res[1] = "Date of birth is missing";
            return res;
        }

        try {
            LocalDate currentDate = LocalDate.now();
            int inputYear = dob.getYear();
            int currentYear = currentDate.getYear();
            logger.debug("inputYear [{}], currentYear [{}]", inputYear, currentYear);
            int age = currentYear - inputYear;

            int m = dob.getMonthValue() - currentDate.getMonthValue();
            logger.debug("Month [{}]", m);
            if (m < 0 || (m == 0 && currentDate.getDayOfMonth() < dob.getDayOfMonth())) {
                age--;
            }

            logger.debug("Age [{}]", age);

            if (age < 18 || age > 100) {
                logger.debug("Age must be between 18 to 100 years.");
                res[0] = "false";
                res[1] = "Age must be between 18 to 100 years";
                return res;
            }
        } catch (Exception e) {
            logger.debug("Invalid date format");
            res[0] = "false";
            res[1] = "Invalid date format";
            return res;
        }

        return res;
    }

    // Validate Password
    public String[] validatePassword(String passValue) {
        logger.debug("validatePassword is called..");
        String[] res = new String[2];
        res[0] = "true";
        res[1] = "";

        // Regex for password complexity (8-16 characters, one digit, one lowercase, one
        // uppercase, one special character)
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}$";
        if (passValue == null || passValue.isEmpty()) {
            logger.debug("Password is missing");
            res[0] = "false";
            res[1] = "Password is missing";
            return res;
        } else if (!passValue.matches(regex)) {
            logger.debug("Password must match complexity requirement");
            res[0] = "false";
            res[1] = "Password must be of 8-20 chars with atleast 1 digit, 1 lowercase, 1 uppercase, 1 special, no spaces.";
            return res;
        }

        return res;
    }

}
