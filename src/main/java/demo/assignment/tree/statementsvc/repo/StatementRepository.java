package demo.assignment.tree.statementsvc.repo;

import demo.assignment.tree.statementsvc.model.Statement;

import java.util.List;

public interface StatementRepository {

    List<Statement> getStatementsBySearchCriteria(String statementId);
}
