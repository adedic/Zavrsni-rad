package hr.tvz.cimernik.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = -1613905173337484737L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	@Getter
	@Setter
	private RoomateGroup roomateGroup;
	@Getter
	@Setter
	private String name;
	@Getter
	@Setter
	private String surname;
	@Getter
	@Setter
	private String phone;
	@Getter
	@Setter
	private String username;
	@Getter
	private String password;
	@Getter
	@Setter
	private boolean enabled;
	@Getter
	@OneToMany(mappedBy = "member", cascade = { CascadeType.ALL }, orphanRemoval = true)
	private List<Invite> invites;

	@Getter
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true)
	private List<Role> roleList;


	public String getCredentials() {
		return this.name + " " + this.surname;
	}

	public User(String username, boolean enabled) {
		this.username = username;
		this.enabled = enabled;
		roleList = new ArrayList<>();
	}
	public void setPassword(String password) {
		if (password != null)
			this.password = new BCryptPasswordEncoder().encode(password);
		else
			this.password = password;
	}
	
	public void setRoleList(String... roles) {
		for (String role : roles) {
			Role newRole = new Role();
			newRole.setUser(this);
			newRole.setRole(role);
			roleList.add(newRole);
		}
	}
	public void setInvites(Invite invite){
		this.invites.add(invite);
	}
	


	
}
