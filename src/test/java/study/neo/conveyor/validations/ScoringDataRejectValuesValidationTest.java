package study.neo.conveyor.validations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.neo.conveyor.dtos.EmploymentDTO;
import study.neo.conveyor.dtos.ScoringDataDTO;
import study.neo.conveyor.enums.EmploymentPosition;
import study.neo.conveyor.enums.EmploymentStatus;
import study.neo.conveyor.enums.Gender;
import study.neo.conveyor.enums.MaritalStatus;
import study.neo.conveyor.exceptions.BirthDateException;
import study.neo.conveyor.exceptions.CreditAmountException;
import study.neo.conveyor.exceptions.UnemployedException;
import study.neo.conveyor.exceptions.WorkExperienceException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Тест компонента валидации ScoringDataRejectValuesValidation.")
public class ScoringDataRejectValuesValidationTest {
    private ScoringDataRejectValuesValidation scoringDataRejectValuesValidation;

    @Test
    @DisplayName("Нетрудоустроенный.")
    void unemployedValidationTest() {
        scoringDataRejectValuesValidation = new ScoringDataRejectValuesValidation();
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1999, 5, 18))
                .passportSeries("1234")
                .passportNumber("123456")
                .passportIssueDate(LocalDate.of(2019, 5, 18))
                .passportIssueBranch("Saratov")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .employmentDTO(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.UNEMPLOYED)
                        .employmentINN("123412341234 123")
                        .salary(BigDecimal.valueOf(50000))
                        .workExperienceTotal(15)
                        .workExperienceCurrent(5)
                        .employmentPosition(EmploymentPosition.TOP_CLASS_MANAGER)
                        .build())
                .account("1234567890")
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.FALSE)
                .build();
        assertThrows(UnemployedException.class,
                () -> scoringDataRejectValuesValidation.callAllValidations(scoringDataDTO));
    }

    @Test
    @DisplayName("Сумма запрашиваемой суммы кредита больше 20 ЗП.")
    void creditAmountLessThanTwentySalariesValidationTest() {
        scoringDataRejectValuesValidation = new ScoringDataRejectValuesValidation();
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1999, 5, 18))
                .passportSeries("1234")
                .passportNumber("123456")
                .passportIssueDate(LocalDate.of(2019, 5, 18))
                .passportIssueBranch("Saratov")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .employmentDTO(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .employmentINN("123412341234 123")
                        .salary(BigDecimal.valueOf(4000))
                        .workExperienceTotal(15)
                        .workExperienceCurrent(5)
                        .employmentPosition(EmploymentPosition.TOP_CLASS_MANAGER)
                        .build())
                .account("1234567890")
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.FALSE)
                .build();
        assertThrows(CreditAmountException.class,
                () -> scoringDataRejectValuesValidation.callAllValidations(scoringDataDTO));
    }

    @Test
    @DisplayName("Возраст меньше двадцати лет.")
    void lessThanTwentyYearsOldBirthDateValidationTest() {
        scoringDataRejectValuesValidation = new ScoringDataRejectValuesValidation();
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(2010, 5, 18))
                .passportSeries("1234")
                .passportNumber("123456")
                .passportIssueDate(LocalDate.of(2019, 5, 18))
                .passportIssueBranch("Saratov")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .employmentDTO(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .employmentINN("123412341234 123")
                        .salary(BigDecimal.valueOf(50000))
                        .workExperienceTotal(15)
                        .workExperienceCurrent(5)
                        .employmentPosition(EmploymentPosition.TOP_CLASS_MANAGER)
                        .build())
                .account("1234567890")
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.FALSE)
                .build();
        assertThrows(BirthDateException.class,
                () -> scoringDataRejectValuesValidation.callAllValidations(scoringDataDTO));
    }

    @Test
    @DisplayName("Опыт работы на текущем месте меньше 3 месяцев.")
    void lessThanThreeMonthOfCurrentWorkExperienceValidationTest() {
        scoringDataRejectValuesValidation = new ScoringDataRejectValuesValidation();
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1999, 5, 18))
                .passportSeries("1234")
                .passportNumber("123456")
                .passportIssueDate(LocalDate.of(2019, 5, 18))
                .passportIssueBranch("Saratov")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .employmentDTO(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .employmentINN("123412341234 123")
                        .salary(BigDecimal.valueOf(50000))
                        .workExperienceTotal(15)
                        .workExperienceCurrent(2)
                        .employmentPosition(EmploymentPosition.TOP_CLASS_MANAGER)
                        .build())
                .account("1234567890")
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.FALSE)
                .build();
        assertThrows(WorkExperienceException.class,
                () -> scoringDataRejectValuesValidation.callAllValidations(scoringDataDTO));
    }

    @Test
    @DisplayName("Опыт работы в общем меньше 12 месяцев.")
    void lessThanTwelveMonthsTotalWorkExperienceValidationTest() {
        scoringDataRejectValuesValidation = new ScoringDataRejectValuesValidation();
        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1999, 5, 18))
                .passportSeries("1234")
                .passportNumber("123456")
                .passportIssueDate(LocalDate.of(2019, 5, 18))
                .passportIssueBranch("Saratov")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .employmentDTO(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .employmentINN("123412341234 123")
                        .salary(BigDecimal.valueOf(50000))
                        .workExperienceTotal(11)
                        .workExperienceCurrent(5)
                        .employmentPosition(EmploymentPosition.TOP_CLASS_MANAGER)
                        .build())
                .account("1234567890")
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.FALSE)
                .build();
        assertThrows(WorkExperienceException.class,
                () -> scoringDataRejectValuesValidation.callAllValidations(scoringDataDTO));
    }
}
