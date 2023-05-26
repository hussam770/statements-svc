package demo.assignment.tree.statementsvc.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(setterPrefix = "set")
public class StatementsSearchCriteria {
    private Integer accountId ;
    private Long fromAmount ;
    private Long toAmount ;
    private LocalDate fromDate ;
    private LocalDate toDate ;

}

