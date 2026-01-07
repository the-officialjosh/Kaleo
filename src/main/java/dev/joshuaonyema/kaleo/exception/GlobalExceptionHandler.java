package dev.joshuaonyema.kaleo.exception;

import dev.joshuaonyema.kaleo.api.dto.response.ErrorDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public  ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException notFoundException){
        log.error("Caught UserNotFoundException: {}", String.valueOf(notFoundException));

        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("User not found");
        return new ResponseEntity<>(errorDto, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException argumentNotValidException){
        log.error("Caught MethodArgumentNotValidException: {}", String.valueOf(argumentNotValidException));
        ErrorDto errorDto = new ErrorDto();

        String validationErrorOccurred = argumentNotValidException.getBindingResult()
                .getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError
                        .getField() + ": " + fieldError.getDefaultMessage())
                .orElse("Validation error occurred");


        errorDto.setError(validationErrorOccurred);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException violationException){
        log.error("Caught ConstraintViolationException: {}", String.valueOf(violationException));
        ErrorDto errorDto = new ErrorDto();

        String errorMessage = violationException.getConstraintViolations()
                .stream().findFirst()
                .map(violation -> violation
                        .getPropertyPath() + ": " + violation.getMessage())
                .orElse("Constraint violation occurred");

        errorDto.setError(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception exception){
        log.error("Caught Exception: {}", String.valueOf(exception));

        ErrorDto errorDto = new ErrorDto();
        errorDto.setError("An unknown error occurred");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
