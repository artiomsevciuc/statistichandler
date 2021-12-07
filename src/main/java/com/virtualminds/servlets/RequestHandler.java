package com.virtualminds.servlets;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.virtualminds.requestprocessor.StatisticsContextHolder;

public class RequestHandler extends HttpServlet {

	private static final long serialVersionUID = -3834059190828442871L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		StatisticsContextHolder.getInstance()
				.processMessage(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())),
						request.getHeader("User-Agent"));
		resp.setStatus(HttpServletResponse.SC_ACCEPTED);
	}
}
