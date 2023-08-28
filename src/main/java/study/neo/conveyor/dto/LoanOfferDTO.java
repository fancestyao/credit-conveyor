package study.neo.conveyor.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanOfferDTO {
    private Long applicationId;
    private BigDecimal requestedAmount;
    private Integer term;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
    private BigDecimal totalAmount;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
}
