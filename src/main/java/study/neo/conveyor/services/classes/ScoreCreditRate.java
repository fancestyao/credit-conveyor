package study.neo.conveyor.services.classes;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.neo.conveyor.configuration.OriginalCreditRatePropertiesConfiguration;
import study.neo.conveyor.configuration.ScoreCreditRatePropertiesConfiguration;
import study.neo.conveyor.dtos.ScoringDataDTO;
import study.neo.conveyor.enums.EmploymentPosition;
import study.neo.conveyor.enums.EmploymentStatus;
import study.neo.conveyor.enums.Gender;
import study.neo.conveyor.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Data
@Slf4j
public class ScoreCreditRate {
    private final ScoreCreditRatePropertiesConfiguration scoreCreditRatePropertiesConfiguration;
    private final OriginalCreditRatePropertiesConfiguration originalCreditRatePropertiesConfiguration;
    private Double originalCreditRate;

    public BigDecimal scoreData(ScoringDataDTO scoringDataDTO) {
        originalCreditRate = originalCreditRatePropertiesConfiguration.getOriginalCreditRate();
        log.info("Выгружаем начальное значение кредитной ставки: {}", originalCreditRate);
        scoreCreditRateDependingOnEmploymentStatus(scoringDataDTO.getEmploymentDTO().getEmploymentStatus());
        log.info("Значение кредитной ставки {} при статусе трудоустройства: {}", originalCreditRate,
                scoringDataDTO.getEmploymentDTO().getEmploymentStatus());
        scoreCreditRateDependingOnEmploymentPosition(scoringDataDTO.getEmploymentDTO().getEmploymentPosition());
        log.info("Значение кредитной ставки {} при позиции трудоустройства: {}", originalCreditRate,
                scoringDataDTO.getEmploymentDTO().getEmploymentPosition());
        scoreCreditRateDependingOnMaritalStatus(scoringDataDTO.getMaritalStatus());
        log.info("Значение кредитной ставки {} при семейном положении: {}", originalCreditRate,
                scoringDataDTO.getMaritalStatus());
        scoreCreditRateDependingOnDependentAmount(scoringDataDTO.getDependentAmount());
        log.info("Значение кредитной ставки {} при количестве иждивенцев: {}", originalCreditRate,
                scoringDataDTO.getDependentAmount());
        scoreCreditRateDependingOnGenderAndBirthDate(scoringDataDTO.getGender(), scoringDataDTO.getBirthDate());
        log.info("Значение кредитной ставки {} при гендере и дате рождения: {}, {}", originalCreditRate,
                scoringDataDTO.getGender(), scoringDataDTO.getBirthDate());
        return BigDecimal.valueOf(originalCreditRate);
    }

    private void scoreCreditRateDependingOnEmploymentStatus(EmploymentStatus employmentStatus) {
        log.info("Расчет кредитной ставки при статусе трудоустройства: {}", employmentStatus);
        switch (employmentStatus) {
            case SELF_EMPLOYED -> originalCreditRate += 1;
            case BUSINESS_OWNER -> originalCreditRate += 3;
        }
    }

    private void scoreCreditRateDependingOnEmploymentPosition(EmploymentPosition employmentPosition) {
        log.info("Расчет кредитной ставки при позиции трудоустройства: {}", employmentPosition);
        switch (employmentPosition) {
            case MID_MANAGER -> originalCreditRate -= 2;
            case TOP_MANAGER -> originalCreditRate -= 4;
        }
    }

    private void scoreCreditRateDependingOnMaritalStatus(MaritalStatus maritalStatus) {
        log.info("Расчет кредитной ставки при семейном положении: {}", maritalStatus);
        switch (maritalStatus) {
            case SINGLE -> originalCreditRate -= 3;
            case MARRIED -> originalCreditRate -= 1;
        }
    }

    private void scoreCreditRateDependingOnDependentAmount(Integer dependentAmount) {
        log.info("Расчет кредитной ставки при количестве иждивенцев: {}", originalCreditRate);
        if (dependentAmount > 1)
            originalCreditRate += 1;
    }

    private void scoreCreditRateDependingOnGenderAndBirthDate(Gender gender, LocalDate birthDate) {
        log.info("Расчет кредитной ставки при гендере и дате рождения: {}, {}", gender, birthDate);
        if (gender.equals(Gender.MALE)
                && ((LocalDate.now().compareTo(birthDate) >=
                scoreCreditRatePropertiesConfiguration.getBottomLineOfMaleGoldenWorkingAge())
                && ((LocalDate.now().compareTo(birthDate) <=
                scoreCreditRatePropertiesConfiguration.getTopLineOfMaleGoldenWorkingAge())))) {
            originalCreditRate -= 3;
        } else if (gender.equals(Gender.FEMALE)
                && ((LocalDate.now().compareTo(birthDate) >=
                scoreCreditRatePropertiesConfiguration.getBottomLineOfFemaleGoldenWorkingAge())
                && ((LocalDate.now().compareTo(birthDate) <=
                scoreCreditRatePropertiesConfiguration.getTopLineOfFemaleGoldenWorkingAge())))) {
            originalCreditRate -= 3;
        } else if (gender.equals(Gender.NON_BINARY)) {
            originalCreditRate += 3;
        }
    }
}
