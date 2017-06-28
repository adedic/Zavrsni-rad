package hr.tvz.cimernik.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hr.tvz.cimernik.model.Invite;
import hr.tvz.cimernik.model.RoomateGroup;
import hr.tvz.cimernik.model.User;

@Repository
public interface InviteRepository extends JpaRepository<Invite, Integer>  {
	List<Invite> findAllByMember(User member);
	List<Invite> findAllByRoomateGroup(RoomateGroup rm);
	List<Invite> findAllByRoomateGroupAndMember(RoomateGroup rg, User member);
	Invite findOneByRoomateGroupAndMemberAndInviter(RoomateGroup rg, User member, User inviter);
	List<Invite> findAllByRoomateGroupAndMemberIsNot(RoomateGroup rm, User member);
	List<Invite> findAllByInviter(User inviter);
	}
