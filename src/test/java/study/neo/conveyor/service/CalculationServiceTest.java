package study.neo.conveyor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.neo.conveyor.configuration.OriginalCreditRatePropertiesConfiguration;
import study.neo.conveyor.dto.PaymentScheduleElement;
import study.neo.conveyor.service.classes.CalculationService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Тест сервиса CalculationService.")
public class CalculationServiceTest {
    private CalculationService calculationService;

    @BeforeEach
    void setup() {
        Double originalCreditRate = 17.0;
        OriginalCreditRatePropertiesConfiguration originalCreditRatePropertiesConfiguration
                = new OriginalCreditRatePropertiesConfiguration(originalCreditRate);
        calculationService = new CalculationService(originalCreditRatePropertiesConfiguration);
    }

    @Test
    @DisplayName("Тестирование метода compileListOfPaymentScheduleElements.")
    void compileListOfPaymentScheduleElementsTest() {
        Integer term = 18;
        BigDecimal monthlyPayment = BigDecimal.valueOf(18715.44);
        BigDecimal amount = BigDecimal.valueOf(300000);
        BigDecimal rate = BigDecimal.valueOf(15);
        PaymentScheduleElement expectedPaymentElementFirst = PaymentScheduleElement
                .builder()
                .number(0)
                .totalPayment(monthlyPayment)
                .remainingDebt(amount.subtract(monthlyPayment))
                .date(LocalDate.now().plusMonths(1))
                .build();
        List<PaymentScheduleElement> result = calculationService
                .compileListOfPaymentScheduleElements(term, monthlyPayment, amount, rate);
        assertEquals(expectedPaymentElementFirst.getNumber(), result.get(0).getNumber());
        assertEquals(expectedPaymentElementFirst.getTotalPayment(), result.get(0).getTotalPayment());
        assertEquals(expectedPaymentElementFirst.getDate(), result.get(0).getDate());
        assertEquals(term, result.size());
    }
}
