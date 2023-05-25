package demo.assignment.tree.statementsvc;

import demo.assignment.tree.statementsvc.model.Statement;
import demo.assignment.tree.statementsvc.repo.StatementRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class StatementSvcApplicationTests {

	@Autowired
	private StatementRepository statementRepository ;

	@Test
	void testRepo() {
		List<Statement> statements = statementRepository.getStatementsBySearchCriteria("4");
		log.debug("statements size are : {}" , statements.size() );
		assertNotNull(statements);
	}

}
