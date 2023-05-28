package demo.assignment.tree.statementsvc.view;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StatementViewDTO {

    private Integer id;
    private Double accountId;
    private LocalDate date;
    private Double amount;
    private String accountType;
    private String hashedAccountNumber;
}
