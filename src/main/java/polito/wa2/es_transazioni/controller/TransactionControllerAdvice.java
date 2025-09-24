package polito.wa2.es_transazioni.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TransactionControllerAdvice {


    // =========================
    // Validation errors (@Valid)
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation error");
        pd.setType(URI.create("https://example.org/validation-error"));
        pd.setProperty("errors", errors);
        return pd;
    }

    // =========================
    // JSON deserialization errors
    // =========================
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleInvalidJson(HttpMessageNotReadableException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Invalid input data");
        pd.setDetail(ex.getMostSpecificCause().getMessage());
        pd.setType(URI.create("https://example.org/invalid-json-error"));
        return pd;
    }


    // =========================
    // Type mismatch errors (e.g., /transactions?id=abc)
    // =========================
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Type mismatch error");
        pd.setDetail("Parameter '" + ex.getName() + "' should be of type '" +
                (ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown") + "'");
        pd.setType(URI.create("https://example.org/type-mismatch-error"));
        return pd;
    }

    // =========================
    // Illegal arguments
    // =========================
    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Invalid argument");
        pd.setDetail(ex.getMessage());
        pd.setType(URI.create("https://example.org/invalid-argument-error"));
        return pd;
    }

    // =========================
    // Database errors
    // =========================
    @ExceptionHandler(DataAccessException.class)
    public ProblemDetail handleDataAccessException(DataAccessException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        pd.setTitle("Database connection error");
        pd.setDetail("There was an error connecting to the database. Please try again later.");
        pd.setType(URI.create("https://example.org/database-connection-error"));
        return pd;
    }

    // =========================
    // Catch-all for other exceptions
    // =========================
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Internal server error");
        pd.setDetail(ex.getMessage());
        pd.setType(URI.create("https://example.org/internal-server-error"));
        return pd;
    }
}

