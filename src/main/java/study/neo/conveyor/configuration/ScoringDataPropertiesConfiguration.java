package study.neo.conveyor.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scoring-data")
@Data
public class ScoringDataPropertiesConfiguration {
    private final Integer minAgeToLoan;
    private final Integer maxAgeToLoan;
    private final Integer minMonthsOfOverallEmployment;
    private final Integer maxAmountOfSalariesToLoan;
    private final Integer minMonthsOfCurrentEmployment;
}
