package int222.project.exceptions;

import java.time.LocalDateTime;

public class ExceptionResponse {
	
	 public static enum ERROR_CODE {
	        ITEM_DOES_NOT_EXIST(1001), ITEM_ALREADY_EXIST(1002), ITEM_DELETED(1003), ITEM_INSUFFICIENT(1004),
	        PRODUCT_DOESNT_FOUND(2001), PRODUCT_ALREADY_EXIST(2002), INVALID_PRODUCT_ATTRIBUTE(2003),
	        COLOR_DOESNT_FOUND(3001), NOT_HAVE_ANY_COLORS(3002), COLOR_ALREADY_EXIST(3003),
	        BRAND_DOESNT_FOUND(4001), BRAND_ALREADY_EXIST(4002),
	        SQL_EXCEPTION(5001),
	        INVALID_ATTRIBUTE(6666),
	        USER_DOESNT_FOUND(7001), USER_ALREADY_EXIST(7002), USER_NOT_THE_SAME(7003), INVALID_USER_ATTRIBUTE(7004), INSUFFICIENT_PERMISSION(7005),
	        ORDER_DOESNT_FOUND(8001),ORDER_ALREADY_EXIST(8002),ORDER_DOESNT_CONTAIN_PRODUCT(8003),ORDER_ATTRIBUTE_INVALID(8004),ORDER_CANCELED(8005),
	        COUPON_DOESNT_FOUND(9001),COUPON_ALREADY_EXIT(9002),COUPON_ALREADY_USED(9003),COUPON_EXPIRED(9004),COUPON_EXCEED_MAX_USAGE(9005),COUPON_DOESNT_MEET_MIN_PRICE(9006),COUPON_INVALID(9007);
		 
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
