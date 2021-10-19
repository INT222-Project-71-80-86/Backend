package int222.project.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Orders implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oid;
	
	private BigDecimal totalprice;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "uid")
    @JsonBackReference
	private Users user;
	
	@ManyToOne
	@JoinColumn(name = "couponcode", referencedColumnName = "couponcode")
	private Coupon coupon;	
	
	private String status;
	
	@OneToMany(mappedBy = "order", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    List<Orderdetail> orderdetail;
}
