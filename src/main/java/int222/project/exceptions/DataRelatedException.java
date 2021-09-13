package int222.project.exceptions;

public class DataRelatedException extends RuntimeException{
	
	ExceptionResponse.ERROR_CODE errorCode;

	public DataRelatedException(ExceptionResponse.ERROR_CODE errorCode, String s) {
		super(s);
		this.errorCode = errorCode;
	}

	public ExceptionResponse.ERROR_CODE getErrorCode() {
		return errorCode;
	}
	
	

}
