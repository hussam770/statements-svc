package demo.assignment.tree.statementsvc.repo;

import demo.assignment.tree.statementsvc.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
