package study.neo.conveyor.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "score-credit-rate")
@Data
public class ScoreCreditRatePropertiesConfiguration {
    private final Integer bottomLineOfFemaleGoldenWorkingAge;
    private final Integer topLineOfFemaleGoldenWorkingAge;
    private final Integer bottomLineOfMaleGoldenWorkingAge;
    private final Integer topLineOfMaleGoldenWorkingAge;
}
