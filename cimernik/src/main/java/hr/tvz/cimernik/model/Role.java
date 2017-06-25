package hr.tvz.cimernik.model;

import java.io.Serializable;
import javax.persistence.Entity;
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
@Table(name="user_role")
public class Role implements Serializable {

	private static final long serialVersionUID = 8637599027032909613L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	@Setter
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "username", referencedColumnName = "username")
	@Getter
	@Setter
	private User user;
	
	@Getter
	@Setter
	private String role;

}
