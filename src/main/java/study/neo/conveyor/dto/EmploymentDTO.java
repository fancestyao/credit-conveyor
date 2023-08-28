package study.neo.conveyor.dto;

import lombok.Builder;
import lombok.Data;
import study.neo.conveyor.enums.EmploymentStatus;
import study.neo.conveyor.enums.EmploymentPosition;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employmentINN;
    private BigDecimal salary;
    private EmploymentPosition employmentPosition;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}