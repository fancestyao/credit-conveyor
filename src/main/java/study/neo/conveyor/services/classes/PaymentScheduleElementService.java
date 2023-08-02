package study.neo.conveyor.services.classes;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.neo.conveyor.dtos.PaymentScheduleElement;

import java.math.BigDecimal;
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
public class PaymentScheduleElementService {
    private static Integer number = 0;
    private static Calendar calendar;

    public List<PaymentScheduleElement> compileListOfPaymentScheduleElements(Integer term,
                                                                             BigDecimal monthlyPayment,
                                                                             BigDecimal amount,
                                                                             BigDecimal rate) {
        log.info("Начинаем компилировать лист расчетного плана с входными параметрами: term {}," +
                "monthlyPayment {}, amount {}, rate {}", term, monthlyPayment, amount, rate);
        List<PaymentScheduleElement> listOfPayments = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        for (int i = 1; i <= term; i++) {
            System.out.println(calendar);
            BigDecimal totalPayment = monthlyPayment.setScale(2, RoundingMode.UP);
            BigDecimal debtPayment = calculateDebtPayment(amount, rate);
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

    private static BigDecimal calculateDebtPayment(BigDecimal amount,
                                                   BigDecimal rate) {
        ZoneId zone = ZoneId.systemDefault();
        Date date = calendar.getTime();
        int numberOfDaysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("numberOfDays: " + numberOfDaysInCurrentMonth);
        int numberOfDaysInCurrentYear;

        if (date.toInstant().atZone(zone).toLocalDate().isLeapYear()) {
            numberOfDaysInCurrentYear = 366;
        } else {
            numberOfDaysInCurrentYear = 365;
        }

        BigDecimal rateInHundredths = rate.divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING);
        System.out.println("rateInHundredths: " + rateInHundredths);
        BigDecimal multiplyAmountRate = amount.multiply(rateInHundredths);
        System.out.println("multiplyAmountRate: " + multiplyAmountRate);
        log.info("Умножаем остаток платежа amount {} на rate {}, ответ: {}", amount, rate, multiplyAmountRate);
        BigDecimal multiplyNumberOfDaysInMonth = multiplyAmountRate
                .multiply(BigDecimal.valueOf(numberOfDaysInCurrentMonth));
        System.out.println("multiplyNumberOfDaysInMonth: " + multiplyNumberOfDaysInMonth);
        log.info("Умножаем на количество дней в месяце: {}", multiplyNumberOfDaysInMonth);
        BigDecimal debtPayment = multiplyNumberOfDaysInMonth
                .divide(BigDecimal.valueOf(numberOfDaysInCurrentYear), RoundingMode.UP);
        System.out.println("debtPayment: " + debtPayment);
        log.info("Делим на количество дней в году: {}", debtPayment);
        return debtPayment.setScale(2, RoundingMode.UP);
    }

    private static BigDecimal calculateInterestPayment(BigDecimal monthlyPayment,
                                                       BigDecimal debtPayment) {
        BigDecimal interestPayment = monthlyPayment.subtract(debtPayment);
        log.info("Значение interestPayment {} при monthlyPayment {} и debtPayment {}",
                interestPayment, monthlyPayment, debtPayment);
        return interestPayment.setScale(2, RoundingMode.UP);
    }

    private static BigDecimal calculateRemainingDebt(BigDecimal interestPayment, BigDecimal originalAmount) {
        BigDecimal remainingDebt = originalAmount.subtract(interestPayment);
        log.info("Значение remainingDebt {} при originalAmount {} и interestPayment {}", remainingDebt,
                originalAmount, interestPayment);
        return remainingDebt.setScale(2, RoundingMode.UP);
    }
}
