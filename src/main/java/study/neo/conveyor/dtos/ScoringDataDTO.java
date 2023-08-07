package study.neo.conveyor.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import study.neo.conveyor.enums.Gender;
import study.neo.conveyor.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoringDataDTO {
    @Schema(description = "Желаемая сумма кредита", example = "100000")
    private BigDecimal amount;
    @Schema(description = "Желаемый период погашения кредита (в месяцах)", example = "18")
    private Integer term;
    @Schema(description = "Имя клиента", example = "Ivan")
    private String firstName;
    @Schema(description = "Фамилия клиента", example = "Ivanov")
    private String lastName;
    @Schema(description = "Отчество клиента (при наличии)", example = "Ivanovich")
    private String middleName;
    @Schema(description = "Пол клиента", example = "FEMALE")
    private Gender gender;
    @Schema(description = "Дата рождения клиента в формате yyyy-MM-dd", example = "1999.05.18")
    private LocalDate birthDate;
    @Schema(description = "Серия паспорта клиента", example = "1234")
    private String passportSeries;
    @Schema(description = "Номер паспорта клиента", example = "123456")
    private String passportNumber;
    @Schema(description = "Дата выдачи паспорта клиента в формате yyyy-MM-dd", example = "2020.05.27")
    private LocalDate passportIssueDate;
    @Schema(description = "Место выдачи паспорта клиента", example = "Saratov")
    private String passportIssueBranch;
    @Schema(description = "Семейное положение клиента", example = "MARRIED")
    private MaritalStatus maritalStatus;
    @Schema(description = "Количество иждивенцев", example = "0")
    private Integer dependentAmount;
    @Schema(description = "Информация о трудоустройстве клиента", example =
                    "{\"employmentStatus\": \"SELF_EMPLOYED\"," +
                    " \"employmentINN\": \"1234567890\"," +
                    " \"salary\": \"50000\"," +
                    " \"workExperienceTotal\": 15," +
                    " \"workExperienceCurrent\": 5," +
                    " \"employmentPosition\": \"TOP_CLASS_MANAGER\"}")
    private EmploymentDTO employmentDTO;
    @Schema(description = "Банковский счет клиента", example = "12341234123412341234")
    private String account;
    @Schema(description = "Имеет ли клиент страхование", example = "true")
    private Boolean isInsuranceEnabled;
    @Schema(description = "Имеет ли клиент заработную плату", example = "false")
    private Boolean isSalaryClient;
}
