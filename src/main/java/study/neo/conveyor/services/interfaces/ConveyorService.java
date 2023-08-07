package study.neo.conveyor.services.interfaces;

import study.neo.conveyor.dtos.CreditDTO;
import study.neo.conveyor.dtos.LoanApplicationRequestDTO;
import study.neo.conveyor.dtos.LoanOfferDTO;
import study.neo.conveyor.dtos.ScoringDataDTO;

import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> offers(LoanApplicationRequestDTO loanApplicationRequestDTO);
    CreditDTO calculation(ScoringDataDTO scoringDataDTO);
}