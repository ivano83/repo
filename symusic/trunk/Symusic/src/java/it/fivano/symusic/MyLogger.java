package it.fivano.symusic;


import it.fivano.symusic.LoggerSync.LogLevel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyLogger {
		
	private String className;
	private Logger log;
	
	private boolean syncronized;
	private LoggerSync loggerSync;

	public MyLogger(Class<?> classe) {
		
		className = classe.getSimpleName();
		
		log = LoggerFactory.getLogger(className);

	}
	
	public void debug(String message) {
		if(syncronized)
			loggerSync.addLog(LogLevel.DEBUG, "["+className+"]\t"+message);
		else if(loggerSync!=null)
			log.debug(message);
		else
			log.debug("["+className+"]\t"+message);
	}
	
	public void info(String message) {
		if(syncronized)
			loggerSync.addLog(LogLevel.INFO, "["+className+"]\t"+message);
		else if(loggerSync!=null)
			log.info(message);
		else
			log.info("["+className+"]\t"+message);
	}
	
	public void warn(String message) {
		if(syncronized)
			loggerSync.addLog(LogLevel.WARN, "["+className+"]\t"+message);
		else if(loggerSync!=null)
			log.warn(message);
		else
			log.warn("["+className+"]\t"+message);
	}
	
	public void error(String message) {
		
		if(syncronized)
			loggerSync.addLog(LogLevel.ERROR, "["+className+"]\t"+message);
		else if(loggerSync!=null)
			log.error(message);
		else
			log.error("["+className+"]\t"+message);
	}
	
	public void error(String message, Throwable t) {
		String stackTrace = getStackTrace(t);
		
		if(syncronized)
			loggerSync.addLog(LogLevel.ERROR, "["+className+"]\t"+message +"\n" + stackTrace);
		else if(loggerSync!=null)
			log.error(message +"\n" + stackTrace);
		else
			log.error("["+className+"]\t"+message +"\n" + stackTrace);
	}

	/**
	 * Il log è attivato in modalita' sincronizzata
	 * In questo stato i log vengono salvati in una lista, 
	 * per poi essere stampati in una unica volta
	 * @param loggerSync oggetto che contiene le righe di log
	 */
	public void startSyncLog(LoggerSync loggerSync) {
		this.loggerSync = loggerSync;
		syncronized = true;
	}
	/**
	 * Il log sincronizzato viene sospeso.
	 * In questo stato i log vengono stampati senza il nome della classe
	 * chiamante (in quanto e' gia' incorporato nel log)
	 */
	public void stopSyncLog() {
		syncronized = false;
	}
	/**
	 * Il log sincronizzato viene completamente disattivato
	 * In questo stato i log vengono stampati nella modalita' standard
	 */
	public void cleanSyncLog() {
		this.loggerSync = null;
		syncronized = false;
	}
	
	private static String getStackTrace(Throwable aThrowable) {
		StringBuffer buff = new StringBuffer();
		
		buff.append(aThrowable.getMessage()+"\n");
		processThrowable(aThrowable, buff);
		
		Throwable t = aThrowable.getCause();
		while(t!=null) {
			buff.append("Caused by: "+t.getMessage()+"\n");
			processThrowable(t, buff);
			t = t.getCause();
		}

		return buff.toString();
	}
	
	private static void processThrowable(Throwable t, StringBuffer buff) {
		int lastInd = 0;
		int ind = 0;
		for(StackTraceElement tmp : t.getStackTrace()) {
			if(ind<5) {
				buff.append("\tat "+tmp.toString()+"\n");
				lastInd++;
			}
			else if(tmp.getClassName().startsWith("it.fivano.symusic")) {
				buff.append("\tat "+tmp.toString()+"\n");
				if(ind>lastInd+1)
					buff.append("\t"+"..."+"\n");
			}
			else if(ind==5) {
				buff.append("\t"+"..."+"\n");
			}
			
			ind++;
		}
	}

}
