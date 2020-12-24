package com.backflipsource.petclinic;

import java.util.List;

import com.backflipsource.entity.annotation.Render;
import com.backflipsource.servlet.Table;
import com.backflipsource.servlet.View;

@View
public class Clinic {

	@View.Field(view = View.Show.class, converter = OwnerStringConverter.class)
	@Render(view = View.Show.class, controlFactory = Table.Factory.class)
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
