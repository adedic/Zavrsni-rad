package hr.tvz.cimernik.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "bill")
public class Bill implements Serializable {

	private static final long serialVersionUID = 3595524199222456759L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	@Setter
	private Integer id;

	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	@Getter
	@Setter
	private RoomateGroup roomateGroup;

	@Getter
	@Setter
	private String title;
	@Getter
	@Setter
	private BigDecimal price;
	@Temporal(TemporalType.DATE)
	@Getter
	@Setter

	@Column(name = "date_created")
	private Date dateCreated;

	@Temporal(TemporalType.DATE)
	@Getter
	@Setter
	@Column(name = "last_update_date")
	private Date lastUpdateDate;
	@Getter
	@Setter

	private String description;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	@Getter
	@Setter
	private Category category;

	public Bill(User user, RoomateGroup roomateGroup, String title, BigDecimal price, Date dateCreated,
			String description, Category category) {
		this.user = user;
		this.roomateGroup = roomateGroup;
		this.title = title;
		this.price = price;
		this.dateCreated = dateCreated;
		this.description = description;
		this.category = category;
	}


}
