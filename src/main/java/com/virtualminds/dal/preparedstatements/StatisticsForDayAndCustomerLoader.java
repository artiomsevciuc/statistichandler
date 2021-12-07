package com.virtualminds.dal.preparedstatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import com.virtualminds.dal.PreparedStatementBuilder;
import com.virtualminds.dal.PreparedStatementHolder;

public class StatisticsForDayAndCustomerLoader extends AbstractPreparedStatementLoader {
	private String customer;
	private ZonedDateTime selectedDay;

	public StatisticsForDayAndCustomerLoader(String customer, ZonedDateTime selectedDay) {
		this.customer = customer;
		this.selectedDay = selectedDay;
	}

	@Override
	protected PreparedStatement fillPreparedStatement() throws SQLException {
		PreparedStatement preparedStatement = PreparedStatementHolder.getInstance().getStatisticsForCustomerForPeriod();
		preparedStatement.setString(1, customer);
		selectedDay = selectedDay.truncatedTo(ChronoUnit.DAYS);
		preparedStatement.setLong(2, selectedDay.toEpochSecond());
		preparedStatement.setLong(3, selectedDay.plus(1, ChronoUnit.DAYS).toEpochSecond());
		return preparedStatement;
	}

	@Override
	protected void processResultSet(ResultSet resultSet, StringBuilder responseBuilder) throws SQLException {
		int validRequests = resultSet.getInt(PreparedStatementBuilder.SUM_VALID_REQUESTS);
		int invalidRequests = resultSet.getInt(PreparedStatementBuilder.SUM_INVALID_REQUESTS);
		responseBuilder.append("\"total_valid_requests_for_customer\":").append(validRequests).append(",");
		responseBuilder.append("\"total_invalid_requests_for_customer\":").append(invalidRequests).append(",");
		responseBuilder.append("\"total_number_of_requests_for_customer\":").append(validRequests + invalidRequests);
	}

}
