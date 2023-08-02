package study.neo.conveyor.validations;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import study.neo.conveyor.dtos.LoanApplicationRequestDTO;
import study.neo.conveyor.exceptions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Component
@Slf4j
public class LoanApplicationRequestValidation {
    private static final Integer NAME_MIN_SYMBOLS_RESTRICTION = 2;
    private static final Integer NAME_MAX_SYMBOLS_RESTRICTION = 30;
    private static final BigDecimal CREDIT_AMOUNT_RESTRICTION = new BigDecimal("10000");
    private static final String LATIN_SYMBOLS_REGEX_RESTRICTION = "^[a-zA-Z]*$";
    private static final Integer CREDIT_TERM_RESTRICTION = 6;
    private static final Integer AGE_OF_ADULTHOOD = 18;
    private static final String EMAIL_REGEX_RESTRICTION = "[\\w.]{2,50}@[\\w.]{2,20}";
    private static final Integer PASSPORT_SERIES_LENGTH_RESTRICTION = 4;
    private static final Integer PASSPORT_NUMBER_LENGTH_RESTRICTION = 6;

    public void callAllValidations(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Валидируем имя для loanApplicationRequestDTO {}", loanApplicationRequestDTO);
        firstNameValidation(loanApplicationRequestDTO.getFirstName());
        log.info("Валидируем фамилию для loanApplicationRequestDTO {}", loanApplicationRequestDTO);
        lastNameValidation(loanApplicationRequestDTO.getLastName());
        log.info("Валидируем отчество для loanApplicationRequestDTO {}", loanApplicationRequestDTO);
        middleNameValidation(loanApplicationRequestDTO.getMiddleName());
        log.info("Валидируем желаемую сумму кредита для loanApplicationRequestDTO {}", loanApplicationRequestDTO);
        amountValidation(loanApplicationRequestDTO.getAmount());
        log.info("Валидируем предполагаемый срок на погашение кредита для loanApplicationRequestDTO {}", loanApplicationRequestDTO);
        termValidation(loanApplicationRequestDTO.getTerm());
        log.info("Валидируем дату рождения для loanApplicationRequestDTO {}", loanApplicationRequestDTO);
        birthDateValidation(loanApplicationRequestDTO.getBirthDate());
        log.info("Валидируем электронную почту для loanApplicationRequestDTO {}", loanApplicationRequestDTO);
        emailValidation(loanApplicationRequestDTO.getEmail());
        log.info("Валидируем паспортные данные для loanApplicationRequestDTO {}", loanApplicationRequestDTO);
        passportValidation(loanApplicationRequestDTO.getPassportSeries(),
                loanApplicationRequestDTO.getPassportNumber());
    }

    private void firstNameValidation(String firstName) {
        if (!firstName.matches(LATIN_SYMBOLS_REGEX_RESTRICTION)) {
            log.info("Имя {} не прошло валидацию на латинские буквы", firstName);
            throw new FirstNameException("При заполнении имени следует использовать латинские буквы.");
        } else if (firstName.length() < NAME_MIN_SYMBOLS_RESTRICTION) {
            log.info("Имя {} не прошло валидацию на минимальное количество букв", firstName);
            throw new FirstNameException("Минимальная длина имени составляет "
                    + NAME_MIN_SYMBOLS_RESTRICTION + " символа.");
        } else if (firstName.length() > NAME_MAX_SYMBOLS_RESTRICTION) {
            log.info("Имя {} не прошло валидацию на максимальное количество букв", firstName);
            throw new FirstNameException("Максимальная длина имени составляет "
                    + NAME_MAX_SYMBOLS_RESTRICTION + " символов.");
        }
    }

