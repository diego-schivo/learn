package com.backflipsource.menagerie;

import static com.backflipsource.Helpers.safeList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.backflipsource.ui.Entity;
import com.backflipsource.ui.Render;
import com.backflipsource.ui.Select;
import com.backflipsource.ui.StringConverter;
import com.backflipsource.ui.Select.Options;

@Entity
public class Pet {

	private String name;

	private String owner;

	@Entity.Field
	@Render(controlFactory = Select.Factory.class)
	@Options({ "bird", "cat", "dog", "hamster", "snake" })
	private String species;

	@Entity.Field
	@Render(controlFactory = Select.Factory.class)
	@Options({ "f", "m" })
	private String sex;

	@Entity.Field(converter = StringConverter.ForLocalDate.class)
	private LocalDate birth;

	@Entity.Field(converter = StringConverter.ForLocalDate.class)
	private LocalDate death;

	public Pet() {
	}

	public Pet(String name, String owner, String species, String sex, LocalDate birth, LocalDate death) {
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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
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

		Pet fluffy = new Pet("Fluffy", "Harold", "cat", "f", LocalDate.parse("1993-02-04", formatter), null);
		Pet claws = new Pet("Claws", "Gwen", "cat", "m", LocalDate.parse("1994-03-17", formatter), null);
		Pet buffy = new Pet("Buffy", "Harold", "dog", "f", LocalDate.parse("1989-05-13", formatter), null);
		Pet fang = new Pet("Fang", "Benny", "dog", "m", LocalDate.parse("1990-08-27", formatter), null);
		Pet bowser = new Pet("Bowser", "Diane", "dog", "m", LocalDate.parse("1979-08-31", formatter),
				LocalDate.parse("1995-07-29", formatter));
		Pet chirpy = new Pet("Chirpy", "Gwen", "bird", "f", LocalDate.parse("1998-09-11", formatter), null);
		Pet whistler = new Pet("Whistler", "Gwen", "bird", null, LocalDate.parse("1997-12-09", formatter), null);
		Pet slim = new Pet("Slim", "Benny", "snake", "m", LocalDate.parse("1996-04-29", formatter), null);
		Pet puffball = new Pet("Puffball", "Diane", "hamster", "f", LocalDate.parse("1999-03-30", formatter), null);
		list = safeList(new Pet[] { fluffy, claws, buffy, fang, bowser, chirpy, whistler, slim, puffball });
	}
}
