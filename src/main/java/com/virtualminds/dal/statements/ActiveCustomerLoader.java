package com.virtualminds.dal.statements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentSkipListSet;

public class ActiveCustomerLoader extends StatementLoader<Long> {
	private static final String SQL_SCRIPT = "select ID from customer where active=1";
	@Override
	protected ConcurrentSkipListSet<Long> processResultSet(ResultSet resultSet) throws SQLException {
		ConcurrentSkipListSet<Long> activeCustomersIDs = new ConcurrentSkipListSet<>();
		while (resultSet.next()) {
			activeCustomersIDs.add(resultSet.getLong(1));
		}
		return activeCustomersIDs;
	}

	@Override
	protected String getSQLScript() {
		return SQL_SCRIPT;
	}
}
