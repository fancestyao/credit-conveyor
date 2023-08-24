package study.neo.conveyor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.neo.conveyor.dto.CreditDTO;
import study.neo.conveyor.dto.LoanApplicationRequestDTO;
import study.neo.conveyor.dto.LoanOfferDTO;
import study.neo.conveyor.dto.ScoringDataDTO;
import study.neo.conveyor.service.interfaces.ConveyorService;

import java.util.List;

@RestController
@RequestMapping("/conveyor")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Conveyor API")
public class ConveyorController {
    private final ConveyorService conveyorService;

    @PostMapping("/offers")
    @Operation(summary = "Расчет условий кредита для пользователя",
            description = "Возвращает лист с 4 LoanOfferDTO по входному LoanApplicationRequestDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция прошла успешно"),
            @ApiResponse(responseCode = "409", description = "Входные данные не прошли прескоринг")
    })
    public ResponseEntity<List<LoanOfferDTO>> offers(@RequestBody @Parameter(description =
            "Входные параметры для расчета условий кредита для пользователя")
                                                         LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Получен запрос в контроллер на расчет условий кредита с loanApplicationRequestDTO: {}",
                loanApplicationRequestDTO);
        return new ResponseEntity<>(conveyorService.offers(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/calculation")
    @Operation(summary = "Расчет параметров кредита",
            description = "Возвращает CreditDTO по входному ScoringDataDTO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция прошла успешно"),
            @ApiResponse(responseCode = "409", description = "Входные данные не прошли скоринг")
    })
    public ResponseEntity<CreditDTO> calculation(@RequestBody @Parameter(description =
            "Входные параметры для расчета параметров кредита") ScoringDataDTO scoringDataDTO) {
        log.info("Получен запрос в контроллер на расчет параметров кредита с scoringDataDTO: {}",
                scoringDataDTO);
        return new ResponseEntity<>(conveyorService.calculation(scoringDataDTO), HttpStatus.OK);
    }
}
