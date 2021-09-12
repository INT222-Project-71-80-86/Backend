package int222.project.models;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * The persistent class for the brand database table.
 * 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Brand implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int bid;
	private String name;

//	public Brand() {
//	}
//
//	public int getBid() {
//		return this.bid;
//	}
//
//	public void setBid(int bid) {
//		this.bid = bid;
//	}
//
//	public String getName() {
//		return this.name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}

}