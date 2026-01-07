package dev.joshuaonyema.kaleo.exception;

import dev.joshuaonyema.kaleo.api.dto.ErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    // ==================== handleUserNotFoundException Tests ====================

    @Test
    void handleUserNotFoundException_whenCalled_thenReturnsUnauthorizedStatus() {
        UserNotFoundException exception = new UserNotFoundException("User with ID '123' not found");

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleUserNotFoundException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void handleUserNotFoundException_whenCalled_thenReturnsErrorDto() {
        UserNotFoundException exception = new UserNotFoundException("User not found");

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleUserNotFoundException(exception);

        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().getError());
    }

    @Test
    void handleUserNotFoundException_whenCalledWithNullMessage_thenReturnsErrorDto() {
        UserNotFoundException exception = new UserNotFoundException();

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleUserNotFoundException(exception);

        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().getError());
    }

    // ==================== handleMethodArgumentNotValidException Tests ====================

    @Test
    void handleMethodArgumentNotValidException_whenCalled_thenReturnsBadRequestStatus() {
        MethodArgumentNotValidException exception = createMethodArgumentNotValidException("name", "must not be blank");

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleMethodArgumentNotValidException_whenFieldError_thenReturnsFieldErrorMessage() {
        MethodArgumentNotValidException exception = createMethodArgumentNotValidException("name", "must not be blank");

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception);

        assertNotNull(response.getBody());
        assertEquals("name: must not be blank", response.getBody().getError());
    }

    @Test
    void handleMethodArgumentNotValidException_whenNoFieldErrors_thenReturnsDefaultMessage() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception);

        assertNotNull(response.getBody());
        assertEquals("Validation error occurred", response.getBody().getError());
    }

    @Test
    void handleMethodArgumentNotValidException_whenMultipleFieldErrors_thenReturnsFirstError() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(
                new FieldError("object", "name", "must not be blank"),
                new FieldError("object", "venue", "is required")
        ));

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception);

        assertNotNull(response.getBody());
        assertEquals("name: must not be blank", response.getBody().getError());
    }

    // ==================== handleConstraintViolation Tests ====================

    @Test
    void handleConstraintViolation_whenCalled_thenReturnsBadRequestStatus() {
        ConstraintViolationException exception = createConstraintViolationException("field", "must not be null");

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleConstraintViolation(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleConstraintViolation_whenViolation_thenReturnsViolationMessage() {
        ConstraintViolationException exception = createConstraintViolationException("email", "must be valid");

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleConstraintViolation(exception);

        assertNotNull(response.getBody());
        assertEquals("email: must be valid", response.getBody().getError());
    }

    @Test
    void handleConstraintViolation_whenNoViolations_thenReturnsDefaultMessage() {
        ConstraintViolationException exception = new ConstraintViolationException(Set.of());

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleConstraintViolation(exception);

        assertNotNull(response.getBody());
        assertEquals("Constraint violation occurred", response.getBody().getError());
    }

    // ==================== handleException Tests ====================

    @Test
    void handleException_whenCalled_thenReturnsInternalServerErrorStatus() {
        Exception exception = new RuntimeException("Something went wrong");

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void handleException_whenCalled_thenReturnsGenericErrorMessage() {
        Exception exception = new RuntimeException("Specific error message");

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleException(exception);

        assertNotNull(response.getBody());
        assertEquals("An unknown error occurred", response.getBody().getError());
    }

    @Test
    void handleException_whenNullPointerException_thenReturnsGenericErrorMessage() {
        Exception exception = new NullPointerException();

        ResponseEntity<ErrorDto> response = globalExceptionHandler.handleException(exception);

        assertNotNull(response.getBody());
        assertEquals("An unknown error occurred", response.getBody().getError());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ==================== Helper Methods ====================

    private MethodArgumentNotValidException createMethodArgumentNotValidException(String field, String message) {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", field, message);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        return exception;
    }

    @SuppressWarnings("unchecked")
    private ConstraintViolationException createConstraintViolationException(String propertyPath, String message) {
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(path.toString()).thenReturn(propertyPath);
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn(message);

        return new ConstraintViolationException(Set.of(violation));
    }
}

