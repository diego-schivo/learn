package com.backflipsource.menagerie;

import static com.backflipsource.Helpers.safeList;

import java.util.List;

import com.backflipsource.form.FormField;
import com.backflipsource.form.Select;

public class Owner {

	private String name;

	@FormField(control = Select.class, converter = PetStringConverter.class)
	private List<Pet> pets;

	public Owner() {
	}

	public Owner(String name, List<Pet> pets) {
		this.name = name;
		this.pets = pets;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public static List<Owner> getList() {
		return list;
	}

	public static void setList(List<Owner> list) {
		Owner.list = list;
	}

	public static List<Owner> list;

	static {
		Owner harold = new Owner("Harold", null);
		Owner gwen = new Owner("Gwen", null);
		list = safeList(new Owner[] { harold, gwen });
	}
}
