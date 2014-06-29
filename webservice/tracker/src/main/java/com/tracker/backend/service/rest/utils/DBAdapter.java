package com.tracker.backend.service.rest.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAdapter {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	private Logger logger;
	private String dataBase;
	private String user;
	private String password;

	public DBAdapter(String dataBase, String user, String password,
			Logger logger) {
		this.logger = logger;
		this.dataBase = new String(dataBase);
		this.user = new String(user);
		this.password = new String(password);
	}

	private void open() {
		try {
			// This will load the MySQL driver, each DB has its own driver
			this.logger.pushDebugs("Loading the database driver.");
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			this.logger.pushDebugs("Connecting to the database server.");
			connect = DriverManager.getConnection(this.dataBase + "?" + "user="
					+ this.user + "&password=" + this.password);
		} catch (SQLException e) {
			this.logger.pushErrors(e,
					"DBAdapter crash: Unable to connect to the database");
		} catch (ClassNotFoundException e) {
			this.logger.pushErrors(e,
					"DBAdapter crash: Unable to find jdbc driver");
		}
	}

	public String read(String table, String fields) {
		StringBuffer content = new StringBuffer();
		String colums[] = fields.split(",");
		open();
		try {
			this.logger.pushDebugs("Reading from the table: " + table
					+ " the columns: " + fields + ".");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from " + table);
			while (resultSet.next()) {
				String data = new String();
				if (colums != null) {
					for (String column : colums) {
						try {
							data += resultSet.getString(column) + ",";
						} catch (SQLException e) {
							this.logger.pushWarnings(e, "Read crash: Table "
									+ table + " does not exist");
						}
					}
					content.append(data.substring(0, data.lastIndexOf(','))
							+ "\n");
				}
			}
		} catch (SQLException e) {
			this.logger.pushWarnings(e, "Read crash: Table " + table
					+ " does not exist");
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.pushErrors(e,
					"Read crash: Unexpected error when trying to read from "
							+ table);
		}
		close();
		return content.toString();
	}
	
	public String read(String table, String fields, String where, String values) {
		StringBuffer content = new StringBuffer();
		String colums[] = fields.split(",");
		String conditions[] = where.split(",");
		String tos[] = values.split(",");
		String clause = "";
		int i=0;
		for(String condition: conditions){
			if(i != 0){
				clause += " and ";
			}
			clause += condition + " = '" + tos[i]+"'";
			i++;
		}
 		open();
		try {
			this.logger.pushDebugs("Reading from the table: " + table
					+ " the columns: " + fields + " where "+clause + ".");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("select * from " + table + " where "+clause);
			while (resultSet.next()) {
				String data = new String();
				if (colums != null) {
					for (String column : colums) {
						try {
							data += resultSet.getString(column) + ",";
						} catch (SQLException e) {
							this.logger.pushWarnings(e, "Read crash: Table "
									+ table + " does not exist");
						}
					}
					content.append(data.substring(0, data.lastIndexOf(','))
							+ "\n");
				}
			}
		} catch (SQLException e) {
			this.logger.pushWarnings(e, "Read crash: Table " + table
					+ " does not exist");
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.pushErrors(e,
					"Read crash: Unexpected error when trying to read from "
							+ table);
		}
		close();
		return content.toString();
	}

	public void write(String table, String fields, String data) {
		String values = new String();
		String columns[] = fields.split(",");
		String datas[] = data.split(",");
		int index = 1;
		open();
		for (@SuppressWarnings("unused")
		String column : columns)
			values += "?,";
		try {
			this.logger.pushDebugs("Writting in the table: " + table
					+ " with the columns: " + fields + " the data: " + data
					+ ".");
			preparedStatement = connect.prepareStatement("insert into  "
					+ table + " values ("
					+ values.substring(0, values.lastIndexOf(',')) + ")");
			for (String value : datas)
				try {
					preparedStatement.setString(index, value);
					index++;
				} catch (SQLException e) {
					this.logger
							.pushWarnings(
									e,
									"Write crash: Unable to full fill the preparedStatement because of the column: "
											+ value + " at position: " + index);
				}
			try {
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				this.logger
						.pushWarnings(e,
								"Write crash: Unable to excecute the preparedStatement");
			}
		} catch (SQLException e) {
			this.logger.pushErrors(e,
					"Write crash: Unable to connect to the database");
		}
		close();
	}

	public void sync(String table, String criteria, String pattern,
			String fields, String data) {
		String values = new String();
		String columns[] = fields.split(",");
		String datas[] = data.split(",");
		String primaries[] = criteria.split(",");
		String capitals[] = pattern.split(",");
		String adapt = new String();
		int index = 1;
		open();
		for (int i = 0; i < columns.length; i++) {
			values += columns[i] + " = ?,";
		}
		for (int i = 0; i < primaries.length; i++) {
			adapt += primaries[i] + " = ? and ";
		}
		try {
			this.logger.pushDebugs("Updating the table: " + table
					+ " according to the columns: " + fields
					+ " with the data: " + data + ".");
			preparedStatement = connect.prepareStatement("update " + table
					+ " set " + values.substring(0, values.lastIndexOf(","))
					+ " where "
					+ adapt.substring(0, adapt.lastIndexOf("and") - 1));
			for (String value : datas)
				try {
					preparedStatement.setString(index++, value);
				} catch (SQLException e) {
					this.logger
							.pushWarnings(
									e,
									"Update crash: Unable to full fill the preparedStatement because of the column: "
											+ value + " at position: " + index);
				}
			for (String value : capitals)
				try {
					preparedStatement.setString(index++, value);
				} catch (SQLException e) {
					this.logger
							.pushWarnings(
									e,
									"Update crash: Unable to full fill the preparedStatement because of the column: "
											+ value + " at position: " + index);
				}
			try {
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				this.logger
						.pushWarnings(e,
								"Update crash: Unable to excecute the preparedStatement");
			}
		} catch (SQLException e) {
			this.logger.pushErrors(e,
					"Update crash: Unable to connect to the database");
		}
		close();
	}

	public void delete(String table, String criteria, String pattern) {
		String primaries[] = criteria.split(",");
		String capitals[] = pattern.split(",");
		String adapt = new String();
		int index = 1;
		open();
		for (int i = 0; i < primaries.length; i++) {
			adapt += primaries[i] + " = ? and ";
		}
		try {
			this.logger.pushDebugs("Deleting from the table: " + table);
			preparedStatement = connect.prepareStatement("delete from " + table
					+ " where "
					+ adapt.substring(0, adapt.lastIndexOf("and") - 1));
			for (String value : capitals)
				try {
					preparedStatement.setString(index++, value);
				} catch (SQLException e) {
					this.logger
							.pushWarnings(
									e,
									"Update crash: Unable to full fill the preparedStatement because of the column: "
											+ value + " at position: " + index);
				}
			try {
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				this.logger
						.pushWarnings(e,
								"Update crash: Unable to excecute the preparedStatement");
			}
		} catch (SQLException e) {
			this.logger.pushErrors(e,
					"Update crash: Unable to connect to the database");
		}
		close();
	}

	private void close() {
		try {
			this.logger.pushDebugs("Closing the connection to the database.");
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {
			this.logger
					.pushErrors(e,
							"Close crash: Unable to get disconnected to the database server.");
		}
	}
}
