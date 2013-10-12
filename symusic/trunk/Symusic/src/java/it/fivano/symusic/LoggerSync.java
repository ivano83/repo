package it.fivano.symusic;

import java.util.LinkedList;
import java.util.List;

public class LoggerSync {
	
	private List<LoggerSyncObject> listLog;
	
	public enum LogLevel {
		DEBUG,
		INFO,
		WARN,
		ERROR
	}
	
	public LoggerSync() {
		listLog = new LinkedList<LoggerSyncObject>();
	}

	public void addLog(LogLevel level, String message) {
		listLog.add(new LoggerSyncObject(level.toString(), message));
	}
	
	public void addLog(LogLevel level, String message, Throwable exception) {
		listLog.add(new LoggerSyncObject(level.toString(), message, exception));
	}

	public void printLog(MyLogger logger) {
		for(LoggerSyncObject lineLog : listLog) {
			
			if(LogLevel.ERROR.toString().equals(lineLog.getLevel())) {
				if(lineLog.getException()==null)
					logger.error(lineLog.getMessage());
				else
					logger.error(lineLog.getMessage(), lineLog.getException());
			}
			else if(LogLevel.WARN.toString().equals(lineLog.getLevel())) {
				logger.warn(lineLog.getMessage());
			}
			else if(LogLevel.DEBUG.toString().equals(lineLog.getLevel())) {
				logger.debug(lineLog.getMessage());
			}
			else if(LogLevel.INFO.toString().equals(lineLog.getLevel())) {
				logger.info(lineLog.getMessage());
			}
			else {
				logger.info(lineLog.getMessage()); // default
			}
		}
	}
	
}

class LoggerSyncObject {
	private String level;
	private String message;
	private Throwable exception;
	
	public LoggerSyncObject(String level, String message) {
		this.level = level;
		this.message = message;
	}
	
	public LoggerSyncObject(String level, String message, Throwable exception) {
		this.level = level;
		this.message = message;
		this.exception = exception;
	}
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Throwable getException() {
		return exception;
	}
	public void setException(Throwable exception) {
		this.exception = exception;
	}

}