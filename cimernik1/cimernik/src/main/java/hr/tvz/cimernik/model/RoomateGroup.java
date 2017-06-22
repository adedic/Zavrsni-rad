package hr.tvz.cimernik.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
	private String name;
	@Getter
	@Setter
	@OneToMany(mappedBy="roomateGroup",fetch=FetchType.EAGER) 
	private List<User> members;

	public RoomateGroup(String name, List<User> members){
		this.name =  name;
		this.members = members;
	}
	
}
