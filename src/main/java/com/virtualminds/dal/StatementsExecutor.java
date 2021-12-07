package com.virtualminds.dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.virtualminds.dal.preparedstatements.StatisticsForDayAndCustomerLoader;
import com.virtualminds.dal.preparedstatements.StatisticsForDayLoader;
import com.virtualminds.requestprocessor.StatisticsPOJO;

public class StatementsExecutor {
	private final Logger logger = Logger.getLogger(StatementsExecutor.class.getName());
	private static final String EXCEPTION = "SQL exception did occur";

	public String getStatisticsForCustomerForPeriod(String customer, ZonedDateTime selectedDay) throws SQLException {
		return new StatisticsForDayAndCustomerLoader(customer, selectedDay).executePreparedStatement();
	}

	public String getStatisticsPerDay(ZonedDateTime selectedDay) throws SQLException {
		return new StatisticsForDayLoader(selectedDay).executePreparedStatement();
	}

	public void insertOrUpdateStatistics(Long customerId, StatisticsPOJO pojo) {
		try {
			PreparedStatement preparedStatement = PreparedStatementHolder.getInstance()
					.getUpdateStatisticsPreparedStatement();
			ZonedDateTime instant = ZonedDateTime.now().truncatedTo(ChronoUnit.HOURS);
			fillPreparedStatement(preparedStatement, instant, customerId, pojo);
			preparedStatement.execute();
			if (preparedStatement.getUpdateCount() != 1) {
				insertStatistics(instant, customerId, pojo);
			}
			pojo.nullifyNumberOfValidRequests();
			pojo.nullifyNumberOfInvalidRequests();
		} catch (SQLException e) {
			logger.log(Level.SEVERE, EXCEPTION, e);
		}
	}

	private void insertStatistics(ZonedDateTime instant, long customerId, StatisticsPOJO pojo) throws SQLException {
		PreparedStatement preparedStatement = PreparedStatementHolder.getInstance()
				.getInsertStatisticsPreparedStatement();
		fillPreparedStatement(preparedStatement, instant, customerId, pojo);
		preparedStatement.execute();
	}

	private void fillPreparedStatement(PreparedStatement preparedStatement, ZonedDateTime instant, long customerId,
			StatisticsPOJO pojo) throws SQLException {
		preparedStatement.setLong(1, pojo.getNumberOfValidRequests());
		preparedStatement.setLong(2, pojo.getNumberOfInvalidRequests());
		preparedStatement.setLong(3, customerId);
		preparedStatement.setLong(4, instant.toEpochSecond());
	}

}
