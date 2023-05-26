package demo.assignment.tree.statementsvc.service;

import demo.assignment.tree.statementsvc.model.Statement;
import demo.assignment.tree.statementsvc.model.StatementsSearchCriteria;
import demo.assignment.tree.statementsvc.model.enums.SearchType;
import demo.assignment.tree.statementsvc.repo.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatementService {

    @Autowired
    private final StatementRepository statementRepository ;

    public StatementService(StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }

    public List<Statement> SearchStatements(int accountId , StatementsSearchCriteria searchCriteria){

        validateSearchCriteria(searchCriteria);
        List<Statement> statementsList = statementRepository
                .getStatementsBySearchCriteria(accountId);

        return filterBySearchCriteria(statementsList , searchCriteria) ;
    }

    public void validateSearchCriteria(StatementsSearchCriteria searchCriteria){
        // validate user input - conflict
        if((searchCriteria.getFromDate() != null && searchCriteria.getToDate() == null)
                || searchCriteria.getFromDate() == null && searchCriteria.getToDate() != null)
            throw new IllegalArgumentException("Missing params, could not perform search with current search criteria..") ;

        if((searchCriteria.getFromAmount() != null && searchCriteria.getToAmount() == null)
                || searchCriteria.getFromAmount() == null && searchCriteria.getToAmount() != null)
            throw new IllegalArgumentException("Missing params, could not perform search with current search criteria ..") ;

        if((searchCriteria.getFromDate() != null && searchCriteria.getFromDate().isAfter(searchCriteria.getToDate())))
            throw new IllegalArgumentException("The from date param could not be after the to date ..") ;

        if((searchCriteria.getFromAmount() != null && searchCriteria.getFromAmount() > searchCriteria.getToAmount()))
            throw new IllegalArgumentException("The from amount param could not be larger than to amount .. ") ;
    }

    private SearchType defineSearchCriteria (StatementsSearchCriteria statementsSearchCriteria) {

        if(statementsSearchCriteria.getFromDate() == null && statementsSearchCriteria.getFromAmount() == null)
            return SearchType.ACCOUNT_ONLY ;
        else if(statementsSearchCriteria.getFromDate() != null && statementsSearchCriteria.getFromAmount() != null)
            return SearchType.AMOUNT_DATE ;
        else if(statementsSearchCriteria.getFromDate() != null)
            return SearchType.DATE ;
        else return SearchType.AMOUNT;
    }

    private List<Statement> filterBySearchCriteria(List<Statement> statementList , StatementsSearchCriteria searchCriteria){
        final var searchType = defineSearchCriteria(searchCriteria);
        return statementList.stream()
                .filter(s -> this.filterBySearchCriteria(s,searchCriteria ,searchType))
                .collect(Collectors.toList());
    }

    private boolean filterBySearchCriteria(Statement item, StatementsSearchCriteria searchCriteria, SearchType searchType){
        switch (searchType){
            case ACCOUNT_ONLY : return true;
            case AMOUNT_DATE : return (item.getDate().isAfter(searchCriteria.getFromDate())
                    && (item.getDate().isBefore(searchCriteria.getToDate()) ))
                    && (Long.parseLong(item.getAmount()) > searchCriteria.getFromAmount())
                    && (Long.parseLong(item.getAmount()) < searchCriteria.getToAmount());

            case DATE: return (item.getDate().isAfter(searchCriteria.getFromDate())
                    && (item.getDate().isBefore(searchCriteria.getToDate()) ));

            case AMOUNT: return (Long.parseLong(item.getAmount()) > searchCriteria.getFromAmount())
                    && (Long.parseLong(item.getAmount()) < searchCriteria.getToAmount()) ;
        }
        throw new IllegalArgumentException("Something went wrong, please try later ..") ;
    }

}
