package study.neo.conveyor.service.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.neo.conveyor.dto.CreditDTO;
import study.neo.conveyor.dto.LoanApplicationRequestDTO;
import study.neo.conveyor.dto.LoanOfferDTO;
import study.neo.conveyor.dto.ScoringDataDTO;
import study.neo.conveyor.service.interfaces.ConveyorService;
import study.neo.conveyor.validation.ScoringDataRejectValuesValidation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConveyorServiceImpl implements ConveyorService {
    private final ScoringDataRejectValuesValidation scoringDataRejectValuesValidation;
    private final CreditDtoService creditDtoService;
    private final LoanOfferService loanOfferService;

    @Override
    public List<LoanOfferDTO> offers(LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Запрос с контроллера на расчет предложений по кредиту передан в ConveyorService.");
        List<LoanOfferDTO> listOfLoanOffers = Stream.of(
                loanOfferService.createLoanOffer(Boolean.TRUE, Boolean.FALSE, loanApplicationRequestDTO),
                loanOfferService.createLoanOffer(Boolean.FALSE, Boolean.TRUE, loanApplicationRequestDTO),
                loanOfferService.createLoanOffer(Boolean.TRUE, Boolean.TRUE, loanApplicationRequestDTO),
                loanOfferService.createLoanOffer(Boolean.FALSE, Boolean.FALSE, loanApplicationRequestDTO)
        ).sorted((Comparator.comparing(LoanOfferDTO::getRate)).reversed()).collect(Collectors.toList());
        log.info("В сервисе ConveyorService сформирован отсортированный " +
                "лист предложений listOfLoanOffers: {}", listOfLoanOffers);
        return listOfLoanOffers;
    }

    @Override
    public CreditDTO calculation(ScoringDataDTO scoringDataDTO) {
        log.info("Запрос с контроллера на расчет кредита передан в ConveyorService.");
        scoringDataRejectValuesValidation.callAllValidations(scoringDataDTO);
        log.info("Валидация scoringDataDTO: {} успешна пройдена.", scoringDataDTO);
        return creditDtoService.createCreditDTO(scoringDataDTO);
    }
}