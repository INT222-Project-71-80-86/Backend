package int222.project.models;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * The persistent class for the color database table.
 * 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Color implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cid")
	private Integer cid;
	
	private String name;
	
	private String code;

}