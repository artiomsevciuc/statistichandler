package com.virtualminds.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PreparedStatementBuilder {
	public static final String SUM_VALID_REQUESTS = "Summ of valid requests";
	public static final String SUM_INVALID_REQUESTS = "Summ of invalid requests";
	private Connection connection;

	public PreparedStatementBuilder(Connection connection) {
		this.connection = connection;
	}

	public PreparedStatement getStatisticsForCustomerForPeriod() throws SQLException {
		StringBuilder statement = new StringBuilder("SELECT ");
		statement.append("SUM(st.request_count) as \"").append(SUM_VALID_REQUESTS).append("\", ");
		statement.append("SUM(st.invalid_count) as \"").append(SUM_INVALID_REQUESTS).append("\" ");
		statement.append("FROM ");
		statement.append("hourly_stats st ");
		statement.append("INNER JOIN customer c ON st.customer_id = c.id ");
		statement.append("WHERE ");
		statement.append("c.name = ? and  st.time>=? and st.time<=? ");
		statement.append("GROUP BY c.name");
		return connection.prepareStatement(statement.toString());
	}

	public PreparedStatement getStatisticsForADay() throws SQLException {
		StringBuilder statement = new StringBuilder("SELECT ");
		statement.append("SUM(request_count) as \"").append(SUM_VALID_REQUESTS).append("\", ");
		statement.append("SUM(invalid_count) as \"").append(SUM_INVALID_REQUESTS).append("\" ");
		statement.append("from ");
		statement.append("hourly_stats ");
		statement.append("where ");
		statement.append("time>=? ");
		statement.append("and time<=?");
		return connection.prepareStatement(statement.toString());
	}
	public PreparedStatement updateStatistics() throws SQLException {
		StringBuilder statement = new StringBuilder();
		statement.append("UPDATE hourly_stats ");
		statement.append("SET ");
		statement.append("request_count = request_count + ? ,invalid_count=invalid_count+? ");
		statement.append("WHERE ");
		statement.append("customer_id = ?  AND time = ?");
		return connection.prepareStatement(statement.toString());
	}

	public PreparedStatement insertStatistics() throws SQLException {
		StringBuilder statement = new StringBuilder();
		statement.append("INSERT INTO hourly_stats (");
		statement.append("request_count, invalid_count, customer_id, time ");
		statement.append(") VALUES (");
		statement.append("?, ?, ?, ?");
		statement.append(")");
		return connection.prepareStatement(statement.toString());
	}
}
