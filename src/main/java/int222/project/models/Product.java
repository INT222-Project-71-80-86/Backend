package int222.project.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the product database table.
 * 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer pid;
	private String description;
	private String image;
	private String name;
	private BigDecimal price;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name="release_date")
	private Date releaseDate;
    private Integer warranty;
    private Integer deleted;
    
    @ManyToOne
    @JoinColumn(name = "bid", referencedColumnName = "bid")
    private Brand brand; 
    @ManyToOne 
    @JoinColumn(name = "catid", referencedColumnName = "catid")
    private Category category; 

    @OneToMany(mappedBy = "product", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    List<Productcolor> productcolor;


}