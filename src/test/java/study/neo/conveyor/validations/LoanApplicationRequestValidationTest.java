package study.neo.conveyor.validations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.neo.conveyor.configuration.LoanApplicationPropertiesConfiguration;
import study.neo.conveyor.dtos.LoanApplicationRequestDTO;
import study.neo.conveyor.exceptions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тест компонента валидации LoanApplicationRequestValidation.")
public class LoanApplicationRequestValidationTest {
    private LoanApplicationRequestValidation loanApplicationRequestValidation;
    private final BigDecimal creditAmountRestriction = BigDecimal.valueOf(10000);

    @BeforeEach
    public void setup() {
        Integer nameMinSymbolsRestriction = 2;
        Integer nameMaxSymbolsRestriction = 30;
        String latinSymbolsRegexRestriction = "^[a-zA-Z]*$";
        Integer creditTermRestriction = 6;
        Integer ageOfAdulthood = 18;
        String emailRegexRestriction = "[\\w.]{2,50}@[\\w.]{2,20}";
        Integer passportSeriesLengthRestriction = 4;
        Integer passportNumberLengthRestriction = 6;
        LoanApplicationPropertiesConfiguration loanApplicationPropertiesConfiguration
                = new LoanApplicationPropertiesConfiguration(nameMinSymbolsRestriction,
                nameMaxSymbolsRestriction,
                creditAmountRestriction,
                latinSymbolsRegexRestriction,
                creditTermRestriction,
                ageOfAdulthood,
                emailRegexRestriction,
                passportSeriesLengthRestriction,
                passportNumberLengthRestriction);
        loanApplicationRequestValidation = new LoanApplicationRequestValidation(loanApplicationPropertiesConfiguration);
    }

    @Test
    @DisplayName("Не английские символы в имени.")
    void firstNameNonlatinSymbolsTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("ТестовоеИмя")
                .lastName("TestLastName")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(FirstNameException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Мало символов в имени.")
    void firstNameLessThanMinSymbolsTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("Q")
                .lastName("TestLastName")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(FirstNameException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Много символов в имени.")
    void firstNameMoreThanMaxSymbolsTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("VeryVeryVeryVeryVeryVeryVeryBigName")
                .lastName("TestLastName")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(FirstNameException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Не английские символы в фамилии.")
    void lastNameNonlatinSymbolsTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("ТестовоеФамилия")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(LastNameException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Мало символов в фамилии.")
    void lastNameLessThatMinSymbolsTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("Q")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(LastNameException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Много символов в фамилии.")
    void lastNameMoreThanMaxSymbolsTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("VeryVeryVeryVeryVeryVeryVeryBigLastName")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(LastNameException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Не английские символы в отчестве.")
    void middleNameNonlatinSymbolsTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .middleName("ТестовоеОтчество")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(MiddleNameException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Мало символов в отчестве.")
    void middleNameLessThatMinSymbolsTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .middleName("T")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(MiddleNameException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Много символов в отчестве.")
    void middleNameMoreThanMaxSymbolsTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .middleName("VeryVeryVeryVeryVeryVeryVeryBigMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(MiddleNameException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Отрицательный показатель суммы кредита.")
    void negativeAmountTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(-13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(CreditAmountException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Отрицательный показатель желаемого срока оплаты.")
    void negativeTermTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .middleName("TestMiddleName")
                .term(-6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(CreditTermException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }
    @Test
    @DisplayName("Несовершеннолетний.")
    void notAnAdultBirthDateTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(2020, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(BirthDateException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Неправильно заполненная почта.")
    void nonFormatEmailTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmailmail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
        assertThrows(EmailException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Неправильное количество символов в серии паспорта.")
    void notFourDigitsPassportSeriesTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("123")
                .passportNumber("123456")
                .build();
        assertThrows(PassportException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }

    @Test
    @DisplayName("Неправильное количество символов в номере паспорта.")
    void notSixDigitsPassportNumberTest() {
        LoanApplicationRequestDTO loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("1234567")
                .build();
        assertThrows(PassportException.class,
                () -> loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO));
    }
}
