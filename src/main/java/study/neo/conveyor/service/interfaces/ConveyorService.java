package study.neo.conveyor.service.interfaces;

import study.neo.conveyor.dto.CreditDTO;
import study.neo.conveyor.dto.LoanApplicationRequestDTO;
import study.neo.conveyor.dto.LoanOfferDTO;
import study.neo.conveyor.dto.ScoringDataDTO;

import java.util.List;

public interface ConveyorService {
    List<LoanOfferDTO> offers(LoanApplicationRequestDTO loanApplicationRequestDTO);
    CreditDTO calculation(ScoringDataDTO scoringDataDTO);
}