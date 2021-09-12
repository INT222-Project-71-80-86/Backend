package int222.project.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the color database table.
 * 
 */
@Entity
public class Color implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int cid;

	private String name;

	//bi-directional many-to-one association to Productcolor
	@OneToMany(mappedBy="color")
	private List<Productcolor> productcolors;

	public Color() {
	}

	public int getCid() {
		return this.cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Productcolor> getProductcolors() {
		return this.productcolors;
	}

	public void setProductcolors(List<Productcolor> productcolors) {
		this.productcolors = productcolors;
	}

	public Productcolor addProductcolor(Productcolor productcolor) {
		getProductcolors().add(productcolor);
		productcolor.setColor(this);

		return productcolor;
	}

	public Productcolor removeProductcolor(Productcolor productcolor) {
		getProductcolors().remove(productcolor);
		productcolor.setColor(null);

		return productcolor;
	}

}