package com.virtualminds.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.ZonedDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.virtualminds.dal.StatementsExecutor;

public class CustomerPerDayRequestHandler extends HttpServlet {

	private static final long serialVersionUID = -3834059190828442871L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String customer = request.getParameter("customer");
		String statisticsForDay = request.getParameter("statisticForDay");
		String responseMessage = null;
		try {
			responseMessage = new StatementsExecutor().getStatisticsForCustomerForPeriod(customer,
					ZonedDateTime.parse(statisticsForDay));
		} catch (SQLException e) {
			throw new ServletException("SQL exception was occured", e);
		}
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		out.print(responseMessage);
		out.flush();
	}
}
