package study.neo.conveyor.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.neo.conveyor.dtos.*;
import study.neo.conveyor.enums.EmploymentPosition;
import study.neo.conveyor.enums.EmploymentStatus;
import study.neo.conveyor.enums.Gender;
import study.neo.conveyor.enums.MaritalStatus;
import study.neo.conveyor.services.classes.ConveyorServiceImpl;
import study.neo.conveyor.services.classes.CreditDtoService;
import study.neo.conveyor.services.classes.LoanOfferService;
import study.neo.conveyor.validations.LoanApplicationRequestValidation;
import study.neo.conveyor.validations.ScoringDataRejectValuesValidation;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса ConveyorService.")
public class ConveyorServiceTest {
    @Mock
    ScoringDataRejectValuesValidation scoringDataRejectValuesValidation;
    @Mock
    LoanApplicationRequestValidation loanApplicationRequestValidation;
    @Mock
    private CreditDtoService creditDtoService;
    @Mock
    private LoanOfferService loanOfferService;
    @InjectMocks
    private ConveyorServiceImpl conveyorService;
    private LoanApplicationRequestDTO loanApplicationRequestDTO;
    private ScoringDataDTO scoringDataDTO;

    @BeforeEach
    void setup() {
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
        scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(300000))
                .term(24)
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1999, 5, 18))
                .passportSeries("1234")
                .passportNumber("123456")
                .passportIssueDate(LocalDate.of(2019, 5, 18))
                .passportIssueBranch("Saratov")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .employmentDTO(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .employmentINN("123412341234 123")
                        .salary(BigDecimal.valueOf(50000))
                        .workExperienceTotal(15)
                        .workExperienceCurrent(5)
                        .employmentPosition(EmploymentPosition.TOP_CLASS_MANAGER)
                        .build())
                .account("1234567890")
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.FALSE)
                .build();
    }

    @Test
    @DisplayName("Мок-тестирование метода offers сервиса ConveyorService.")
    public void offersTest() throws IOException {
        LoanOfferDTO loanOfferDTO1 = new LoanOfferDTO();
        loanOfferDTO1.setTotalAmount(BigDecimal.valueOf(300000));
        loanOfferDTO1.setRate(BigDecimal.valueOf(18));
        loanOfferDTO1.setApplicationId(3L);
        when(loanOfferService
                .createLoanOffer(any(Boolean.class), any(Boolean.class), any(LoanApplicationRequestDTO.class)))
                .thenReturn(loanOfferDTO1);
        List<LoanOfferDTO> result = conveyorService.offers(loanApplicationRequestDTO);
        assertEquals(result.get(0), loanOfferDTO1);
        assertEquals(4, result.size());
    }

    @Test
    @DisplayName("Мок-тестирование метода calculation сервиса ConveyorService.")
    public void calculationTest() throws IOException {
        CreditDTO creditDTO = new CreditDTO();
        creditDTO.setTerm(18);
        creditDTO.setAmount(BigDecimal.valueOf(300000));
        creditDTO.setRate(BigDecimal.valueOf(15));
        when(creditDtoService
                .createCreditDTO(any(ScoringDataDTO.class)))
                .thenReturn(creditDTO);
        CreditDTO result = conveyorService.calculation(scoringDataDTO);
        assertEquals(result, creditDTO);
    }
}