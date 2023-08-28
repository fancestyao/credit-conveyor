package study.neo.conveyor.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.neo.conveyor.dtos.CreditDTO;
import study.neo.conveyor.dtos.PaymentScheduleElement;
import study.neo.conveyor.dtos.ScoringDataDTO;
import study.neo.conveyor.services.classes.CreditDtoService;
import study.neo.conveyor.services.classes.LoanOfferService;
import study.neo.conveyor.services.classes.PaymentScheduleElementService;
import study.neo.conveyor.services.classes.ScoreCreditRate;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест сервиса CreditDtoService.")
public class CreditDtoServiceTest {
    @Mock
    private PaymentScheduleElementService paymentScheduleElementService;
    @Mock
    private ScoreCreditRate scoreCreditRate;
    @Mock
    private LoanOfferService loanOfferService;
    @InjectMocks
    private CreditDtoService creditDtoService;

    @Test
    @DisplayName("Мок-тестирование метода createCreditDTO компонента ScoreCreditRate.")
    public void testCreateCreditDTO() throws IOException {
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        scoringDataDTO.setAmount(BigDecimal.valueOf(300000).setScale(2, RoundingMode.HALF_EVEN));
        scoringDataDTO.setTerm(18);
        PaymentScheduleElement paymentScheduleElement = new PaymentScheduleElement();
        paymentScheduleElement.setNumber(1);
        when(scoreCreditRate.scoreData(any(ScoringDataDTO.class))).thenReturn(BigDecimal.valueOf(15));
        when(loanOfferService.calculateMonthlyPayment(any(BigDecimal.class), any(Integer.class), any(BigDecimal.class)))
                .thenReturn(BigDecimal.valueOf(25000));
        when(paymentScheduleElementService.compileListOfPaymentScheduleElements(any(Integer.class), any(BigDecimal.class),
                any(BigDecimal.class), any(BigDecimal.class))).thenReturn(List.of(paymentScheduleElement));
        CreditDTO result = creditDtoService.createCreditDTO(scoringDataDTO);
        assertEquals(result.getAmount(), scoringDataDTO.getAmount());
        assertEquals(result.getTerm(), scoringDataDTO.getTerm());
    }
}