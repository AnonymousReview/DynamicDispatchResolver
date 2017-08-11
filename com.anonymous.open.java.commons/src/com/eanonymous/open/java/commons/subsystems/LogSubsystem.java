package com.eanonymous.open.java.commons.subsystems;

public class LogSubsystem extends JavaSubsystem {

	public static final String TAG = "LOG_SUBSYSTEM";

	@Override
	public String getName() {
		return "Log";
	}

	@Override
	public String getDescription() {
		return "Java logging libraries";
	}

	@Override
	public String getTag() {
		return TAG;
	}

	@Override
	public String[] getParentTags() {
		return new String[] { IOSubsystem.TAG };
	}

	@Override
	public String[] getNamespaces() {
		return new String[] { 
			"java.util.logging", // standard JDK logging apis
			
			"org.apache.logging.log4j", // Public API for Log4j 2
			"org.apache.logging.log4j.message", // Public Message Types used for Log4j 2
			"org.apache.logging.log4j.simple", // Simple logging implementation
			"org.apache.logging.log4j.spi", // Internal interfaces and classes to be used by authors of logging implementations or for internal use by API classes
			"org.apache.logging.log4j.status", // Status API for Log4j 2
			"org.apache.logging.log4j.util", // Internal utility classes for the Log4j 2 API
			
			"org.apache.log4j", // The main log4j package.
			"org.apache.log4j.chainsaw", // Chainsaw is a GUI log viewer and filter for the log4j package.
			"org.apache.log4j.config", // Package used in getting/setting component properties.
			"org.apache.log4j.helpers", // This package is used internally.
			"org.apache.log4j.jdbc", // The JDBCAppender provides for sending log events to a database.
			"org.apache.log4j.jmx", // This package lets you manage log4j settings using JMX.
			"org.apache.log4j.lf5", 
			"org.apache.log4j.lf5.util", 
			"org.apache.log4j.lf5.viewer", 
			"org.apache.log4j.lf5.viewer.categoryexplorer", 
			"org.apache.log4j.lf5.viewer.configure", 
			"org.apache.log4j.net", // Package for remote logging.
			"org.apache.log4j.nt", // Package for NT event logging.
			"org.apache.log4j.or", // ObjectRenders are resposible for rendering messages depending on their class type.
			"org.apache.log4j.or.jms", // This package contains the MessageRenderer which renders objects of type javax.jms.Message.
			"org.apache.log4j.or.sax", // This package contains the AttributesRenderer which renders object of class org.xml.sax.Attributes.
			"org.apache.log4j.pattern", // Provides classes implementing format specifiers in conversion patterns.
			"org.apache.log4j.rewrite",  
			"org.apache.log4j.spi", // Contains part of the System Programming Interface (SPI) needed to extend log4j.
			"org.apache.log4j.varia", // Contains various appenders, filters and other odds and ends.
			"org.apache.log4j.xml", // XML based components.
			"org.apache.log4j.xml.examples"
			};
	}

}
