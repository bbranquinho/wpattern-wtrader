package org.wtrader.loader.utils.exceptions;

public class PropertyNotFoundedException extends RuntimeException {

	private static final long serialVersionUID = 201401201324L;

	public PropertyNotFoundedException() {
		super();
	}


	public PropertyNotFoundedException(String arg0) {
		super(arg0);
	}

	public PropertyNotFoundedException(Throwable arg0) {
		super(arg0);
	}

	public PropertyNotFoundedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
