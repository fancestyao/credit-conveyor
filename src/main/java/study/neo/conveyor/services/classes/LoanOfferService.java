package study.neo.conveyor.services.classes;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import study.neo.conveyor.dtos.LoanApplicationRequestDTO;
import study.neo.conveyor.dtos.LoanOfferDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Properties;

@Data
@Component
@Slf4j
public class LoanOfferService {
    public LoanOfferDTO createLoanOffer(Boolean isInsuranceEnabled,
                                         Boolean isSalaryClient,
                                         LoanApplicationRequestDTO loanApplicationRequestDTO) throws IOException {
        log.info("Создаем кредитное предложение с входными данными: isInsuranceEnabled {}, isSalaryClient {}, " +
                        "loanApplicationRequestDto {}", isInsuranceEnabled, isSalaryClient, loanApplicationRequestDTO);
        BigDecimal totalAmount = calculateTotalAmount(isInsuranceEnabled,
                isSalaryClient, loanApplicationRequestDTO.getAmount());
        log.info("Значение totalAmount: {}", totalAmount);
        BigDecimal rate = calculateRate(isInsuranceEnabled, isSalaryClient);
        log.info("Значение rate: {}", rate);
        BigDecimal monthlyPayment = calculateMonthlyPayment(rate, loanApplicationRequestDTO.getTerm(), totalAmount);
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

    private BigDecimal calculateTotalAmount(Boolean isInsuranceEnabled,
                                            Boolean isSalaryClient,
                                            BigDecimal amount) {
        log.info("Рассчитываем значение totalAmount с входными данными: isInsuranceEnabled {}, " +
                "isSalaryClient {}, amount {}", isInsuranceEnabled, isSalaryClient, amount);
        int totalAmount = 0;
        if (isInsuranceEnabled.equals(Boolean.TRUE) && isSalaryClient.equals(Boolean.TRUE)) {
            totalAmount += 50000;
            log.info("Добавляем к значению totalAmount 50000");
        } else if (isInsuranceEnabled.equals(Boolean.TRUE) && isSalaryClient.equals(Boolean.FALSE)) {
            totalAmount += 12500;
            log.info("Добавляем к значению totalAmount 12500");
        } else if (isInsuranceEnabled.equals(Boolean.FALSE) && isSalaryClient.equals(Boolean.TRUE)) {
            totalAmount += 25000;
            log.info("Добавляем к значению totalAmount 25000");
        }
        log.info("Значение totalAmount успешно рассчитано.");
        return BigDecimal.valueOf(totalAmount).add(amount);
    }

    private BigDecimal calculateRate(Boolean isInsuranceEnabled,
                                     Boolean isSalaryClient) throws IOException {
        log.info("Рассчитываем значение rate с входными данными: isInsuranceEnabled {}, " +
                "isSalaryClient {}", isInsuranceEnabled, isSalaryClient);
        double totalRate = Double.parseDouble(loadOriginalCreditRate());
        log.info("Выгружаем значение начальной кредитной ставки банка {}", totalRate);
        if (isInsuranceEnabled.equals(Boolean.TRUE) && isSalaryClient.equals(Boolean.TRUE)) {
            totalRate -= 3;
            log.info("Отнимаем от значения rate 3");
        } else if (isInsuranceEnabled.equals(Boolean.TRUE) && isSalaryClient.equals(Boolean.FALSE)) {
            totalRate -= 1;
            log.info("Отнимаем от значения rate 1");
        } else if (isInsuranceEnabled.equals(Boolean.FALSE) && isSalaryClient.equals(Boolean.TRUE)) {
            totalRate -= 2;
            log.info("Отнимаем от значения rate 2");
        }
        log.info("Значение rate успешно рассчитано.");
        return BigDecimal.valueOf(totalRate);
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal rate, Integer term, BigDecimal totalAmount) {
        MathContext mathContext = new MathContext(30);
        log.info("Рассчитываем значение monthlyPayment с входными данными: rate {}, " +
                "term {}, totalAmount {}", rate, term, totalAmount);
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 4, RoundingMode.CEILING);
        log.info("Рассчитываем значение monthlyRate {}", monthlyRate);
        BigDecimal monthlyRateInHundredths = monthlyRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING);
        log.info("Рассчитываем значение monthlyRateInHundredths {}", monthlyRateInHundredths);
        BigDecimal brackets = monthlyRateInHundredths.add(BigDecimal.valueOf(1));
        log.info("Рассчитываем значение brackets {}", brackets);
        BigDecimal bracketsToThePowerOfTerm = brackets.pow(term, mathContext);
        log.info("Рассчитываем значение bracketsToThePowerOfTerm {}", bracketsToThePowerOfTerm);
        BigDecimal numerator = monthlyRateInHundredths.multiply(bracketsToThePowerOfTerm);
        log.info("Рассчитываем значение numerator {}", numerator);
        BigDecimal denominator = bracketsToThePowerOfTerm.subtract(BigDecimal.valueOf(1));
        log.info("Рассчитываем значение denominator {}", denominator);
        BigDecimal annuityCoefficient = numerator.divide(denominator, RoundingMode.CEILING);
        log.info("Рассчитываем значение annuityCoefficient {}", annuityCoefficient);
        BigDecimal monthlyPayment = totalAmount.multiply(annuityCoefficient);
        log.info("Рассчитываем значение monthlyPayment {}", monthlyPayment);
        return monthlyPayment;
    }

    private String loadOriginalCreditRate() throws IOException {
        String rootPath = "src/main/resources";
        String applicationPropertiesPath = rootPath + "/application.properties";
        Properties properties = new Properties();
        properties.load(new FileInputStream(applicationPropertiesPath));
        return properties.getProperty("credit-rate");
    }
}
