package hr.tvz.cimernik.db;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.tvz.cimernik.model.Bill;
import hr.tvz.cimernik.model.RoomateGroup;
import hr.tvz.cimernik.model.User;


@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
	List<Bill> findAllByUser(User user);
	List<Bill> findAllByRoomateGroup(RoomateGroup rg);


	List<Bill> findAllByUserAndCategoryDescription(User user, String description);
}

