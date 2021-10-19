package int222.project.models;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * The persistent class for the productcolor database table.
 * 
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@JsonIgnoreProperties(value = {"id"})
public class Productcolor implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductcolorPK id;
	
	private Integer amount;

	@ManyToOne(optional = false)
    @MapsId("cid")
    @JoinColumn(name = "cid")
    private Color color;

    @ManyToOne(optional = false)
    @MapsId("pid")
    @JoinColumn(name = "pid")
    @JsonBackReference
	private Product product;
    

}