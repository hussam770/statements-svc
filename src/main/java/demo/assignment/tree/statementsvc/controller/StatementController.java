package demo.assignment.tree.statementsvc.controller;


import demo.assignment.tree.statementsvc.model.StatementsSearchCriteria;
import demo.assignment.tree.statementsvc.service.StatementService;
import demo.assignment.tree.statementsvc.view.StatementViewDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "v1/statements")
public class StatementController {

    private final StatementService statementService;

    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @GetMapping("find/{account-id}")
    public ResponseEntity<List<StatementViewDTO>> getStatement(@PathVariable("account-id") int accountId,
                                                               @RequestParam(value = "fromDate", required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                               @RequestParam(value = "toDate", required = false)
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                               @RequestParam(value = "fromAmount", required = false) Double fromAmount,
                                                               @RequestParam(value = "toAmount", required = false) Double toAmount) {

        log.info("getStatement ..");

        StatementsSearchCriteria statementsSearchCriteria = StatementsSearchCriteria.builder().
                setFromDate(fromDate).
                setToDate(toDate).
                setFromAmount(fromAmount).
                setToAmount(toAmount).
                build();


        final var statementList = statementService.searchStatements(accountId, statementsSearchCriteria);
        return ResponseEntity.ok(statementList);
    }

    @GetMapping("find-by-account/{account-id}")
    public ResponseEntity<List<StatementViewDTO>> getStatementByAccountId(@PathVariable("account-id") int accountId) {
        log.info("getStatementByAccountId ..");
        final var statementList = statementService.getStatementById(accountId);
        return ResponseEntity.ok(statementList);
    }


}
