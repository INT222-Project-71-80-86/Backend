package int222.project.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Review implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ReviewPK id;
	
	private String review;
	private Integer rating;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date datetime;
	
	@ManyToOne(optional = false)
	@MapsId("pid")
    @JoinColumn(name = "pid")
	@JsonBackReference //For not making review trace to product because we only use review in product's page.
	private Product product;
	
	@ManyToOne(optional = false)
	@MapsId("uid")
	@JoinColumn(name = "uid")
	// Sending User to review page (mainly username)
	private Users user;
}
