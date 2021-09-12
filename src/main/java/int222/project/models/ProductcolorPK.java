package int222.project.models;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the productcolor database table.
 * 
 */
@Embeddable
public class ProductcolorPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="product_pid", insertable=false, updatable=false)
	private int productPid;

	@Column(name="color_cid", insertable=false, updatable=false)
	private int colorCid;

	public ProductcolorPK() {
	}
	public int getProductPid() {
		return this.productPid;
	}
	public void setProductPid(int productPid) {
		this.productPid = productPid;
	}
	public int getColorCid() {
		return this.colorCid;
	}
	public void setColorCid(int colorCid) {
		this.colorCid = colorCid;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ProductcolorPK)) {
			return false;
		}
		ProductcolorPK castOther = (ProductcolorPK)other;
		return 
			(this.productPid == castOther.productPid)
			&& (this.colorCid == castOther.colorCid);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productPid;
		hash = hash * prime + this.colorCid;
		
		return hash;
	}
}