package int222.project.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the product database table.
 * 
 */
@Entity
@NamedQuery(name="Product.findAll", query="SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int pid;

	private int amount;

	private String description;
	
	private String image;

	private String name;

	private BigDecimal price;

	@Temporal(TemporalType.DATE)
	@Column(name="release_date")
	private Date releaseDate;

	//bi-directional many-to-one association to Brand
	@ManyToOne
	@JoinColumn(name="bid")
	private Brand brand;

	//bi-directional many-to-one association to Category
	@ManyToOne
	@JoinColumn(name="catid")
	private Category category;

	//bi-directional many-to-one association to Productcolor
	@OneToMany(mappedBy="product")
	private List<Productcolor> productcolors;

	public Product() {
	}

	public int getPid() {
		return this.pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getReleaseDate() {
		return this.releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Brand getBrand() {
		return this.brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Productcolor> getProductcolors() {
		return this.productcolors;
	}

	public void setProductcolors(List<Productcolor> productcolors) {
		this.productcolors = productcolors;
	}

	public Productcolor addProductcolor(Productcolor productcolor) {
		getProductcolors().add(productcolor);
		productcolor.setProduct(this);

		return productcolor;
	}

	public Productcolor removeProductcolor(Productcolor productcolor) {
		getProductcolors().remove(productcolor);
		productcolor.setProduct(null);

		return productcolor;
	}

}