package com.virtualminds.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesLoader {
	private static final String CUSTOM_PROPERTIES_FILE_NAME = "RequestProcesssor.properties";
	private static final String PROPERTIES_ERROR_INITIALIZATION = "Initialization of the custom properties was failed, will be used default ones";
	private static final String MESSAGE_FORMAT = "%s=%s";
	private final Logger logger = Logger.getLogger(PropertiesLoader.class.getName());

	public Properties loadProperties() {
		File configDir = new File(System.getProperty("catalina.base"), "conf");
		File configFile = new File(configDir, CUSTOM_PROPERTIES_FILE_NAME);
		Properties properties = new Properties();
		if (configFile.exists()) {
			try {
				properties.load(new FileInputStream(configFile));
			} catch (IOException e) {
				logger.log(Level.WARNING, String.format(PROPERTIES_ERROR_INITIALIZATION, configFile.getAbsolutePath()),
						e);
			}
		}
		logLoadedProperties(properties);
		return properties;
	}

	private void logLoadedProperties(Properties properties) {
		Set<Entry<Object, Object>> entrySet = properties.entrySet();
		if (!entrySet.isEmpty()) {
			logger.info("Were loaded following properties");
			entrySet.forEach(entry -> logger.info(String.format(MESSAGE_FORMAT, entry.getKey(), entry.getValue())));
		} else {
			logger.info("No properties were loaded, will be used default ones");
		}
	}
}
