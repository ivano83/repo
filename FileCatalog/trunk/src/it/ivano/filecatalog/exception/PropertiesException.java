package it.ivano.filecatalog.exception;

public class PropertiesException extends Exception {

	private static final long serialVersionUID = 1020644448556259574L;

	public PropertiesException(Throwable t) {
		super(t);
	}
	
	public PropertiesException(String msg, Throwable t) {
		super(msg,t);
	}
	
	public PropertiesException(String msg) {
		super(msg);
	}
	
}
