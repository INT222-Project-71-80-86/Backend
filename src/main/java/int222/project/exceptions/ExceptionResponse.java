package int222.project.exceptions;

import java.time.LocalDateTime;

public class ExceptionResponse {
	
	 public static enum ERROR_CODE {
	        ITEM_DOES_NOT_EXIST(1001), ITEM_ALREADY_EXIST(1002),
	        PRODUCT_DOESNT_FOUND(2001), PRODUCT_ALREADY_EXIST(2002),
	        COLOR_DOESNT_FOUND(3001), NOT_HAVE_ANY_COLORS(3002),
	        BRAND_DOESNT_FOUND(4001), BRAND_ALREADY_EXIST(4002),
	        SQL_EXCEPTION(5001);
		 
	        private int value;

	        ERROR_CODE(int value) {
	            this.value = value;
	        }
	    }

	    private ERROR_CODE errorCode;
	    private String message;
	    private LocalDateTime dateTime;


	    public ExceptionResponse(ERROR_CODE errorCode, String message, LocalDateTime dateTime) {
	        this.errorCode = errorCode;
	        this.message = message;
	        this.dateTime = dateTime;
	    }

	    public ERROR_CODE getErrorCode() {
	        return errorCode;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public LocalDateTime getDateTime() {
	        return dateTime;
	    }

	    public void setDateTime(LocalDateTime dateTime) {
	        this.dateTime = dateTime;
	    }
}
