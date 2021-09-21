package int222.project.models;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Orderdetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private OrderdetailPK id;
    private Integer amount;
    private BigDecimal priceeach;
	@ManyToOne(optional = false)
    @MapsId("uid")
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne(optional = false)
    @MapsId("pid")
    @JoinColumn(name = "pid")
	private Product product;

	

}