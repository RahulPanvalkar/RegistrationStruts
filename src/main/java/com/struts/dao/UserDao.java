package com.struts.dao;

import com.struts.models.User;
import com.struts.util.DBConnectionManager;
import com.struts.util.LoggerUtil;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UserDao {
	
	private int noOfRecords;	// store total no of user records

	private static final Logger logger = LoggerUtil.getLogger(UserDao.class);


	// getter method to return noOfRecords
	public int getNoOfRecords() { return noOfRecords; }
	
	// Method to save new user
	public void saveUser(User user) {
		logger.debug("User >>> {}", user);

		String sql = "INSERT INTO users(fname, lname, email, password, gender, dob) VALUES (?,?,?,?,?,?)";

		try (Connection conn = DBConnectionManager.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			conn.setAutoCommit(false);
			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPassword());
			ps.setString(5, String.valueOf(user.getGender()));
			// convert string date to sql.Date format
			ps.setDate(6, Date.valueOf(user.getDob()));

			// if row is added in table commit transaction
			int UpdatedRows = ps.executeUpdate();
			logger.debug("updatedRows: " + UpdatedRows);
			if (UpdatedRows != 1) {
				// if something went wrong, rollback transaction
				conn.rollback();
				return;
			}

			conn.commit();
			logger.info("Details of user {} {} successfully added", user.getFirstName(), user.getLastName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	// Method to save new user
	public void updateUser(User user) {
		logger.debug("user >>  {}", user);

		String sql = "UPDATE users SET fname=?, lname=?, email=?, gender=?, dob=? WHERE userId=?";

		try (Connection conn = DBConnectionManager.getConnection(); 
				PreparedStatement ps = conn.prepareStatement(sql)) {
			conn.setAutoCommit(false);
			ps.setString(1, user.getFirstName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getEmail());
			ps.setString(4, String.valueOf(user.getGender()));
			ps.setInt(6, user.getUserId());

			// convert string date to sql.Date format
			ps.setDate(5, Date.valueOf(user.getDob()));

			// if row is added in table commit transaction
			int UpdatedRows = ps.executeUpdate();
			logger.debug("updatedRows: [{}]", UpdatedRows);
			if (UpdatedRows != 1) {
				// if something went wrong, rollback transaction
				conn.rollback();
				return;
			}

			conn.commit();
			logger.info("Details of user with Id [{}] successfully updated", user.getUserId());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// Method to get the user data from DB from username (email/userId)
	public Optional<User> getUser(String username) {

		if (username == null || username.trim().isEmpty()) {
			return Optional.empty();
		}

		boolean isEmail = true;
		String sql = "SELECT * FROM users WHERE email = ?";

		logger.debug("username : [{}]", username);
		username = username.trim(); // remove leading and trailing white spaces

		// check if username is email or userId
		String numRegex = "\\d+";
		if (username.matches(numRegex)) {
			isEmail = false;
			sql = "SELECT * FROM users WHERE userId = ?";
		}

		try (Connection conn = DBConnectionManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			if (isEmail) {
				ps.setString(1, username);
			} else {
				// if username is userId, convert username to int
				ps.setInt(1, Integer.parseInt(username));
			}

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					User user = new User();
					user.setUserId(rs.getInt("userId"));
					user.setFirstName(rs.getString("fname"));
					user.setLastName(rs.getString("lname"));
					user.setEmail(rs.getString("email"));
					user.setDob(rs.getDate("dob").toLocalDate());
					user.setGender(rs.getString("gender").charAt(0));
					logger.debug("user :: " + user);
					return Optional.of(user);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	// Method to get all users data from DB from username (email/userId)
	public List<User> getAllUser() {

		List<User> usersList = new ArrayList<>();

		String sql = "SELECT * FROM users";

		try (Connection conn = DBConnectionManager.getConnection(); 
				PreparedStatement ps = conn.prepareStatement(sql)) {

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					User user = new User();
					user.setUserId(rs.getInt("userId"));
					user.setFirstName(rs.getString("fname"));
					user.setLastName(rs.getString("lname"));
					user.setEmail(rs.getString("email"));
					user.setDob(rs.getDate("dob").toLocalDate());
					user.setGender(rs.getString("gender").charAt(0));

					// add user to list
					usersList.add(user);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usersList;
	}

	// to get Paginated users
	public List<User> getPaginatedUsers(int limit, int offset) {

		List<User> usersList = new ArrayList<>();

		String sql = "SELECT * FROM users limit ? offset ?";

		try (Connection conn = DBConnectionManager.getConnection(); 
				PreparedStatement ps = conn.prepareStatement(sql)) {
			
			ps.setInt(1, limit);
			ps.setInt(2, offset);
			
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					User user = new User();
					user.setUserId(rs.getInt("userId"));
					user.setFirstName(rs.getString("fname"));
					user.setLastName(rs.getString("lname"));
					user.setEmail(rs.getString("email"));
					user.setDob(rs.getDate("dob").toLocalDate());
					user.setGender(rs.getString("gender").charAt(0));

					// add user to list
					usersList.add(user);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usersList;
	}
	
	// Method to delete user data from users table
	public void deleteUser(String userIdStr) throws Exception {
		String sql = "DELETE FROM users WHERE userId = ?";

		logger.debug("deleteUser >> userIdStr : " + userIdStr);
		userIdStr = userIdStr.trim(); // remove leading and trailing white spaces

		// check if userId is valid
		String numRegex = "\\d+";
		if (!userIdStr.matches(numRegex)) {
			throw new Exception("Incorrect userId provided");
		}
		int userid = Integer.parseInt(userIdStr);

		try (Connection conn = DBConnectionManager.getConnection(); 
				PreparedStatement ps = conn.prepareStatement(sql)) {
			conn.setAutoCommit(false);
			ps.setInt(1, userid);

			int rows = ps.executeUpdate();
			logger.debug("rows updated : "+rows);
			if (rows != 1) {
				conn.rollback();
				throw new RuntimeException("Incorrect Update, " + rows + " Rows updated");
			}
			conn.commit();
			logger.info("Details of user with Id [{}] successfully deleted", userid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
