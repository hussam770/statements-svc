package demo.assignment.tree.statementsvc.repo;

import demo.assignment.tree.statementsvc.model.Statement;
import demo.assignment.tree.statementsvc.model.mappers.StatementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JDBCStatementRepository implements StatementRepository {

    private static final String SEARCH_STATEMENTS_SQL = "select s.account_id , s.datefield , s. ID , " +
            "s.amount from statement s,account a" +
            " where int(s.account_id) = a.ID and a.ID = ? ";

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public JDBCStatementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Statement> getStatementsBySearchCriteria(int accountId) {
        return jdbcTemplate.query(SEARCH_STATEMENTS_SQL, new StatementMapper(), accountId);

    }
}
