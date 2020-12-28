package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.safeGet;
import static com.backflipsource.Helpers.safeList;
import static com.backflipsource.Helpers.safeRun;
import static com.backflipsource.Helpers.safeStream;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.backflipsource.ui.Entity;
import com.backflipsource.ui.StringConverter.ForInteger;
import com.backflipsource.ui.StringConverter.ForLocalDate;

@Entity(uri = "/visits")
public class Visit {

	@Entity.Field(identifier = true, converter = ForInteger.class)
	private Integer id;

	@Entity.Field(converter = ForLocalDate.class)
	private LocalDate date;

	@Entity.Field
	private String description;

	@Entity.Field(converter = PetStringConverter.class)
	private Pet pet;

	public Visit() {
	}

	public Visit(Integer id, LocalDate date, String description, Pet pet) {
		super();
		this.id = id;
		this.date = date;
		this.description = description;
		this.pet = pet;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public static List<Visit> list;

	static {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		Pet samantha = safeGet(() -> Pet.data.list().get(1));
		Pet max = safeGet(() -> Pet.data.list().get(2));

		Visit visit1 = new Visit(1, LocalDate.parse("2013-01-01", formatter), "rabies shot", samantha);
		Visit visit2 = new Visit(2, LocalDate.parse("2013-01-02", formatter), "rabies shot", max);
		Visit visit3 = new Visit(3, LocalDate.parse("2013-01-03", formatter), "neutered", max);
		Visit visit4 = new Visit(4, LocalDate.parse("2013-01-04", formatter), "spayed", max);
		list = safeStream(new Visit[] { visit1, visit2, visit3, visit4 }).collect(toList());

		safeRun(() -> samantha.setVisits(safeList(new Visit[] { visit1, visit4 })));
		safeRun(() -> max.setVisits(safeList(new Visit[] { visit2, visit3 })));
	}
}
