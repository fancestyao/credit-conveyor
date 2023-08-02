package study.neo.conveyor.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.neo.conveyor.dtos.CreditDTO;
import study.neo.conveyor.dtos.LoanApplicationRequestDTO;
import study.neo.conveyor.dtos.LoanOfferDTO;
import study.neo.conveyor.dtos.ScoringDataDTO;
import study.neo.conveyor.services.interfaces.ConveyorService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/conveyor")
@RequiredArgsConstructor
@Slf4j
@Api
public class ConveyorController {
    private final ConveyorService conveyorService;

    @PostMapping("/offers")
    @ApiOperation("offers")
    public ResponseEntity<List<LoanOfferDTO>> offers(@RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO)
            throws IOException {
        log.info("Получен запрос в контроллер на расчет условий кредита с loanApplicationRequestDTO: {}",
                loanApplicationRequestDTO);
        return new ResponseEntity<>(conveyorService.offers(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/calculation")
    @ApiOperation("calculation")
    public ResponseEntity<CreditDTO> calculation(@RequestBody ScoringDataDTO scoringDataDTO) throws IOException {
        log.info("Получен запрос в контроллер на расчет параметров кредита с scoringDataDTO: {}",
                scoringDataDTO);
        return new ResponseEntity<>(conveyorService.calculation(scoringDataDTO), HttpStatus.OK);
    }
}
