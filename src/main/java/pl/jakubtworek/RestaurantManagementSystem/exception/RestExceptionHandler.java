package pl.jakubtworek.RestaurantManagementSystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
        ErrorResponse error = new ErrorResponse();
        error.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        error.setMessage("Accept: application/json");
        request.removeAttribute(
                HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);

        return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(EmployeeNotFoundException exc){
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(JobNotFoundException exc){
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
