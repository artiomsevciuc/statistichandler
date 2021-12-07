package com.virtualminds.context;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import com.virtualminds.dal.PreparedStatementHolder;
import com.virtualminds.dal.statements.ActiveCustomerLoader;
import com.virtualminds.dal.statements.BlackListIPsLoader;
import com.virtualminds.dal.statements.BlackListUserAgentsLoader;
import com.virtualminds.exceptions.StatisticHandlerRuntimeException;
import com.virtualminds.requestprocessor.StatisticsContextHolder;

public class ContextStarter implements ServletContextListener {
	private static final String INITIATING_DATABASE_CONTEXT_EXCEPTION = "Initianting database context exception";
	private static final String STOPPING_DATABASE_CONTEXT_EXCEPTION = "Stopping database context excption";
	private static final String THREAD_POOL_INITIALIZATION_EXCEPTION = "Thread pool initialization failed";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		initializeConnection();
		initializeThreadPoolHolder();
	}

	private void initializeConnection() {
		try {
			InitialContext initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource dataSource = (DataSource) envContext.lookup("jdbc/statistics");
			PreparedStatementHolder.getInstance().initializeConnection(dataSource);
		} catch (NamingException | SQLException e) {
			throw new StatisticHandlerRuntimeException(INITIATING_DATABASE_CONTEXT_EXCEPTION, e);
		}
	}

	private void initializeThreadPoolHolder() {
		try {
			StatisticsContextHolder.getInstance().initializeThreadPoolExecutor(
					new BlackListIPsLoader().retrieveFromDatabase(),
					new ActiveCustomerLoader().retrieveFromDatabase(),
					new BlackListUserAgentsLoader().retrieveFromDatabase(), new PropertiesLoader().loadProperties());
		} catch (SQLException e) {
			throw new StatisticHandlerRuntimeException(THREAD_POOL_INITIALIZATION_EXCEPTION, e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		StatisticsContextHolder.getInstance().destroyContext();
		try {
			PreparedStatementHolder.getInstance().getConnection().close();
		} catch (SQLException e) {
			throw new StatisticHandlerRuntimeException(STOPPING_DATABASE_CONTEXT_EXCEPTION, e);
		}
	}

}
