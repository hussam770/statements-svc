package demo.assignment.tree.statementsvc.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(value = "User" , timeToLive = 60 * 5)
@Builder(setterPrefix = "set")
public class User {

    @Id
    private String username ;
    private String token ;
}
