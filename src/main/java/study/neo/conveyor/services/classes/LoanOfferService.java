package study.neo.conveyor.services.classes;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import study.neo.conveyor.dtos.LoanApplicationRequestDTO;
import study.neo.conveyor.dtos.LoanOfferDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@RequiredArgsConstructor
@Component
@Slf4j
public class LoanOfferService {
    private final CalculationService calculationService;

    public LoanOfferDTO createLoanOffer(Boolean isInsuranceEnabled,
                                         Boolean isSalaryClient,
                                         LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Создаем кредитное предложение с входными данными: isInsuranceEnabled {}, isSalaryClient {}, " +
                        "loanApplicationRequestDto {}", isInsuranceEnabled, isSalaryClient, loanApplicationRequestDTO);
        BigDecimal totalAmount = calculationService.calculateTotalAmount(isInsuranceEnabled,
                isSalaryClient, loanApplicationRequestDTO.getAmount());
        log.info("Значение totalAmount: {}", totalAmount);
        BigDecimal rate = calculationService.calculateRate(isInsuranceEnabled, isSalaryClient);
        log.info("Значение rate: {}", rate);
        BigDecimal monthlyPayment = calculationService.calculateMonthlyPayment(rate, loanApplicationRequestDTO.getTerm(), totalAmount);
        log.info("Значение monthlyPayment: {}", monthlyPayment);
        LoanOfferDTO resultLoanOfferDto = LoanOfferDTO.builder()
                .requestedAmount(loanApplicationRequestDTO.getAmount())
                .term(loanApplicationRequestDTO.getTerm())
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .totalAmount(totalAmount)
                .rate(rate)
                .monthlyPayment(monthlyPayment.setScale(2, RoundingMode.HALF_EVEN))
                .build();
        log.info("Конечное значение loanOfferDto в методе " +
                "createLoanOfferDto сервиса LoanOfferService {}", resultLoanOfferDto);
        return resultLoanOfferDto;
    }
}
