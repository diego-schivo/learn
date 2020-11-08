package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeRun;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.backflipsource.servlet.Select;
import com.backflipsource.servlet.Select.Options;
import com.backflipsource.servlet.StringConverter;
import com.backflipsource.servlet.View;

@View
public class Pet {

	private String name;

	@View.Field(control = Select.class, converter = OwnerStringConverter.class)
	private Owner owner;

	@View.Field(control = Select.class)
	@Options({ "bird", "cat", "dog", "hamster", "snake" })
	private String species;

	@View.Field(control = Select.class)
	@Options({ "f", "m" })
	private String sex;

	@View.Field(converter = StringConverter.ForLocalDate.class)
	private LocalDate birth;

	@View.Field(converter = StringConverter.ForLocalDate.class)
	private LocalDate death;

	public Pet() {
	}

	public Pet(String name, Owner owner, String species, String sex, LocalDate birth, LocalDate death) {
		this.name = name;
		this.owner = owner;
		this.species = species;
		this.sex = sex;
		this.birth = birth;
		this.death = death;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public LocalDate getDeath() {
		return death;
	}

	public void setDeath(LocalDate death) {
		this.death = death;
	}

	public static List<Pet> list;

	static {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		Owner harold = safeGet(() -> Owner.list.get(0));
		// Owner gwen = safeGet(() -> Owner.list.get(1));

		Pet fluffy = new Pet("Fluffy", harold, "cat", "f", LocalDate.parse("1993-02-04", formatter), null);
		// Pet claws = new Pet("Claws", gwen, "cat", "m", LocalDate.parse("1994-03-17",
		// formatter), null);
		// list = safeList(new Pet[] { fluffy, claws });
		list = safeList(new Pet[] { fluffy });

		safeRun(() -> harold.setPets(safeList(new Pet[] { fluffy })));
		// safeRun(() -> gwen.setPets(safeList(new Pet[] { claws })));
	}
}
