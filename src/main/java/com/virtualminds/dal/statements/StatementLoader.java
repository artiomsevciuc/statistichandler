package com.virtualminds.dal.statements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentSkipListSet;

import com.virtualminds.dal.PreparedStatementHolder;

public abstract class StatementLoader<S> {

	public ConcurrentSkipListSet<S> retrieveFromDatabase() throws SQLException {
		Statement statement = PreparedStatementHolder.getInstance().getConnection().createStatement();
		ResultSet resultSet = null;
		try {
			resultSet = statement.executeQuery(getSQLScript());
			return processResultSet(resultSet);
		} finally {
			statement.close();
			if (resultSet != null) {
				resultSet.close();
			}
		}
	}

	protected abstract String getSQLScript();

	protected abstract ConcurrentSkipListSet<S> processResultSet(ResultSet resultSet) throws SQLException;

}
