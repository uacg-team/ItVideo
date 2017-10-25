package com.itvideo.model.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 */
public enum DBConnection {
	CON1();
	private final String DB_IP = "127.0.0.1";
	private final String DB_PORT = "3306";
	private final String DB_NAME = "youtubedb";
	private final String DB_USER = "root";
	private final String DB_PASS = "vilio";
	private final String URL = "jdbc:mysql://" + DB_IP + ":" + DB_PORT + "/" + DB_NAME;
	
	private Connection connection;
	private DBConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("class Driver for db not found!");
		}
		try {
			this.connection = DriverManager.getConnection(URL, DB_USER, DB_PASS);
		} catch (SQLException e) {
			System.out.println("Error in DB:" + e.getMessage());
		}
	}
	public Connection getConnection() {
		return connection;
	}
	public void closeConnection() throws SQLException {
		connection.close();
	}
}
