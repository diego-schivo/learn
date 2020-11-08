package com.backflipsource.petclinic;

import java.util.List;

import com.backflipsource.servlet.Table;
import com.backflipsource.servlet.View;

@View
public class Clinic {

	@View.Field(view = View.Show.class, controlFactory = Table.Factory.class, converter = OwnerStringConverter.class)
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
		instance = new Clinic(Owner.list);
	}
}
