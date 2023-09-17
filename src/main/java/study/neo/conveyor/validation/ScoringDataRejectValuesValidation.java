package study.neo.conveyor.validation;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import study.neo.conveyor.configuration.ScoringDataPropertiesConfiguration;
import study.neo.conveyor.dto.ScoringDataDTO;
import study.neo.conveyor.enums.EmploymentStatus;
import study.neo.conveyor.exception.BirthDateException;
import study.neo.conveyor.exception.CreditAmountException;
import study.neo.conveyor.exception.UnemployedException;
import study.neo.conveyor.exception.WorkExperienceException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static study.neo.conveyor.enums.EmploymentStatus.UNEMPLOYED;

@Data
@Component
@Slf4j
public class ScoringDataRejectValuesValidation {
    private final ScoringDataPropertiesConfiguration scoringDataPropertiesConfiguration;

    public void callAllValidations(ScoringDataDTO scoringDataDTO) {
        unemployedValidation(scoringDataDTO.getEmploymentDTO().getEmploymentStatus());
        creditAmountValidation(scoringDataDTO.getAmount(), scoringDataDTO.getEmploymentDTO().getSalary());
        birthDateValidation(scoringDataDTO.getBirthDate());
        totalWorkExperienceValidation(scoringDataDTO.getEmploymentDTO().getWorkExperienceTotal());
        currentWorkExperienceValidation(scoringDataDTO.getEmploymentDTO().getWorkExperienceCurrent());
    }

    private void unemployedValidation(EmploymentStatus status) {
        if (status.equals(UNEMPLOYED)) {
            log.warn("Необходимо иметь стабильный источник дохода.");
            throw new UnemployedException("Необходимо иметь стабильный источник дохода.");
        }
    }

    private void creditAmountValidation(BigDecimal amount, BigDecimal salary) {
        if (amount.divide(salary, RoundingMode.HALF_EVEN)
                .compareTo(BigDecimal.valueOf(scoringDataPropertiesConfiguration.getMaxAmountOfSalariesToLoan())) > 0) {
            log.warn("Сумма займа должна быть меньше, чем "
                    + scoringDataPropertiesConfiguration.getMaxAmountOfSalariesToLoan() + " зарплат.");
            throw new CreditAmountException("Сумма займа должна быть меньше, чем "
                    + scoringDataPropertiesConfiguration.getMaxAmountOfSalariesToLoan() + " зарплат.");
        }
    }

    private void birthDateValidation(LocalDate birthDate) {
        if (LocalDate.now().compareTo(birthDate) < scoringDataPropertiesConfiguration.getMinAgeToLoan()
                || LocalDate.now().compareTo(birthDate) > scoringDataPropertiesConfiguration.getMaxAgeToLoan()) {
            log.warn("Возраст должен быть от "
                    + scoringDataPropertiesConfiguration.getMinAgeToLoan() + " до "
                    + scoringDataPropertiesConfiguration.getMaxAgeToLoan() + " лет.");
            throw new BirthDateException("Возраст должен быть от "
                    + scoringDataPropertiesConfiguration.getMinAgeToLoan() + " до "
                    + scoringDataPropertiesConfiguration.getMaxAgeToLoan() + " лет.");
        }
    }

    private void totalWorkExperienceValidation(Integer totalWorkExperience) {
        if (totalWorkExperience < scoringDataPropertiesConfiguration.getMinMonthsOfOverallEmployment()) {
            log.warn("Общий стаж трудоустройства должен быть более "
                    + scoringDataPropertiesConfiguration.getMinMonthsOfOverallEmployment() + " месяцев.");
            throw new WorkExperienceException("Общий стаж трудоустройства должен быть более "
                    + scoringDataPropertiesConfiguration.getMinMonthsOfOverallEmployment() + " месяцев.");
        }
    }

    private void currentWorkExperienceValidation(Integer currentWorkExperience) {
        if (currentWorkExperience < scoringDataPropertiesConfiguration.getMinMonthsOfCurrentEmployment()) {
            log.warn("Текущий стаж трудоустройства должен быть более "
                    + scoringDataPropertiesConfiguration.getMinMonthsOfCurrentEmployment() + " месяцев.");
            throw new WorkExperienceException("Текущий стаж трудоустройства должен быть более "
                    + scoringDataPropertiesConfiguration.getMinMonthsOfCurrentEmployment() + " месяцев.");
        }
    }
}
