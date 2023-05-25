package demo.assignment.tree.statementsvc.service;

import demo.assignment.tree.statementsvc.model.Statement;
import demo.assignment.tree.statementsvc.model.StatementsSearchCriteria;
import demo.assignment.tree.statementsvc.repo.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatementService {

    @Autowired
    private final StatementRepository statementRepository ;

    public StatementService(StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }

    private List<Statement> SearchStatements(StatementsSearchCriteria searchCriteria){

        return null ;
    }

    private void validateSearchCriteria(StatementsSearchCriteria searchCriteria){

    }







}
