package demo.assignment.tree.statementsvc.controller;


import demo.assignment.tree.statementsvc.model.Statement;
import demo.assignment.tree.statementsvc.model.StatementsSearchCriteria;
import demo.assignment.tree.statementsvc.service.StatementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value="v1/statements")
public class StatementController {

    private final StatementService statementService ;

    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @GetMapping("find/{account-id}")
    public ResponseEntity<List<Statement>> getStatement(@PathVariable("account-id") int accountId,
                                               @RequestParam(value = "fromDate" , required = false) LocalDate fromDate,
                                               @RequestParam(value = "toDate" , required = false) LocalDate toDate,
                                               @RequestParam(value = "fromAmount" , required = false) Long fromAmount,
                                               @RequestParam(value = "toAmount" , required = false) Long toAmount){

        StatementsSearchCriteria statementsSearchCriteria = StatementsSearchCriteria.builder().
                setFromDate(fromDate).
                setToDate(toDate).
                setFromAmount(fromAmount).
                setToAmount(toAmount).
                build();


        final var statementList = statementService.SearchStatements(accountId, statementsSearchCriteria);
        return ResponseEntity.ok(statementList);
    }


}
