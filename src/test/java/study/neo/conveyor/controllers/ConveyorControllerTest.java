package study.neo.conveyor.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import study.neo.conveyor.dtos.CreditDTO;
import study.neo.conveyor.dtos.LoanApplicationRequestDTO;
import study.neo.conveyor.dtos.LoanOfferDTO;
import study.neo.conveyor.dtos.ScoringDataDTO;
import study.neo.conveyor.services.interfaces.ConveyorService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Тест контроллера ConveyorController.")
public class ConveyorControllerTest {
    @InjectMocks
    private ConveyorController conveyorController;
    @Mock
    private ConveyorService conveyorService;

    @Test
    @DisplayName("Тест по расчету кредита.")
    void calculationTest() throws Exception {
        ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
        CreditDTO expectedDTO = new CreditDTO();
        when(conveyorService.calculation(scoringDataDTO)).thenReturn(expectedDTO);
        ResponseEntity<CreditDTO> response = conveyorController.calculation(scoringDataDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDTO, response.getBody());
        verify(conveyorService).calculation(scoringDataDTO);
    }

    @Test
    @DisplayName("Тест по кредитному предложению.")
    void offersTest() throws Exception {
        LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();
        LoanOfferDTO loanOfferDTO1 = new LoanOfferDTO();
        LoanOfferDTO loanOfferDTO2 = new LoanOfferDTO();
        LoanOfferDTO loanOfferDTO3 = new LoanOfferDTO();
        LoanOfferDTO loanOfferDTO4 = new LoanOfferDTO();
        List<LoanOfferDTO> resultList = List.of(loanOfferDTO1, loanOfferDTO2,
                loanOfferDTO3, loanOfferDTO4);
        when(conveyorService.offers(Mockito.any(LoanApplicationRequestDTO.class))).thenReturn(resultList);
        ResponseEntity<List<LoanOfferDTO>> response = conveyorController.offers(loanApplicationRequestDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultList, response.getBody());
        verify(conveyorService).offers(loanApplicationRequestDTO);
    }
}
