package demo.assignment.tree.statementsvc.model.mappers;

import demo.assignment.tree.statementsvc.model.Statement;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StatementMapper implements RowMapper<Statement> {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public Statement mapRow(ResultSet rs, int rowNum) throws SQLException {
        LocalDate localDate = LocalDate.parse(
                rs.getString("datefield"),
                dateTimeFormatter);
        return new Statement(rs.getInt("id"),
                rs.getDouble("account_id"),
                localDate,
                Double.parseDouble(rs.getString("amount")));
    }
}
