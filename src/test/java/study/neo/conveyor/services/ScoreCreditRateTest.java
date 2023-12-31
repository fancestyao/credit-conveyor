package study.neo.conveyor.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.neo.conveyor.dtos.EmploymentDTO;
import study.neo.conveyor.dtos.ScoringDataDTO;
import study.neo.conveyor.enums.EmploymentPosition;
import study.neo.conveyor.enums.EmploymentStatus;
import study.neo.conveyor.enums.Gender;
import study.neo.conveyor.enums.MaritalStatus;
import study.neo.conveyor.services.classes.ScoreCreditRate;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Тест сервиса ScoreCreditRate.")
public class ScoreCreditRateTest {
    private ScoringDataDTO scoringDataDTO;
    private ScoreCreditRate scoreCreditRate;

    @BeforeEach
    void setup() {
        scoreCreditRate = new ScoreCreditRate();
    }

    @Test
    @DisplayName("Тестирование метода scoreData: самоустроенный мужчина топ-менеджер.")
    public void testScoreDataMaleSelfEmployedTopManager() throws IOException {
        scoringDataDTO = ScoringDataDTO.builder()
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
                        .workExperienceCurrent(5)
                        .employmentPosition(EmploymentPosition.TOP_CLASS_MANAGER)
                        .build())
                .account("1234567890")
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.FALSE)
                .build();
        double expectedCreditRate = 11.0;
        BigDecimal result = scoreCreditRate.scoreData(scoringDataDTO);
        assertEquals(BigDecimal.valueOf(expectedCreditRate), result);
    }

    @Test
    @DisplayName("Тестирование метода scoreData: владелица бизнеса девушка менеджер среднего класса.")
    public void testScoreDataFemaleBusinessOwnerMiddleClassManager() throws IOException {
        scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.of(1979, 5, 18))
                .passportSeries("1234")
                .passportNumber("123456")
                .passportIssueDate(LocalDate.of(2019, 5, 18))
                .passportIssueBranch("Saratov")
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(2)
                .employmentDTO(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                        .employmentINN("123412341234 123")
                        .salary(BigDecimal.valueOf(50000))
                        .workExperienceTotal(15)
                        .workExperienceCurrent(5)
                        .employmentPosition(EmploymentPosition.MIDDLE_CLASS_MANAGER)
                        .build())
                .account("1234567890")
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.FALSE)
                .build();
        double expectedCreditRate = 15.0;
        BigDecimal result = scoreCreditRate.scoreData(scoringDataDTO);
        assertEquals(BigDecimal.valueOf(expectedCreditRate), result);
    }

    @Test
    @DisplayName("Тестирование метода scoreData: владельцы небинарные личности менеджеры среднего класса.")
    public void testScoreDataNonBinaryBusinessOwnerMiddleClassManager() throws IOException {
        scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .gender(Gender.NON_BINARY)
                .birthDate(LocalDate.of(1979, 5, 18))
                .passportSeries("1234")
                .passportNumber("123456")
                .passportIssueDate(LocalDate.of(2019, 5, 18))
                .passportIssueBranch("Saratov")
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(2)
                .employmentDTO(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.BUSINESS_OWNER)
                        .employmentINN("123412341234 123")
                        .salary(BigDecimal.valueOf(50000))
                        .workExperienceTotal(15)
                        .workExperienceCurrent(5)
                        .employmentPosition(EmploymentPosition.MIDDLE_CLASS_MANAGER)
                        .build())
                .account("1234567890")
                .isInsuranceEnabled(Boolean.TRUE)
                .isSalaryClient(Boolean.FALSE)
                .build();
        double expectedCreditRate = 21.0;
        BigDecimal result = scoreCreditRate.scoreData(scoringDataDTO);
        assertEquals(BigDecimal.valueOf(expectedCreditRate), result);
    }
}
