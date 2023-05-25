package demo.assignment.tree.statementsvc.model;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class Statement {

    private Integer Id ;
    private Double accountId ;
    private LocalDate date ;
    private String Amount ;
}
