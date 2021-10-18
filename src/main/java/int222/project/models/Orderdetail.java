package int222.project.models;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    @MapsId("oid")
    @JoinColumn(name = "oid")
	@JsonBackReference
    private Orders order;

//    @ManyToOne(optional = false)
//    @MapsId("id")
//    @JoinColumns({
//    	@JoinColumn(name = "pid"),
//    	@JoinColumn(name = "cid")
//    })
//    @JsonBackReference
//	private Productcolor productcolor;
	
	  @ManyToOne(optional = false)
	  @MapsId("id")
	  @JoinColumn(name = "pid")
	  @JsonBackReference
		private Product product;
	  
	  @ManyToOne(optional = false)
	  @MapsId("id")
	  @JoinColumn(name = "cid")
	  @JsonBackReference
		private Color color;


}
