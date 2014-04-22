package it.ivano.filecatalog.exception;

public class FileDataException extends Exception {

	private static final long serialVersionUID = 1020644448556259573L;

	public FileDataException(Throwable t) {
		super(t);
	}
	
	public FileDataException(String msg, Throwable t) {
		super(msg,t);
	}
	
	public FileDataException(String msg) {
		super(msg);
	}
	
}
