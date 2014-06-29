package com.tracker.backend.service.rest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.tracker.backend.service.rest.utils.exception.TechnicalUtilsException;

public class Configuration {
	private Properties loader;
	public final static Logger LOGGER = new Logger();

	public Configuration() {
	}

	public Properties getLoader() {
		return loader;
	}

	public void loadProperties(String nameFileProperties)
			throws TechnicalUtilsException {
		LOGGER.pushDebugs("[STAR] loadProperties");
		this.loader = new Properties();
		try {
			InputStream inputProp = getClass().getResourceAsStream(nameFileProperties);
			this.loader.load(inputProp);
		} catch (IOException e) {
			throw new TechnicalUtilsException(e.getMessage(), e);
		}

	}
}
