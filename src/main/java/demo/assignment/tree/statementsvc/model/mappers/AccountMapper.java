package demo.assignment.tree.statementsvc.model.mappers;

import demo.assignment.tree.statementsvc.model.Account;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements RowMapper<Account> {
    @Override
    public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Account(rs.getInt("ID"),
                rs.getString("account_type"),
                rs.getString("account_number"));
    }
}
