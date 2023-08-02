package study.neo.conveyor.services.classes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import study.neo.conveyor.dtos.*;
import study.neo.conveyor.services.interfaces.ConveyorService;
import study.neo.conveyor.validations.LoanApplicationRequestValidation;
import study.neo.conveyor.validations.ScoringDataRejectValuesValidation;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConveyorServiceImpl implements ConveyorService {
    private final LoanApplicationRequestValidation loanApplicationRequestValidation;
    private final ScoringDataRejectValuesValidation scoringDataRejectValuesValidation;
    private final CreditDtoService creditDtoService;
    private final LoanOfferService loanOfferService;

    @Override
    public List<LoanOfferDTO> offers(LoanApplicationRequestDTO loanApplicationRequestDTO) throws IOException {
        log.info("Запрос с контроллера на расчет предложений по кредиту передан в ConveyorService.");
        loanApplicationRequestValidation.callAllValidations(loanApplicationRequestDTO);
        log.info("Валидация loanApplicationDTO: {} успешна пройдена.", loanApplicationRequestDTO);
        List<LoanOfferDTO> listOfLoanOffers = Stream.of(
                loanOfferService.createLoanOffer(Boolean.TRUE, Boolean.FALSE, loanApplicationRequestDTO),
                loanOfferService.createLoanOffer(Boolean.FALSE, Boolean.TRUE, loanApplicationRequestDTO),
                loanOfferService.createLoanOffer(Boolean.TRUE, Boolean.TRUE, loanApplicationRequestDTO),
                loanOfferService.createLoanOffer(Boolean.FALSE, Boolean.FALSE, loanApplicationRequestDTO)
        ).sorted((Comparator.comparing(LoanOfferDTO::getRate))).collect(Collectors.toList());
        for (long i = 0L; i < 4; i++) {
            listOfLoanOffers.get(Math.toIntExact(i)).setApplicationId(i);
        }
        log.info("В сервисе ConveyorService сформирован отсортированный " +
                "лист предложений listOfLoanOffers: {}", listOfLoanOffers);
        return listOfLoanOffers;
    }

    @Override
    public CreditDTO calculation(ScoringDataDTO scoringDataDTO) throws IOException {
        log.info("Запрос с контроллера на расчет кредита передан в ConveyorService.");
        scoringDataRejectValuesValidation.callAllValidations(scoringDataDTO);
        log.info("Валидация scoringDataDTO: {} успешна пройдена.", scoringDataDTO);
        return creditDtoService.createCreditDTO(scoringDataDTO);
    }
}