package hr.tvz.cimernik.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.tvz.cimernik.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	User findOneByUsername(String username);
}
