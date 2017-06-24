package hr.tvz.cimernik.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.tvz.cimernik.model.RoomateGroup;


@Repository
public interface RoomateGroupRepository extends JpaRepository<RoomateGroup, Integer>{
	List<RoomateGroup> findAllByNameContaining(String name);
	List<RoomateGroup> findAllByNameLike(String name);
}
