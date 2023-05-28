package demo.assignment.tree.statementsvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class Statement {

    private Integer id;
    private Double accountId;
    private LocalDate date;
    private Double amount;
    private String accountType;
    private String accountNumber;
}
