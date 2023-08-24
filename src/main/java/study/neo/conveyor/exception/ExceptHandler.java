package study.neo.conveyor.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptHandler {
    @ExceptionHandler(CreditAmountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse creditAmountValidationException(CreditAmountException creditAmountException) {
        return new ErrorResponse(creditAmountException.getMessage());
    }
    @ExceptionHandler(BirthDateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse birthDateValidationException(BirthDateException birthDateException) {
        return new ErrorResponse(birthDateException.getMessage());
    }

    @ExceptionHandler(UnemployedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse unemployedException(UnemployedException unemployedException) {
        return new ErrorResponse(unemployedException.getMessage());
    }

    @ExceptionHandler(WorkExperienceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse workExperienceException(WorkExperienceException workExperienceException) {
        return new ErrorResponse(workExperienceException.getMessage());
    }

    @Data
    @RequiredArgsConstructor
    private static class ErrorResponse {
        private final String error;
        private String description;
    }
}