package com.virtualminds.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public class PreparedStatementHolder {
	private Connection connection;
	private DataSource dataSource;
	private PreparedStatement statisticsForCustomerForPeriod;
	private PreparedStatement updateStatisticsPreparedStatement;
	private PreparedStatement insertStatisticsPreparedStatement;
	private PreparedStatement statisticsForADay;
	private static PreparedStatementHolder instance;

	public static PreparedStatementHolder getInstance() {
		if (instance == null) {
			instance = new PreparedStatementHolder();
		}
		return instance;
	}

	public Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connection = dataSource.getConnection();
			connection.setAutoCommit(true);
			statisticsForCustomerForPeriod = new PreparedStatementBuilder(connection)
					.getStatisticsForCustomerForPeriod();
			insertStatisticsPreparedStatement = new PreparedStatementBuilder(connection).insertStatistics();
			updateStatisticsPreparedStatement = new PreparedStatementBuilder(connection).updateStatistics();
		}
		return connection;
	}

	public PreparedStatement getStatisticsForCustomerForPeriod() throws SQLException {
		if (statisticsForCustomerForPeriod == null) {
			statisticsForCustomerForPeriod = new PreparedStatementBuilder(connection)
					.getStatisticsForCustomerForPeriod();
		}
		return statisticsForCustomerForPeriod;
	}

	public PreparedStatement getStatisticsForADay() throws SQLException {
		if (statisticsForADay == null) {
			statisticsForADay = new PreparedStatementBuilder(connection).getStatisticsForADay();
		}
		return statisticsForADay;
	}

	public PreparedStatement getInsertStatisticsPreparedStatement() throws SQLException {
		if (insertStatisticsPreparedStatement == null) {
			insertStatisticsPreparedStatement = new PreparedStatementBuilder(connection).insertStatistics();
		}
		return insertStatisticsPreparedStatement;
	}

	public PreparedStatement getUpdateStatisticsPreparedStatement() throws SQLException {
		if (updateStatisticsPreparedStatement == null) {
			updateStatisticsPreparedStatement = new PreparedStatementBuilder(connection).updateStatistics();
		}
		return updateStatisticsPreparedStatement;
	}

	public void initializeConnection(DataSource dataSource) throws SQLException {
		this.dataSource = dataSource;
		getConnection();
	}
}
