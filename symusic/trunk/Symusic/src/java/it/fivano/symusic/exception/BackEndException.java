package it.fivano.symusic.exception;

public class BackEndException extends Exception {

	private static final long serialVersionUID = -6061494130864159439L;
	
	public BackEndException() {
		super();
	}

	public BackEndException(String s) {
		super(s);
	}
	
	public BackEndException(String s, Throwable t) {
		super(s,t);
	}
	
	public BackEndException(Throwable t) {
		super(t);
	}
}
