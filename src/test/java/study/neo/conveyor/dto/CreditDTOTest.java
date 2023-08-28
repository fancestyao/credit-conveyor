package study.neo.conveyor.dto;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@DisplayName("Тест CreditDTO.")
public class CreditDTOTest {
    @Autowired
    private JacksonTester<CreditDTO> jacksonTester;

    @Test
    @DisplayName("Сериализация CreditDTOTest.")
    void testSerialize() throws IOException {
        CreditDTO creditDTO = CreditDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.FALSE)
                .monthlyPayment(BigDecimal.valueOf(3000))
                .rate(BigDecimal.valueOf(6.5))
                .psk(BigDecimal.valueOf(5.5))
                .paymentSchedule(List.of(PaymentScheduleElement.builder()
                                .date(LocalDate.of(1999, 5, 18))
                                .number(1)
                                .remainingDebt(BigDecimal.valueOf(100000))
                                .interestPayment(BigDecimal.valueOf(3500))
                                .debtPayment(BigDecimal.valueOf(50000))
                                .totalPayment(BigDecimal.valueOf(15000))
                        .build()))
                .build();
        JsonContent<CreditDTO> result = jacksonTester.write(creditDTO);
        assertThat(result).extractingJsonPathNumberValue("$.amount").isEqualTo(100000);
        assertThat(result).extractingJsonPathNumberValue("$.term").isEqualTo(24);
        assertThat(result).extractingJsonPathBooleanValue("$.isInsuranceEnabled").isEqualTo(true);
        assertThat(result).extractingJsonPathBooleanValue("$.isSalaryClient").isEqualTo(false);
        assertThat(result).extractingJsonPathNumberValue("$.monthlyPayment").isEqualTo(3000);
        assertThat(result).extractingJsonPathNumberValue("$.rate").isEqualTo(6.5);
        assertThat(result).extractingJsonPathNumberValue("$.psk").isEqualTo(5.5);
        assertThat(result).extractingJsonPathArrayValue("$.paymentSchedule").isEqualTo(List.of(Map.of(
                "date", "1999-05-18",
                "number", 1,
                "remainingDebt", 100000,
                "interestPayment", 3500,
                "debtPayment", 50000,
                "totalPayment", 15000
                )));
    }

    @Test
    @DisplayName("Десериализация CreditDTOTest.")
    void testDeserialize() throws IOException {
        String jsonString = "{\"amount\": \"100000\"," +
                " \"term\": \"24\"," +
                " \"isInsuranceEnabled\": \"true\"," +
                " \"isSalaryClient\": false," +
                " \"monthlyPayment\": 3000," +
                " \"rate\": \"6.5\"," +
                " \"psk\": \"5.5\"," +
                " \"paymentSchedule\": [{" +
                " \"date\": \"1999-05-18\"," +
                " \"number\": \"1\"," +
                " \"remainingDebt\": \"100000\"," +
                " \"interestPayment\": \"3500\"," +
                " \"debtPayment\": \"50000\"," +
                " \"totalPayment\": \"15000\"}]}";
        CreditDTO creditDTO = jacksonTester.parseObject(jsonString);
        AssertionsForClassTypes.assertThat(creditDTO.getAmount()).isEqualTo(BigDecimal.valueOf(100000));
        AssertionsForClassTypes.assertThat(creditDTO.getTerm()).isEqualTo(24);
        AssertionsForClassTypes.assertThat(creditDTO.getIsInsuranceEnabled()).isEqualTo(true);
        AssertionsForClassTypes.assertThat(creditDTO.getIsSalaryClient()).isEqualTo(false);
        AssertionsForClassTypes.assertThat(creditDTO.getMonthlyPayment()).isEqualTo(BigDecimal.valueOf(3000));
        AssertionsForClassTypes.assertThat(creditDTO.getRate()).isEqualTo(BigDecimal.valueOf(6.5));
        AssertionsForClassTypes.assertThat(creditDTO.getPsk()).isEqualTo(BigDecimal.valueOf(5.5));
        AssertionsForClassTypes.assertThat(creditDTO.getPaymentSchedule()).isEqualTo(List.of(PaymentScheduleElement.builder()
                .date(LocalDate.of(1999, 5, 18))
                .number(1)
                .remainingDebt(BigDecimal.valueOf(100000))
                .interestPayment(BigDecimal.valueOf(3500))
                .debtPayment(BigDecimal.valueOf(50000))
                .totalPayment(BigDecimal.valueOf(15000))
                .build()));
    }
}
