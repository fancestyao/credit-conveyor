package study.neo.conveyor.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "loan-application")
@Data
public class LoanApplicationPropertiesConfiguration {
    private final Integer nameMinSymbolsRestriction;
    private final Integer nameMaxSymbolsRestriction;
    private final BigDecimal creditAmountRestriction;
    private final String latinSymbolsRegexRestriction;
    private final Integer creditTermRestriction;
    private final Integer ageOfAdulthood;
    private final String emailRegexRestriction;
    private final Integer passportSeriesLengthRestriction;
    private final Integer passportNumberLengthRestriction;
}
