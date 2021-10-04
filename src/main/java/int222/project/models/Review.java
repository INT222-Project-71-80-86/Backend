package int222.project.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer reviewid;
	
	private String review;
	private Integer rating;
	
	@ManyToOne(optional = false)
    @MapsId("pid")
    @JoinColumn(name = "pid")
	@JsonBackReference //For not making review trace to product because we only use review in product's page.
	private Product product;
}
