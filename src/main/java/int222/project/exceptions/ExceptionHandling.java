package int222.project.exceptions;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import int222.project.exceptions.ExceptionResponse.ERROR_CODE;
import int222.project.files.ResponseMessage;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {
	
	 @ExceptionHandler(DataRelatedException.class)
	 public ResponseEntity<Object> exceptionsHandle(DataRelatedException ex, WebRequest req){
		 ExceptionResponse response = new ExceptionResponse(ex.getErrorCode(), ex.getMessage(), LocalDateTime.now());
		 ResponseEntity<Object> entity = new ResponseEntity<Object>(response , HttpStatus.NOT_FOUND);
		 
		 return entity;
	 }
	 
	 @ExceptionHandler(DataIntegrityViolationException.class)
	 public ResponseEntity<Object> dataExceptionsHandle(DataIntegrityViolationException ex){
		 ExceptionResponse response = new ExceptionResponse( ERROR_CODE.SQL_EXCEPTION, ex.getMessage(), LocalDateTime.now());
		 ResponseEntity<Object> entity = new ResponseEntity<Object>(response , HttpStatus.BAD_REQUEST);
		 return entity;
	 }
	 
	 @ExceptionHandler(MaxUploadSizeExceededException.class)
	  public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException exc) {
	    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("File too large!"));
	  }
}
