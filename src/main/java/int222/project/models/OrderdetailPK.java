package int222.project.models;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class OrderdetailPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private ProductcolorPK productcolor;

	private Integer oid;

}