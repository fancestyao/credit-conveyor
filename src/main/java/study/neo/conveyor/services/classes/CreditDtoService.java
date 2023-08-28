package study.neo.conveyor.services.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import study.neo.conveyor.dtos.CreditDTO;
import study.neo.conveyor.dtos.PaymentScheduleElement;
import study.neo.conveyor.dtos.ScoringDataDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreditDtoService {
    private final PaymentScheduleElementService paymentScheduleElementService;
    private final ScoreCreditRate scoreCreditRate;
    private final LoanOfferService loanOfferService;

    public CreditDTO createCreditDTO(ScoringDataDTO scoringDataDTO) throws IOException {
        log.info("Создаем creditDTO с входными данными scoringDataDTO: {}", scoringDataDTO);
        BigDecimal rate = scoreCreditRate.scoreData(scoringDataDTO);
        log.info("Значение параметра rate: {}", rate);
        BigDecimal monthlyPayment = loanOfferService.calculateMonthlyPayment(rate, scoringDataDTO.getTerm(),
                scoringDataDTO.getAmount());
        log.info("Значение параметра monthlyPayment: {}", monthlyPayment);
        BigDecimal fullAmount = calculateFullAmount(monthlyPayment, scoringDataDTO.getTerm());
        log.info("Значение параметра fullAmount: {}", fullAmount);
        BigDecimal psk = calculatePsk(fullAmount, scoringDataDTO.getAmount(), scoringDataDTO.getTerm());
        log.info("Значение параметра psk: {}", psk);
        List<PaymentScheduleElement> paymentScheduleElements = paymentScheduleElementService
                .compileListOfPaymentScheduleElements(scoringDataDTO.getTerm(), monthlyPayment,
                        scoringDataDTO.getAmount(), rate);
        log.info("Значение параметра paymentScheduleElements: {}", paymentScheduleElements);
        return CreditDTO.builder()
                .amount(scoringDataDTO.getAmount().setScale(2, RoundingMode.HALF_UP))
                .term(scoringDataDTO.getTerm())
                .isSalaryClient(scoringDataDTO.getIsSalaryClient())
                .isInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled())
                .rate(rate.setScale(2, RoundingMode.HALF_UP))
                .monthlyPayment(monthlyPayment.setScale(2, RoundingMode.HALF_UP))
                .psk(psk.setScale(2, RoundingMode.HALF_UP))
                .paymentSchedule(paymentScheduleElements)
                .build();
    }

    private BigDecimal calculatePsk(BigDecimal fullAmount, BigDecimal amount, Integer term) {
        log.info("Рассчитываем значение psk с входными данными: fullAmount {}, " +
                "amount {}, term {}", fullAmount, amount, term);
        BigDecimal divideAmounts = fullAmount.divide(amount, RoundingMode.HALF_EVEN);
        log.info("Рассчитываем значение divideAmounts {}", divideAmounts);
        BigDecimal numerator = divideAmounts.subtract(BigDecimal.valueOf(1));
        log.info("Рассчитываем значение numerator {}", numerator);
        BigDecimal divideNumerator = numerator.divide(BigDecimal.valueOf(term / 12), RoundingMode.HALF_EVEN);
        log.info("Рассчитываем значение divideNumerator {}", divideNumerator);
        BigDecimal psk = divideNumerator.multiply(BigDecimal.valueOf(100));
        log.info("Рассчитываем значение psk {}", psk);
        return psk;
    }

    private BigDecimal calculateFullAmount(BigDecimal monthlyPayment, Integer term) {
        log.info("Рассчитываем значение fullAmount с входными данными: monthlyPayment {}, " +
                "term {}", monthlyPayment, term);
        return monthlyPayment.multiply(BigDecimal.valueOf(term));
    }
}
