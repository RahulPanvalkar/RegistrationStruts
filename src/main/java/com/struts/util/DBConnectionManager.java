package com.struts.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {

	public static Connection connection;

	public static Connection getConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				return connection;
			}

			Class.forName("com.mysql.cj.jdbc.Driver");

			String username = "root";
			String password = "root";
			String url = "jdbc:mysql://localhost:3306/training";

			connection = DriverManager.getConnection(url, username, password);

			System.out.println("Connection established successfully");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return connection;
	}

}
