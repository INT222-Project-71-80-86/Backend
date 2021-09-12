package int222.project.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the productcolor database table.
 * 
 */
@Entity
public class Productcolor implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductcolorPK id;

	@ManyToOne(optional = false)
    @MapsId("cid")
    @JoinColumn(name = "cid")
	private Color color;

    @ManyToOne(optional = false)
    @MapsId("pid")
    @JoinColumn(name = "pid")
    @JsonBackReference
	private Product product;

	public Productcolor() {
	}

	public ProductcolorPK getId() {
		return this.id;
	}

	public void setId(ProductcolorPK id) {
		this.id = id;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}