    private void lastNameValidation(String lastName) {
        if (!lastName.matches(LATIN_SYMBOLS_REGEX_RESTRICTION)) {
            log.info("Фамилия {} не прошла валидацию на латинские буквы", lastName);
            throw new LastNameException("При заполнении фамилии следует использовать латинские буквы.");
        } else if (lastName.length() < NAME_MIN_SYMBOLS_RESTRICTION) {
            log.info("Фамилия {} не прошла валидацию на минимальное количество букв", lastName);
            throw new LastNameException("Минимальная длина фамилии составляет "
                    + NAME_MIN_SYMBOLS_RESTRICTION + " символа.");
        } else if (lastName.length() > NAME_MAX_SYMBOLS_RESTRICTION) {
            log.info("Фамилия {} не прошла валидацию на максимальное количество букв", lastName);
            throw new LastNameException("Максимальная длина фамилии составляет "
                    + NAME_MAX_SYMBOLS_RESTRICTION + " символов.");
        }
    }

    private void middleNameValidation(String middleName) {
        if (middleName != null) {
            if (!middleName.matches(LATIN_SYMBOLS_REGEX_RESTRICTION)) {
                log.info("Отчество {} не прошло валидацию на латинские буквы", middleName);
                throw new MiddleNameException("При заполнении отчества следует использовать латинские буквы.");
            } else if (middleName.length() < NAME_MIN_SYMBOLS_RESTRICTION) {
                log.info("Отчество {} не прошло валидацию на минимальное количество букв", middleName);
                throw new MiddleNameException("Минимальная длина отчества составляет "
                        + NAME_MIN_SYMBOLS_RESTRICTION + " символа.");
            } else if (middleName.length() > NAME_MAX_SYMBOLS_RESTRICTION) {
                log.info("Отчество {} не прошло валидацию на максимальное количество букв", middleName);
                throw new MiddleNameException("Максимальная длина отчества составляет "
                        + NAME_MAX_SYMBOLS_RESTRICTION + " символов.");
            }
        }
    }

    private void amountValidation(BigDecimal amount) {
        if (amount.compareTo(CREDIT_AMOUNT_RESTRICTION) < 0) {
            log.info("Желаемая сумма кредита {} не прошла валидацию на минимальное значение", amount);
            throw new CreditAmountException("Значение суммы кредита должно быть больше или равно "
                    + CREDIT_AMOUNT_RESTRICTION);
        }
    }

    private void termValidation(Integer term) {
        if (term.compareTo(CREDIT_TERM_RESTRICTION) < 0) {
            log.info("Предполагаемый срок кредита {} не прошел валидацию на минимальное значение", term);
            throw new CreditTermException("Значение срока кредита должно быть больше или равно "
                    + CREDIT_TERM_RESTRICTION);
        }
    }

    private void birthDateValidation(LocalDate birthDate) {
        if (LocalDate.now().compareTo(birthDate) < AGE_OF_ADULTHOOD) {
            log.info("Дата рождения {} не прошла валидацию на минимальное значение", birthDate);
            throw new BirthDateException("Необходимо достичь совершеннолетия.");
        }
    }

    private void emailValidation(String email) {
        if (!email.matches(EMAIL_REGEX_RESTRICTION)) {
            log.info("Электронная почта {} не прошла валидацию на установленный формат", email);
            throw new EmailException("Неверный формат заполненной почты.");
        }
    }

    private void passportValidation(String passportSeries, String passportNumber) {
        if (passportSeries.length() != PASSPORT_SERIES_LENGTH_RESTRICTION) {
            log.info("Серия паспорта {} не прошла валидацию по количество цифр", passportSeries);
            throw new PassportException("Серия паспорта должна составлять "
                    + PASSPORT_SERIES_LENGTH_RESTRICTION + " цифры.");
        }
        if (passportNumber.length() != PASSPORT_NUMBER_LENGTH_RESTRICTION) {
            log.info("Номер паспорта {} не прошел валидацию по количество цифр", passportSeries);
            throw new PassportException("Номер паспорта должен составлять "
                    + PASSPORT_NUMBER_LENGTH_RESTRICTION + "цифр.");
        }
    }
}
