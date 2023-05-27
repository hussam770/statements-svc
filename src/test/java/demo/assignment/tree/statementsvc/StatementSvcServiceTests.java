package demo.assignment.tree.statementsvc;

import demo.assignment.tree.statementsvc.model.StatementsSearchCriteria;
import demo.assignment.tree.statementsvc.service.StatementService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
class StatementSvcServiceTests {

    @Autowired
    private StatementService statementService;

    private static StatementsSearchCriteria searchCriteria;

    @BeforeAll
    static void initializeSearchCriteria() {

    }



    @Test
    void testAll() {
        Integer accountId = 3;
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = LocalDate.now().minusYears(3);
        Double fromAmount = 500d;
        Double toAmount = 560d;

        initSearchCriteria(accountId , fromDate , toDate , fromAmount , toAmount);

        final var statementList = statementService.searchStatements(3, searchCriteria);
        log.debug("statements all params size are : {}", statementList.size());
        assertNotNull(statementList);
    }


    @Test
    void testDate() {
        Integer accountId = 3;
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = LocalDate.now().minusYears(3);

        initSearchCriteria(accountId , fromDate , toDate  , null , null);

        final var statementList = statementService.searchStatements(3, searchCriteria);
        log.debug("statements date params size are : {}", statementList.size());
        assertNotNull(statementList);
    }

    @Test
    void testAmount() {
        Integer accountId = 3;
        Double fromAmount = 500d;
        Double toAmount = 560d;

        initSearchCriteria(accountId , null , null , fromAmount , toAmount);

        final var statementList = statementService.searchStatements(3, searchCriteria);
        log.debug("statements amount  size are : {}", statementList.size());
        assertNotNull(statementList);
    }

    @Test
    void testAccountIdOnly() {
        Integer accountId = 3;

        initSearchCriteria(accountId , null , null , null , null);

        final var statementList = statementService.searchStatements(3, searchCriteria);
        log.debug("statements account only size are : {}", statementList.size());
        assertNotNull(statementList);
    }



    private static void initSearchCriteria(Integer accountId , LocalDate fromDate ,
                                           LocalDate toDate , Double fromAmount ,
                                           Double toAmount) {

        searchCriteria = StatementsSearchCriteria.builder().
                setFromDate(fromDate).
                setToDate(toDate).
                setFromAmount(fromAmount).
                setToAmount(toAmount).
                build();
    }

}
