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

@View(uri = "/pets")
public class Pet {

	@View.Field(identifier = true, converter = StringConverter.ForInteger.class)
	private Integer id;

	@View.Field
	private String name;

	@View.Field(converter = StringConverter.ForLocalDate.class)
	private LocalDate birthDate;

	@View.Field
	@View.Field(view = View.Edit.class, controlFactory = Select.Factory.class)
	@Options({ "cat", "dog", "lizard", "snake", "bird", "hamster" })
	private String type;

	private Owner owner;

	public Pet() {
	}

	public Pet(Integer id, String name, LocalDate birthDate, String type, Owner owner) {
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.type = type;
		this.owner = owner;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public static List<Pet> list;

	static {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		Owner george = safeGet(() -> Owner.list.get(0));
		Owner jean = safeGet(() -> Owner.list.get(5));

		Pet leo = new Pet(1, "Leo", LocalDate.parse("2010-09-07", formatter), "cat", george);
		Pet samantha = new Pet(7, "Samantha", LocalDate.parse("2012-09-04", formatter), "cat", jean);
		Pet max = new Pet(8, "Max", LocalDate.parse("2012-09-04", formatter), "cat", jean);
		list = safeList(new Pet[] { leo, samantha, max });

		safeRun(() -> george.setPets(safeList(new Pet[] { leo })));
		safeRun(() -> jean.setPets(safeList(new Pet[] { samantha, max })));
	}
}
