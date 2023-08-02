package study.neo.conveyor.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.neo.conveyor.dtos.LoanApplicationRequestDTO;
import study.neo.conveyor.dtos.LoanOfferDTO;
import study.neo.conveyor.services.classes.LoanOfferService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Тест сервиса LoanOfferService.")
public class LoanOfferServiceTest {
    private LoanOfferService loanOfferService;
    private LoanApplicationRequestDTO loanApplicationRequestDTO;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;

    @BeforeEach
    void setup() {
        loanOfferService = new LoanOfferService();
        loanApplicationRequestDTO = LoanApplicationRequestDTO.builder()
                .firstName("TestFirstName")
                .lastName("TestLastName")
                .middleName("TestMiddleName")
                .term(6)
                .amount(BigDecimal.valueOf(13000))
                .birthDate(LocalDate.of(1999, 5, 18))
                .email("TestEmail@mail.ru")
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
    }

    @Test
    @DisplayName("Тестирование создания loanOffer с ЗП и страховкой.")
    void createLoanOfferTestWhileIsInsuranceEnabledIsTrueAndIsSalaryClientTrue() throws IOException {
        isInsuranceEnabled = Boolean.TRUE;
        isSalaryClient = Boolean.TRUE;
        LoanOfferDTO expectedResult = LoanOfferDTO.builder()
                .requestedAmount(BigDecimal.valueOf(13000))
                .term(6)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .totalAmount(BigDecimal.valueOf(63000))
                .rate(BigDecimal.valueOf(14.0))
                .build();
        LoanOfferDTO result = loanOfferService
                .createLoanOffer(isInsuranceEnabled, isSalaryClient, loanApplicationRequestDTO);
        assertEquals(expectedResult.getRate(), result.getRate());
        assertEquals(expectedResult.getTotalAmount(), result.getTotalAmount());
        assertEquals(expectedResult.getIsInsuranceEnabled(), result.getIsInsuranceEnabled());
        assertEquals(expectedResult.getIsSalaryClient(), result.getIsSalaryClient());
    }

    @Test
    @DisplayName("Тестирование создания loanOffer с ЗП, но без страховкой.")
    void createLoanOfferTestWhileIsInsuranceEnabledIsFalseAndIsSalaryClientTrue() throws IOException {
        isInsuranceEnabled = Boolean.FALSE;
        isSalaryClient = Boolean.TRUE;
        LoanOfferDTO expectedResult = LoanOfferDTO.builder()
                .requestedAmount(BigDecimal.valueOf(13000))
                .term(6)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .totalAmount(BigDecimal.valueOf(38000))
                .rate(BigDecimal.valueOf(15.0))
                .build();
        LoanOfferDTO result = loanOfferService
                .createLoanOffer(isInsuranceEnabled, isSalaryClient, loanApplicationRequestDTO);
        assertEquals(expectedResult.getRate(), result.getRate());
        assertEquals(expectedResult.getTotalAmount(), result.getTotalAmount());
        assertEquals(expectedResult.getIsInsuranceEnabled(), result.getIsInsuranceEnabled());
        assertEquals(expectedResult.getIsSalaryClient(), result.getIsSalaryClient());
    }

    @Test
    @DisplayName("Тестирование создания loanOffer без ЗП, но со страховкой.")
    void createLoanOfferTestWhileIsInsuranceEnabledIsTrueAndIsSalaryClientFalse() throws IOException {
        isInsuranceEnabled = Boolean.TRUE;
        isSalaryClient = Boolean.FALSE;
        LoanOfferDTO expectedResult = LoanOfferDTO.builder()
                .requestedAmount(BigDecimal.valueOf(13000))
                .term(6)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .totalAmount(BigDecimal.valueOf(25500))
                .rate(BigDecimal.valueOf(16.0))
                .build();
        LoanOfferDTO result = loanOfferService
                .createLoanOffer(isInsuranceEnabled, isSalaryClient, loanApplicationRequestDTO);
        assertEquals(expectedResult.getRate(), result.getRate());
        assertEquals(expectedResult.getTotalAmount(), result.getTotalAmount());
        assertEquals(expectedResult.getIsInsuranceEnabled(), result.getIsInsuranceEnabled());
        assertEquals(expectedResult.getIsSalaryClient(), result.getIsSalaryClient());
    }

    @Test
    @DisplayName("Тестирование создания loanOffer без ЗП и страховки.")
    void createLoanOfferTestWhileIsInsuranceEnabledIsFalseAndIsSalaryClientFalse() throws IOException {
        isInsuranceEnabled = Boolean.FALSE;
        isSalaryClient = Boolean.FALSE;
        LoanOfferDTO expectedResult = LoanOfferDTO.builder()
                .requestedAmount(BigDecimal.valueOf(13000))
                .term(6)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .totalAmount(BigDecimal.valueOf(13000))
                .rate(BigDecimal.valueOf(17.0))
                .build();
        LoanOfferDTO result = loanOfferService
                .createLoanOffer(isInsuranceEnabled, isSalaryClient, loanApplicationRequestDTO);
        assertEquals(expectedResult.getRate(), result.getRate());
        assertEquals(expectedResult.getTotalAmount(), result.getTotalAmount());
        assertEquals(expectedResult.getIsInsuranceEnabled(), result.getIsInsuranceEnabled());
        assertEquals(expectedResult.getIsSalaryClient(), result.getIsSalaryClient());
    }
}
