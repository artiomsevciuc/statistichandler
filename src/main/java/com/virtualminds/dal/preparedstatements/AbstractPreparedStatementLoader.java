package com.virtualminds.dal.preparedstatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractPreparedStatementLoader {

	public String executePreparedStatement() throws SQLException {
		PreparedStatement preparedStatement = fillPreparedStatement();
		StringBuilder responseBuilder = new StringBuilder("{");
		ResultSet resultSet = null;
		try {
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				processResultSet(resultSet, responseBuilder);
			}
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}
		responseBuilder.append("}");
		return responseBuilder.toString();
	}

	protected abstract PreparedStatement fillPreparedStatement() throws SQLException;

	protected abstract void processResultSet(ResultSet resultSet, StringBuilder responseBuilder) throws SQLException;

}
