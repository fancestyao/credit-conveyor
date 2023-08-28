package study.neo.conveyor.validations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.neo.conveyor.dtos.LoanApplicationRequestDTO;
import study.neo.conveyor.exceptions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Тест компонента валидации LoanApplicationRequestValidation.")
public class LoanApplicationRequestValidationTest {
    private LoanApplicationRequestValidation loanApplicationRequestValidation;
    @Test
    @DisplayName("Не английские символы в имени.")
    void firstNameNonlatinSymbolsTest() {
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
        loanApplicationRequestValidation = new LoanApplicationRequestValidation();
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
