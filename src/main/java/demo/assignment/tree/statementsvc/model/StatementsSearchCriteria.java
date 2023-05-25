package demo.assignment.tree.statementsvc.model;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(builderMethodName = "of")
public class StatementsSearchCriteria {
    private enum SEARCH_TYPE {AMOUNT , DATE , AMOUNT_DATE , ACCOUNT_ONLY}
    private Integer accountId ;
    private Long fromAmount ;
    private Long toAmount ;
    private LocalDate fromDate ;
    private LocalDate toDate ;

    private SEARCH_TYPE defineSearchCriteria () {

        if(fromDate == null && fromAmount == null)
            return SEARCH_TYPE.ACCOUNT_ONLY ;
        else if(fromDate != null && fromAmount != null)
            return SEARCH_TYPE.AMOUNT_DATE ;
        else if(fromDate != null)
            return SEARCH_TYPE.DATE ;
        else return SEARCH_TYPE.AMOUNT;
    }
}

