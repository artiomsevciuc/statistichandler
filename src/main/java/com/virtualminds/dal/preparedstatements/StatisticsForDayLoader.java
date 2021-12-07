package com.virtualminds.dal.preparedstatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import com.virtualminds.dal.PreparedStatementBuilder;
import com.virtualminds.dal.PreparedStatementHolder;

public class StatisticsForDayLoader extends AbstractPreparedStatementLoader {
	private ZonedDateTime selectedDay;

	public StatisticsForDayLoader(ZonedDateTime selectedDay) {
		this.selectedDay = selectedDay;
	}

	@Override
	protected PreparedStatement fillPreparedStatement() throws SQLException {
		PreparedStatement preparedStatement = PreparedStatementHolder.getInstance().getStatisticsForADay();
		selectedDay = selectedDay.truncatedTo(ChronoUnit.DAYS);
		preparedStatement.setLong(1, selectedDay.toEpochSecond());
		preparedStatement.setLong(2, selectedDay.plus(1, ChronoUnit.DAYS).toEpochSecond());
		return preparedStatement;
	}

	@Override
	protected void processResultSet(ResultSet resultSet, StringBuilder responseBuilder) throws SQLException {
		int validRequests = resultSet.getInt(PreparedStatementBuilder.SUM_VALID_REQUESTS);
		int invalidRequests = resultSet.getInt(PreparedStatementBuilder.SUM_INVALID_REQUESTS);
		responseBuilder.append("\"total_daily_valid_requests\":").append(validRequests).append(",");
		responseBuilder.append("\"total_daily_invalid_requests\":").append(invalidRequests).append(",");
		responseBuilder.append("\"total_daily_number_of_requests\":").append(validRequests + invalidRequests);
	}

}
