package study.neo.conveyor.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.neo.conveyor.dtos.PaymentScheduleElement;
import study.neo.conveyor.services.classes.PaymentScheduleElementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Тест сервиса PaymentScheduleElementService.")
public class PaymentScheduleElementServiceTest {
    private PaymentScheduleElementService paymentScheduleElementService;

    @BeforeEach
    void setup() {
        paymentScheduleElementService = new PaymentScheduleElementService();
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
        List<PaymentScheduleElement> result = paymentScheduleElementService
                .compileListOfPaymentScheduleElements(term, monthlyPayment, amount, rate);
        assertEquals(expectedPaymentElementFirst.getNumber(), result.get(0).getNumber());
        assertEquals(expectedPaymentElementFirst.getTotalPayment(), result.get(0).getTotalPayment());
        assertEquals(expectedPaymentElementFirst.getDate(), result.get(0).getDate());
        assertEquals(term, result.size());
    }
}
