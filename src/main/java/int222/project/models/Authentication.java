package int222.project.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

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
public class Authentication implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer uid;
	
	@OneToOne
	@MapsId
	@JoinColumn(name = "uid")
	@JsonBackReference
	private Users user;
	
	private String password;
	
}
