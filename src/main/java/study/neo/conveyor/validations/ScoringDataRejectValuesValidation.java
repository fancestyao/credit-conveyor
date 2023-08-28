package study.neo.conveyor.validations;

import lombok.Data;
import org.springframework.stereotype.Component;
import study.neo.conveyor.dtos.ScoringDataDTO;
import study.neo.conveyor.enums.EmploymentStatus;
import study.neo.conveyor.exceptions.BirthDateException;
import study.neo.conveyor.exceptions.CreditAmountException;
import study.neo.conveyor.exceptions.UnemployedException;
import study.neo.conveyor.exceptions.WorkExperienceException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static study.neo.conveyor.enums.EmploymentStatus.UNEMPLOYED;

@Data
@Component
public class ScoringDataRejectValuesValidation {
    private static final Integer MIN_AGE_TO_LOAN = 20;
    private static final Integer MAX_AGE_TO_LOAN = 60;
    private static final Integer MIN_MONTHS_OF_OVERALL_EMPLOYMENT = 12;
    private static final Integer MAX_AMOUNT_OF_SALARIES_TO_LOAN = 20;
    private static final Integer MIN_MONTHS_OF_CURRENT_EMPLOYMENT = 3;

    public void callAllValidations(ScoringDataDTO scoringDataDTO) {
        unemployedValidation(scoringDataDTO.getEmploymentDTO().getEmploymentStatus());
        creditAmountValidation(scoringDataDTO.getAmount(), scoringDataDTO.getEmploymentDTO().getSalary());
        birthDateValidation(scoringDataDTO.getBirthDate());
        totalWorkExperienceValidation(scoringDataDTO.getEmploymentDTO().getWorkExperienceTotal());
        currentWorkExperienceValidation(scoringDataDTO.getEmploymentDTO().getWorkExperienceCurrent());
    }

    private void unemployedValidation(EmploymentStatus status) {
        if (status.equals(UNEMPLOYED)) {
            throw new UnemployedException("Необходимо иметь стабильный источник дохода.");
        }
    }

    private void creditAmountValidation(BigDecimal amount, BigDecimal salary) {
        System.out.println(amount.divide(salary, RoundingMode.HALF_EVEN));
        if (amount.divide(salary, RoundingMode.HALF_EVEN)
                .compareTo(BigDecimal.valueOf(MAX_AMOUNT_OF_SALARIES_TO_LOAN)) > 0) {
            throw new CreditAmountException("Сумма займа должна быть больше, чем "
                    + MAX_AMOUNT_OF_SALARIES_TO_LOAN + " зарплат.");
        }
    }

    private void birthDateValidation(LocalDate birthDate) {
        if (LocalDate.now().compareTo(birthDate) < MIN_AGE_TO_LOAN
                || LocalDate.now().compareTo(birthDate) > MAX_AGE_TO_LOAN) {
            throw new BirthDateException("Возраст должен быть от "
                    + MIN_AGE_TO_LOAN + " до " + MAX_AGE_TO_LOAN + " лет.");
        }
    }

    private void totalWorkExperienceValidation(Integer totalWorkExperience) {
        if (totalWorkExperience < MIN_MONTHS_OF_OVERALL_EMPLOYMENT) {
            throw new WorkExperienceException("Общий стаж трудоустройства должен быть более "
                    + MIN_MONTHS_OF_OVERALL_EMPLOYMENT + " месяцев.");
        }
    }

    private void currentWorkExperienceValidation(Integer currentWorkExperience) {
        if (currentWorkExperience < MIN_MONTHS_OF_CURRENT_EMPLOYMENT) {
            throw new WorkExperienceException("Текущий стаж трудоустройства должен быть более "
                    + MIN_MONTHS_OF_CURRENT_EMPLOYMENT + " месяцев.");
        }
    }
}
