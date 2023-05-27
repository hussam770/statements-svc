package demo.assignment.tree.statementsvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Account {

    private Integer id;
    private String accountType;
    private String accountNumber;
}
