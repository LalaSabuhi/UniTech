package az.unibank.unitech.repos;

import az.unibank.unitech.entities.Account;
import az.unibank.unitech.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query(nativeQuery = true, value ="select * from users u " +
            "where u.username = :username and u.enabled = true ")
    Optional<User> findUserByUsername(@Param("username") String username);

}
