package com.virtualminds.dal.statements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentSkipListSet;

public class BlackListIPsLoader extends StatementLoader<Long> {
	private static final String SQL_SCRIPT = "select ip from ip_blacklist";

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
