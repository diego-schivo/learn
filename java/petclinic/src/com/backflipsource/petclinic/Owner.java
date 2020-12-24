package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.safeStream;
import static com.backflipsource.Helpers.safeString;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.backflipsource.entity.annotation.Render;
import com.backflipsource.servlet.EntityData;
import com.backflipsource.servlet.StringConverter.ForInteger;
import com.backflipsource.servlet.View;

@View(uri = "/owners")
@Render(view = View.Show.class, controlPage = "/owner/description-list.jsp", presentation = Owner2.class)
public class Owner {

	@View.Field(identifier = true, view = View.List.class, converter = ForInteger.class)
	@Render(view = View.List.class, controlPage = "/owner-id-anchor.jsp")
	private Integer id;

	@View.Field(view = { View.Show.class, View.Edit.class })
	private String firstName;

	@View.Field(view = { View.Show.class, View.Edit.class })
	private String lastName;

	@View.Field
	private String address;

	@View.Field
	private String city;

	@View.Field
	private String telephone;

	@View.Field(view = View.Show.class, converter = PetStringConverter.class)
	@Render(view = View.Show.class, controlPage = "/owner-pets-table.jsp")
	private List<Pet> pets;

	public Owner() {
	}

	public Owner(Integer id, String firstName, String lastName, String address, String city, String telephone,
			List<Pet> pets) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.telephone = telephone;
		this.pets = pets;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public static EntityData<Owner> data = new Data();

	protected static class Data implements EntityData<Owner> {

		protected List<Owner> list;

		protected Integer nextId;

		protected Data() {
			Owner george = new Owner(1, "George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023", null);
			Owner betty = new Owner(2, "Betty", "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749", null);
			Owner eduardo = new Owner(3, "Eduardo", "Rodriquez", "2693 Commerce St.", "McFarland", "6085558763", null);
			Owner harold = new Owner(4, "Harold", "Davis", "563 Friendly St.", "Windsor", "6085553198", null);
			Owner peter = new Owner(5, "Peter", "McTavish", "2387 S. Fair Way", "Madison", "6085552765", null);
			Owner jean = new Owner(6, "Jean", "Coleman", "105 N. Lake St.", "Monona", "6085552654", null);
			Owner jeff = new Owner(7, "Jeff", "Black", "1450 Oak Blvd.", "Monona", "6085555387", null);
			Owner maria = new Owner(8, "Maria", "Escobito", "345 Maple St.", "Madison", "6085557683", null);
			Owner david = new Owner(9, "David", "Schroeder", "2749 Blackhawk Trail", "Madison", "6085559435", null);
			Owner carlos = new Owner(10, "Carlos", "Estaban", "2335 Independence La.", "Waunakee", "6085555487", null);
			list = safeStream(new Owner[] { george, betty, eduardo, harold, peter, jean, jeff, maria, david, carlos })
					.collect(toList());

			nextId = 11;
		}

		@Override
		public List<Owner> list() {
			return list;
		}

		@Override
		public void save(Owner t) {
			if (t.getId() != null) {
				return;
			}
			t.setId(nextId++);
			list.add(t);
		}

		public List<Owner> find(String lastName) {
			String prefix = safeString(lastName).toLowerCase();
			return safeStream(list).filter(item -> safeString(item.getLastName()).toLowerCase().startsWith(prefix))
					.collect(toList());
		}
	}
}
