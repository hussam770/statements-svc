package demo.assignment.tree.statementsvc.service;

import demo.assignment.tree.statementsvc.model.Statement;
import demo.assignment.tree.statementsvc.model.StatementsSearchCriteria;
import demo.assignment.tree.statementsvc.model.enums.SearchType;
import demo.assignment.tree.statementsvc.repo.StatementRepository;
import demo.assignment.tree.statementsvc.view.StatementViewDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatementService {

    private final StatementRepository statementRepository;

    private final PasswordEncoder encoder ;

    @Autowired
    public StatementService(StatementRepository statementRepository, PasswordEncoder encoder) {
        this.statementRepository = statementRepository;
        this.encoder = encoder;
    }

    public List<StatementViewDTO> searchStatements(int accountId, StatementsSearchCriteria searchCriteria) {
        log.info("searchStatements .. ");
        validateSearchCriteria(searchCriteria);
        List<Statement> statementsList = statementRepository
                .getStatementsBySearchCriteria(accountId);

        return filterBySearchCriteria(statementsList, searchCriteria);
    }

    public List<StatementViewDTO> getStatementById(int accountId) {

        log.info("getStatementById .. ");
        List<Statement> statementsList = statementRepository
                .getStatementsBySearchCriteria(accountId);

        ModelMapper mapper = new ModelMapper();
        return statementsList.stream()
                .map(s -> {
                    StatementViewDTO statementViewDTO = mapper.map(s, StatementViewDTO.class);
                    statementViewDTO.setHashedAccountNumber(encoder.encode(s.getAccountNumber()));
                    return statementViewDTO ;
                }).collect(Collectors.toList());
    }

    private void validateSearchCriteria(StatementsSearchCriteria searchCriteria) {
        // validate user input - conflict
        if ((searchCriteria.getFromDate() != null && searchCriteria.getToDate() == null)
                || searchCriteria.getFromDate() == null && searchCriteria.getToDate() != null)
            throw new IllegalArgumentException("Missing params, could not perform search with current search criteria..");

        if ((searchCriteria.getFromAmount() != null && searchCriteria.getToAmount() == null)
                || searchCriteria.getFromAmount() == null && searchCriteria.getToAmount() != null)
            throw new IllegalArgumentException("Missing params, could not perform search with current search criteria ..");

        if ((searchCriteria.getFromDate() != null && searchCriteria.getFromDate().isAfter(searchCriteria.getToDate())))
            throw new IllegalArgumentException("The from date param could not be after the to date ..");

        if ((searchCriteria.getFromAmount() != null && searchCriteria.getFromAmount() > searchCriteria.getToAmount()))
            throw new IllegalArgumentException("The from amount param could not be larger than to amount .. ");
    }

    private List<StatementViewDTO> filterBySearchCriteria(List<Statement> statementList
            , StatementsSearchCriteria searchCriteria) {
        ModelMapper mapper = new ModelMapper();
        final var searchType = defineSearchCriteria(searchCriteria);
        return statementList.stream()
                .filter(s -> this.filterBySearchCriteria(s, searchCriteria, searchType))
                .map(s -> {
                    final var statementViewDTO = mapper.map(s, StatementViewDTO.class);
                    statementViewDTO.setHashedAccountNumber(encoder.encode(s.getAccountNumber()));
                    return statementViewDTO ;
                })
                .collect(Collectors.toList());
    }

    private boolean filterBySearchCriteria(Statement item, StatementsSearchCriteria searchCriteria, SearchType searchType) {
        switch (searchType) {
            case ACCOUNT_ONLY:
                return (item.getDate().isAfter(LocalDate.now().minusMonths(3)));
            case AMOUNT_DATE:
                return (item.getDate().isAfter(searchCriteria.getFromDate())
                        && (item.getDate().isBefore(searchCriteria.getToDate())))
                        && (item.getAmount() > searchCriteria.getFromAmount())
                        && (item.getAmount() < searchCriteria.getToAmount());

            case DATE:
                return (item.getDate().isAfter(searchCriteria.getFromDate())
                        && (item.getDate().isBefore(searchCriteria.getToDate())));

            case AMOUNT:
                return (item.getAmount() > searchCriteria.getFromAmount())
                        && (item.getAmount() < searchCriteria.getToAmount());
        }
        throw new IllegalArgumentException("Something went wrong, please try later ..");
    }

    private SearchType defineSearchCriteria(StatementsSearchCriteria statementsSearchCriteria) {

        if (statementsSearchCriteria.getFromDate() == null && statementsSearchCriteria.getFromAmount() == null)
            return SearchType.ACCOUNT_ONLY;
        else if (statementsSearchCriteria.getFromDate() != null && statementsSearchCriteria.getFromAmount() != null)
            return SearchType.AMOUNT_DATE;
        else if (statementsSearchCriteria.getFromDate() != null)
            return SearchType.DATE;
        else return SearchType.AMOUNT;
    }

}
