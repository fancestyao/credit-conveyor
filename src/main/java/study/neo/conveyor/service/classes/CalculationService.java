package study.neo.conveyor.service.classes;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.neo.conveyor.configuration.OriginalCreditRatePropertiesConfiguration;
import study.neo.conveyor.dto.PaymentScheduleElement;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@Service
@Slf4j
public class CalculationService {
    private final OriginalCreditRatePropertiesConfiguration originalCreditRatePropertiesConfiguration;

    public List<PaymentScheduleElement> compileListOfPaymentScheduleElements(Integer term,
                                                                             BigDecimal monthlyPayment,
                                                                             BigDecimal amount,
                                                                             BigDecimal rate) {
        int number = 0;
        Calendar calendar;
        log.info("Начинаем компилировать лист расчетного плана с входными параметрами: term {}," +
                "monthlyPayment {}, amount {}, rate {}", term, monthlyPayment, amount, rate);
        List<PaymentScheduleElement> listOfPayments = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        for (int i = 1; i <= term; i++) {
            BigDecimal totalPayment = monthlyPayment.setScale(2, RoundingMode.UP);
            BigDecimal debtPayment = calculateDebtPayment(amount, rate, calendar);
            BigDecimal interestPayment = calculateInterestPayment(monthlyPayment, debtPayment);
            BigDecimal remainingDebt = calculateRemainingDebt(interestPayment, amount);
            if (i == term) {
                totalPayment = monthlyPayment.add(remainingDebt);
                interestPayment = amount;
                remainingDebt = BigDecimal.valueOf(0);
            }
            amount = amount.subtract(interestPayment);
            listOfPayments.add(PaymentScheduleElement.builder()
                    .number(number++)
                    .date(LocalDate.now().plusMonths(number))
                    .totalPayment(totalPayment)
                    .debtPayment(debtPayment)
                    .interestPayment(interestPayment)
                    .remainingDebt(remainingDebt)
                    .build());
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        return listOfPayments;
    }

    public BigDecimal calculateTotalAmount(Boolean isInsuranceEnabled,
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

    public BigDecimal calculateRate(Boolean isInsuranceEnabled,
                                    Boolean isSalaryClient) {
        log.info("Рассчитываем значение rate с входными данными: isInsuranceEnabled {}, " +
                "isSalaryClient {}", isInsuranceEnabled, isSalaryClient);
        Double totalRate = originalCreditRatePropertiesConfiguration.getOriginalCreditRate();
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

    /**
     *
     * Формула расчета ежемесячной выплаты: MP=AC*TA, где
     * MP — ежемесячная выплата; AC — коэффициент аннуитета; TA — полная сумма кредита.
     * Формула расчета коэффициента аннуитета: AC=(MR*(1+MR)^QP)/((1+MR)^QP-1), где
     * AC — коэффициент аннуитета; MR — месячная процентная ставка; QP — количество платежей.
     * @param rate указывает на рассчитанную кредитную ставку. Рассчитывается в следующем методе:
     * @see CalculationService#calculateRate(Boolean isInsuranceEnabled, Boolean isSalaryClient)
     * @param term указывает на предполагаемый период оплаты кредита
     * @param totalAmount указывает на желаемую сумму кредита. Рассчитывается в следующем методе:
     * @see CalculationService#calculateTotalAmount(Boolean isInsuranceEnabled, Boolean isSalaryClient, BigDecimal amount)
     * @return возвращает значение месячного платежа типа BigDecimal для аннуитетного типа оплаты кредита
     */
    public BigDecimal calculateMonthlyPayment(BigDecimal rate,
                                              Integer term,
                                              BigDecimal totalAmount) {
        MathContext mathContext = new MathContext(30);
        log.info("Рассчитываем значение monthlyPayment с входными данными: rate {}, " +
                "term {}, totalAmount {}", rate, term, totalAmount);
        BigDecimal monthlyRateInHundredths = rate.divide(BigDecimal.valueOf(12), 4, RoundingMode.CEILING)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING);
        log.info("Рассчитываем значение monthlyRateInHundredths {}", monthlyRateInHundredths);
        BigDecimal bracketsToThePowerOfTerm = monthlyRateInHundredths
                .add(BigDecimal.valueOf(1)).pow(term, mathContext);
        log.info("Рассчитываем значение bracketsToThePowerOfTerm {}", bracketsToThePowerOfTerm);
        BigDecimal annuityCoefficient = monthlyRateInHundredths.multiply(bracketsToThePowerOfTerm)
                .divide(bracketsToThePowerOfTerm.subtract(BigDecimal.valueOf(1)),
                RoundingMode.CEILING);
        log.info("Рассчитываем значение annuityCoefficient {}", annuityCoefficient);
        BigDecimal monthlyPayment = totalAmount.multiply(annuityCoefficient);
        log.info("Рассчитываем значение monthlyPayment {}", monthlyPayment);
        return monthlyPayment;
    }

    private BigDecimal calculateDebtPayment(BigDecimal amount,
                                                   BigDecimal rate, Calendar calendar) {
        ZoneId zone = ZoneId.systemDefault();
        Date date = calendar.getTime();
        int numberOfDaysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int numberOfDaysInCurrentYear;

        if (date.toInstant().atZone(zone).toLocalDate().isLeapYear()) {
            numberOfDaysInCurrentYear = 366;
        } else {
            numberOfDaysInCurrentYear = 365;
        }

        BigDecimal rateInHundredths = rate.divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING);
        BigDecimal multiplyAmountRate = amount.multiply(rateInHundredths);
        log.info("Умножаем остаток платежа amount {} на rate {}, ответ: {}", amount, rate, multiplyAmountRate);
        BigDecimal multiplyNumberOfDaysInMonth = multiplyAmountRate
                .multiply(BigDecimal.valueOf(numberOfDaysInCurrentMonth));
        log.info("Умножаем на количество дней в месяце: {}", multiplyNumberOfDaysInMonth);
        BigDecimal debtPayment = multiplyNumberOfDaysInMonth
                .divide(BigDecimal.valueOf(numberOfDaysInCurrentYear), RoundingMode.UP);
        log.info("Делим на количество дней в году: {}", debtPayment);
        return debtPayment.setScale(2, RoundingMode.UP);
    }

    private BigDecimal calculateInterestPayment(BigDecimal monthlyPayment,
                                                       BigDecimal debtPayment) {
        BigDecimal interestPayment = monthlyPayment.subtract(debtPayment);
        log.info("Значение interestPayment {} при monthlyPayment {} и debtPayment {}",
                interestPayment, monthlyPayment, debtPayment);
        return interestPayment.setScale(2, RoundingMode.UP);
    }

    private BigDecimal calculateRemainingDebt(BigDecimal interestPayment,
                                                     BigDecimal originalAmount) {
        BigDecimal remainingDebt = originalAmount.subtract(interestPayment);
        log.info("Значение remainingDebt {} при originalAmount {} и interestPayment {}", remainingDebt,
                originalAmount, interestPayment);
        return remainingDebt.setScale(2, RoundingMode.UP);
    }
}
