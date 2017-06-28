package hr.tvz.cimernik.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Entity
@Table(name = "invite")
public class Invite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Integer id;
	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	private RoomateGroup roomateGroup;
	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User member;
	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "inviter_id")
	private User inviter;

	public Invite(RoomateGroup group, User member, User inviter) {
		this.roomateGroup = group;
		this.member = member;
		this.inviter = inviter;
	}

}
