package hr.tvz.cimernik.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.tvz.cimernik.model.RoomateGroup;


@Repository
public interface RoomateGroupRepository extends JpaRepository<RoomateGroup, Integer>{

}
