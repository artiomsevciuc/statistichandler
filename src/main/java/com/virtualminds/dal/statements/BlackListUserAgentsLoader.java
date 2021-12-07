package com.virtualminds.dal.statements;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentSkipListSet;

public class BlackListUserAgentsLoader extends StatementLoader<String> {
	private static final String SQL_SCRIPT = "select ua from ua_blacklist";
	@Override
	protected ConcurrentSkipListSet<String> processResultSet(ResultSet resultSet) throws SQLException {
		ConcurrentSkipListSet<String> userAgentsBlackList = new ConcurrentSkipListSet<>();
		while (resultSet.next()) {
			userAgentsBlackList.add(resultSet.getString(1));
		}
		return userAgentsBlackList;
	}
	@Override
	protected String getSQLScript() {
		return SQL_SCRIPT;
	}
}
