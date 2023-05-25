package demo.assignment.tree.statementsvc.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="v1/statements")
public class StatementController {

    @GetMapping("/{statementId}")
    public ResponseEntity<String> getStatement(@PathVariable("statementId") String statementId){

        return ResponseEntity.ok("Success");
    }
}
