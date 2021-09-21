package int222.project.models;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class OrderdetailPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer pid;

	private Integer oid;

}