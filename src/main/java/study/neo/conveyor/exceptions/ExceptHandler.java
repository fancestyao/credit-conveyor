package study.neo.conveyor.exceptions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptHandler {
    @ExceptionHandler(FirstNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse firstNameValidationException(FirstNameException nameException) {
        return new ErrorResponse(nameException.getMessage());
    }

    @ExceptionHandler(LastNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse lastNameValidationException(LastNameException nameException) {
        return new ErrorResponse(nameException.getMessage());
    }

    @ExceptionHandler(MiddleNameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse middleNameValidationException(MiddleNameException nameException) {
        return new ErrorResponse(nameException.getMessage());
    }

    @ExceptionHandler(CreditAmountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse creditAmountValidationException(CreditAmountException creditAmountException) {
        return new ErrorResponse(creditAmountException.getMessage());
    }

    @ExceptionHandler(CreditTermException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse creditTermValidationException(CreditTermException creditTermException) {
        return new ErrorResponse(creditTermException.getMessage());
    }

    @ExceptionHandler(BirthDateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse birthDateValidationException(BirthDateException birthDateException) {
        return new ErrorResponse(birthDateException.getMessage());
    }

    @ExceptionHandler(EmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse emailValidationException(EmailException emailException) {
        return new ErrorResponse(emailException.getMessage());
    }

    @ExceptionHandler(PassportException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse passportValidationException(PassportException passportException) {
        return new ErrorResponse(passportException.getMessage());
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