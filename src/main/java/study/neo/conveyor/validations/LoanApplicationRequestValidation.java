package study.neo.conveyor.validations;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import study.neo.conveyor.configuration.LoanApplicationPropertiesConfiguration;
import study.neo.conveyor.dtos.LoanApplicationRequestDTO;
import study.neo.conveyor.exceptions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Component
@Slf4j
public class LoanApplicationRequestValidation {
    private final LoanApplicationPropertiesConfiguration loanApplicationPropertiesConfiguration;

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
        if (!firstName.matches(loanApplicationPropertiesConfiguration.getLatinSymbolsRegexRestriction())) {
            log.info("Имя {} не прошло валидацию на латинские буквы", firstName);
            throw new FirstNameException("При заполнении имени следует использовать латинские буквы.");
        } else if (firstName.length() < loanApplicationPropertiesConfiguration.getNameMinSymbolsRestriction()) {
            log.info("Имя {} не прошло валидацию на минимальное количество букв", firstName);
            throw new FirstNameException("Минимальная длина имени составляет "
                    + loanApplicationPropertiesConfiguration.getNameMinSymbolsRestriction() + " символа.");
        } else if (firstName.length() > loanApplicationPropertiesConfiguration.getNameMaxSymbolsRestriction()) {
            log.info("Имя {} не прошло валидацию на максимальное количество букв", firstName);
            throw new FirstNameException("Максимальная длина имени составляет "
                    + loanApplicationPropertiesConfiguration.getNameMaxSymbolsRestriction() + " символов.");
        }
    }

    private void lastNameValidation(String lastName) {
        if (!lastName.matches(loanApplicationPropertiesConfiguration.getLatinSymbolsRegexRestriction())) {
            log.info("Фамилия {} не прошла валидацию на латинские буквы", lastName);
            throw new LastNameException("При заполнении фамилии следует использовать латинские буквы.");
        } else if (lastName.length() < loanApplicationPropertiesConfiguration.getNameMinSymbolsRestriction()) {
            log.info("Фамилия {} не прошла валидацию на минимальное количество букв", lastName);
            throw new LastNameException("Минимальная длина фамилии составляет "
                    + loanApplicationPropertiesConfiguration.getNameMinSymbolsRestriction() + " символа.");
        } else if (lastName.length() > loanApplicationPropertiesConfiguration.getNameMaxSymbolsRestriction()) {
            log.info("Фамилия {} не прошла валидацию на максимальное количество букв", lastName);
            throw new LastNameException("Максимальная длина фамилии составляет "
                    + loanApplicationPropertiesConfiguration.getNameMaxSymbolsRestriction() + " символов.");
        }
    }

    private void middleNameValidation(String middleName) {
        if (middleName != null) {
            if (!middleName.matches(loanApplicationPropertiesConfiguration.getLatinSymbolsRegexRestriction())) {
                log.info("Отчество {} не прошло валидацию на латинские буквы", middleName);
                throw new MiddleNameException("При заполнении отчества следует использовать латинские буквы.");
            } else if (middleName.length() < loanApplicationPropertiesConfiguration.getNameMinSymbolsRestriction()) {
                log.info("Отчество {} не прошло валидацию на минимальное количество букв", middleName);
                throw new MiddleNameException("Минимальная длина отчества составляет "
                        + loanApplicationPropertiesConfiguration.getNameMinSymbolsRestriction() + " символа.");
            } else if (middleName.length() > loanApplicationPropertiesConfiguration.getNameMaxSymbolsRestriction()) {
                log.info("Отчество {} не прошло валидацию на максимальное количество букв", middleName);
                throw new MiddleNameException("Максимальная длина отчества составляет "
                        + loanApplicationPropertiesConfiguration.getNameMaxSymbolsRestriction() + " символов.");
            }
        }
    }

    private void amountValidation(BigDecimal amount) {
        if (amount.compareTo(loanApplicationPropertiesConfiguration.getCreditAmountRestriction()) < 0) {
            log.info("Желаемая сумма кредита {} не прошла валидацию на минимальное значение", amount);
            throw new CreditAmountException("Значение суммы кредита должно быть больше или равно "
                    + loanApplicationPropertiesConfiguration.getCreditAmountRestriction());
        }
    }

    private void termValidation(Integer term) {
        if (term.compareTo(loanApplicationPropertiesConfiguration.getCreditTermRestriction()) < 0) {
            log.info("Предполагаемый срок кредита {} не прошел валидацию на минимальное значение", term);
            throw new CreditTermException("Значение срока кредита должно быть больше или равно "
                    + loanApplicationPropertiesConfiguration.getCreditTermRestriction());
        }
    }

    private void birthDateValidation(LocalDate birthDate) {
        if (LocalDate.now().compareTo(birthDate) < loanApplicationPropertiesConfiguration.getAgeOfAdulthood()) {
            log.info("Дата рождения {} не прошла валидацию на минимальное значение", birthDate);
            throw new BirthDateException("Необходимо достичь совершеннолетия.");
        }
    }

    private void emailValidation(String email) {
        if (!email.matches(loanApplicationPropertiesConfiguration.getEmailRegexRestriction())) {
            log.info("Электронная почта {} не прошла валидацию на установленный формат", email);
            throw new EmailException("Неверный формат заполненной почты.");
        }
    }

    private void passportValidation(String passportSeries, String passportNumber) {
        if (!passportSeries.matches("\\d+")) {
            log.info("Серия паспорта {} не прошла валидацию на символы", passportSeries);
            throw new PassportException("Серия паспорта должна состоять только из цифр.");
        }
        if (!passportNumber.matches("\\d+")) {
            log.info("Номер паспорта {} не прошел валидацию на символы", passportSeries);
            throw new PassportException("Номер паспорта должен состоять только из цифр.");
        }
        if (passportSeries.length() != loanApplicationPropertiesConfiguration.getPassportSeriesLengthRestriction()) {
            log.info("Серия паспорта {} не прошла валидацию по количество цифр", passportSeries);
            throw new PassportException("Серия паспорта должна составлять "
                    + loanApplicationPropertiesConfiguration.getPassportSeriesLengthRestriction() + " цифры.");
        }
        if (passportNumber.length() != loanApplicationPropertiesConfiguration.getPassportNumberLengthRestriction()) {
            log.info("Номер паспорта {} не прошел валидацию по количество цифр", passportSeries);
            throw new PassportException("Номер паспорта должен составлять "
                    + loanApplicationPropertiesConfiguration.getPassportNumberLengthRestriction() + "цифр.");
        }
    }
}
