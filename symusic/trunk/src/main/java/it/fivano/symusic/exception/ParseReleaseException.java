package it.fivano.symusic.exception;

public class ParseReleaseException extends Exception {

	private static final long serialVersionUID = -6061494130864159469L;
	
	public ParseReleaseException() {
		super();
	}

	public ParseReleaseException(String s) {
		super(s);
	}
	
	public ParseReleaseException(String s, Throwable t) {
		super(s,t);
	}
}
