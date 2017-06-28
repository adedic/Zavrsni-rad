package hr.tvz.cimernik.db;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hr.tvz.cimernik.model.Bill;
import hr.tvz.cimernik.model.RoomateGroup;
import hr.tvz.cimernik.model.User;


@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
	List<Bill> findAllByUser(User user);
	List<Bill> findAllByRoomateGroup(RoomateGroup rg);
	List<Bill> findAllByUserAndRoomateGroup(User user, RoomateGroup roomateGroup);
	@Query("SELECT b FROM Bill b "
			+ "WHERE YEAR(b.dateCreated) = YEAR(CURDATE()) "
			+ "AND MONTH(b.dateCreated) = ?1 "
			+ "AND b.roomateGroup = ?2")
	
	List<Bill> findAllByDateCreatedLike(Integer month, RoomateGroup group);
	
	
	@Query("SELECT b FROM Bill b "
			+ "WHERE YEAR(b.dateCreated) = YEAR(CURDATE()) "
			+ "AND MONTH(b.dateCreated) = ?1 "
			+ "AND b.roomateGroup = ?2 "
			+ "AND b.user = ?3")
	List<Bill> findAllByDateCreatedAndUserLike(Integer month, RoomateGroup group, User user);
}

