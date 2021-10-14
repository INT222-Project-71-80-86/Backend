package int222.project.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Orders implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer oid;
	
	private BigDecimal totalprice;
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@ManyToOne(optional = false)
    @MapsId("uid")
    @JoinColumn(name = "uid")
    @JsonBackReference
	private Users user;
	
	@ManyToOne
	@JoinColumn(name = "couponcode", referencedColumnName = "couponcode")
	private Coupon coupon;	
}
