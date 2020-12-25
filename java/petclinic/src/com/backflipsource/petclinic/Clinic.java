package com.backflipsource.petclinic;

import java.util.List;

import com.backflipsource.ui.Entity;
import com.backflipsource.ui.EntityDetail;
import com.backflipsource.ui.Render;
import com.backflipsource.ui.Table;

@Entity
public class Clinic {

	@Entity.Field(mode = EntityDetail.class, converter = OwnerStringConverter.class)
	@Render(mode = EntityDetail.class, controlFactory = Table.Factory.class)
	private List<Owner> owners;

	public Clinic() {
	}

	public Clinic(List<Owner> owners) {
		this.owners = owners;
	}

	public List<Owner> getOwners() {
		return owners;
	}

	public void setOwners(List<Owner> owners) {
		this.owners = owners;
	}

	public static Clinic instance;

	static {
		instance = new Clinic(Owner.data.list());
	}
}
