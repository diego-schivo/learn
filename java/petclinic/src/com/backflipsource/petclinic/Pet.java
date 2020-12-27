package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeRun;
import static com.backflipsource.Helpers.safeStream;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.backflipsource.servlet.EntityData;
import com.backflipsource.servlet.StringConverter.ForInteger;
import com.backflipsource.servlet.StringConverter.ForLocalDate;
import com.backflipsource.ui.Entity;
import com.backflipsource.ui.EntityForm;
import com.backflipsource.ui.Render;
import com.backflipsource.ui.Select;
import com.backflipsource.ui.Select.Options;

@Entity(uri = "/pets")
public class Pet {

	@Entity.Field(identifier = true, converter = ForInteger.class)
	private Integer id;

	@Entity.Field
	@Render
	private String name;

	@Entity.Field(converter = ForLocalDate.class)
	@Render
	private LocalDate birthDate;

	@Entity.Field
	// @Render(mode = EntityForm.class, controlFactory = Select.Factory.class)
	@Render
	@Options({ "cat", "dog", "lizard", "snake", "bird", "hamster" })
	private String type;

	@Entity.Field(converter = OwnerStringConverter.class)
	private Owner owner;

	@Entity.Field(converter = VisitStringConverter.class)
	// @Render(controlPage = "/pet-visits-table.jsp")
	private List<Visit> visits;

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

	public List<Visit> getVisits() {
		return visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}

	public static EntityData<Pet> data = new Data();

	protected static class Data implements EntityData<Pet> {

		protected List<Pet> list;

		protected Integer nextId;

		protected Data() {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			Owner george = safeGet(() -> Owner.data.list().get(0));
			Owner jean = safeGet(() -> Owner.data.list().get(5));

			Pet leo = new Pet(1, "Leo", LocalDate.parse("2010-09-07", formatter), "cat", george);
			Pet samantha = new Pet(7, "Samantha", LocalDate.parse("2012-09-04", formatter), "cat", jean);
			Pet max = new Pet(8, "Max", LocalDate.parse("2012-09-04", formatter), "cat", jean);
			// list = safeList(new Pet[] { leo, samantha, max });
			list = safeStream(new Pet[] { leo, samantha, max }).collect(toList());

			safeRun(() -> george.setPets(safeList(new Pet[] { leo })));
			safeRun(() -> jean.setPets(safeList(new Pet[] { samantha, max })));
		}

		@Override
		public List<Pet> list() {
			return list;
		}

		@Override
		public void save(Pet t) {
			if (t.getId() != null) {
				return;
			}
			t.setId(nextId++);
			list.add(t);
		}
	}
}
