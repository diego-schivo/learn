package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.safeList;

import java.util.List;

import com.backflipsource.servlet.StringConverter;
import com.backflipsource.servlet.View;

@View
public class Owner {

	@View.Field(identifier = true, converter = StringConverter.ForInteger.class)
	private Integer id;

	@View.Field
	private String firstName;

	@View.Field
	private String lastName;

	@View.Field
	private String address;

	@View.Field
	private String city;

	@View.Field
	private String telephone;

	// @View.Field(converter = PetStringConverter.class)
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

	public static List<Owner> getList() {
		return list;
	}

	public static void setList(List<Owner> list) {
		Owner.list = list;
	}

	public static List<Owner> list;

	static {
		Owner george = new Owner(1, "George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023", null);
		list = safeList(new Owner[] { george });
	}
}
