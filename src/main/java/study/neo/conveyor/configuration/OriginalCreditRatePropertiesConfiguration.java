package study.neo.conveyor.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "credit-rate")
@Data
public class OriginalCreditRatePropertiesConfiguration {
    private final Double originalCreditRate;
}
