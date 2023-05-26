package demo.assignment.tree.statementsvc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class Statement {

    private Integer Id ;
    private Double accountId ;
    private LocalDate date ;
    private String Amount ;
}
