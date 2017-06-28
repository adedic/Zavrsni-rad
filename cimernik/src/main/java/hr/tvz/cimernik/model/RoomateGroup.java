package hr.tvz.cimernik.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

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
@Table(name = "roomate_group")
public class RoomateGroup implements Serializable {
	
	private static final long serialVersionUID = 8064241938700065376L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	@Getter
	@Setter
	private Integer id;
	@Getter
	@Setter
	@NotNull	
	@NotEmpty(message = "Moraš unijeti naziv grupe.")
	private String name;
	
	@Getter
	@Setter
	@Column(name="date_created")
	private Date dateCreated;
			
	@Getter
	@Setter
	@OneToMany(mappedBy="roomateGroup",fetch=FetchType.EAGER) 
	private List<User> members;
	
	@Transient
	@Getter
	@Setter
	private boolean hasInvite;

	public RoomateGroup(String name, List<User> members){
		this.name =  name;
		this.members = members;
		this.dateCreated = new Date();
	}
}
