package study.neo.conveyor.services.interfaces;

import study.neo.conveyor.dtos.CreditDTO;
import study.neo.conveyor.dtos.LoanApplicationRequestDTO;
import study.neo.conveyor.dtos.LoanOfferDTO;
import study.neo.conveyor.dtos.ScoringDataDTO;

import java.io.IOException;
import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> offers(LoanApplicationRequestDTO loanApplicationRequestDTO) throws IOException;
    CreditDTO calculation(ScoringDataDTO scoringDataDTO) throws IOException;
}