package study.neo.conveyor.services.classes;

import org.springframework.stereotype.Service;
import study.neo.conveyor.dtos.ScoringDataDTO;
import study.neo.conveyor.enums.EmploymentPosition;
import study.neo.conveyor.enums.EmploymentStatus;
import study.neo.conveyor.enums.Gender;
import study.neo.conveyor.enums.MaritalStatus;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Properties;

@Service
public class ScoreCreditRate {
    private static final Integer BOTTOM_LINE_OF_FEMALE_GOLDEN_WORKING_AGE = 30;
    private static final Integer TOP_LINE_OF_FEMALE_GOLDEN_WORKING_AGE = 55;
    private static final Integer BOTTOM_LINE_OF_MALE_GOLDEN_WORKING_AGE = 35;
    private static final Integer TOP_LINE_OF_MALE_FEMALE_WORKING_AGE = 60;
    private Double originalCreditRate;

    public BigDecimal scoreData(ScoringDataDTO scoringDataDTO) throws IOException {
        originalCreditRate = Double.valueOf(loadOriginalCreditRate());
        scoreCreditRateDependingOnEmploymentStatus(scoringDataDTO.getEmploymentDTO().getEmploymentStatus());
        scoreCreditRateDependingOnEmploymentPosition(scoringDataDTO.getEmploymentDTO().getEmploymentPosition());
        scoreCreditRateDependingOnMaritalStatus(scoringDataDTO.getMaritalStatus());
        scoreCreditRateDependingOnDependentAmount(scoringDataDTO.getDependentAmount());
        scoreCreditRateDependingOnGenderAndBirthDate(scoringDataDTO.getGender(), scoringDataDTO.getBirthDate());
        return BigDecimal.valueOf(originalCreditRate);
    }

    private String loadOriginalCreditRate() throws IOException {
        String rootPath = "src/main/resources";
        String applicationPropertiesPath = rootPath + "/application.properties";
        Properties properties = new Properties();
        properties.load(new FileInputStream(applicationPropertiesPath));
        return properties.getProperty("credit-rate");
    }

    private void scoreCreditRateDependingOnEmploymentStatus(EmploymentStatus employmentStatus) {
        switch (employmentStatus) {
            case SELF_EMPLOYED -> originalCreditRate += 1;
            case BUSINESS_OWNER -> originalCreditRate += 3;
        }
    }

    private void scoreCreditRateDependingOnEmploymentPosition(EmploymentPosition employmentPosition) {
        switch (employmentPosition) {
            case MIDDLE_CLASS_MANAGER -> originalCreditRate -= 2;
            case TOP_CLASS_MANAGER -> originalCreditRate -= 4;
        }
    }

    private void scoreCreditRateDependingOnMaritalStatus(MaritalStatus maritalStatus) {
        switch (maritalStatus) {
            case SINGLE -> originalCreditRate -= 3;
            case MARRIED -> originalCreditRate -= 1;
        }
    }

    private void scoreCreditRateDependingOnDependentAmount(Integer dependentAmount) {
        if (dependentAmount > 1)
            originalCreditRate += 1;
    }

    private void scoreCreditRateDependingOnGenderAndBirthDate(Gender gender, LocalDate birthDate) {
        if (gender.equals(Gender.MALE)
                && ((LocalDate.now().compareTo(birthDate) >= BOTTOM_LINE_OF_MALE_GOLDEN_WORKING_AGE)
                && ((LocalDate.now().compareTo(birthDate) <= TOP_LINE_OF_MALE_FEMALE_WORKING_AGE)))) {
            originalCreditRate -= 3;
        } else if (gender.equals(Gender.FEMALE)
                && ((LocalDate.now().compareTo(birthDate) >= BOTTOM_LINE_OF_FEMALE_GOLDEN_WORKING_AGE)
                && ((LocalDate.now().compareTo(birthDate) <= TOP_LINE_OF_FEMALE_GOLDEN_WORKING_AGE)))) {
            originalCreditRate -= 3;
        } else if (gender.equals(Gender.NON_BINARY)) {
            originalCreditRate += 3;
        }
    }
}
