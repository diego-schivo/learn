package com.backflipsource.petclinic;

import static com.backflipsource.Helpers.substringAfterLast;
import static com.backflipsource.Helpers.substringBeforeFirst;

import com.backflipsource.Converter;
import com.backflipsource.servlet.View;

// @Presentation
public class Owner2 {

	// @Presentation.Field(converter = Name.class)
	@View.Field(converter2 = Name.class)
	private String name;

	// @Presentation.Field
	@View.Field
	private String address;

	// @Presentation.Field
	@View.Field
	private String city;

	// @Presentation.Field
	@View.Field
	private String telephone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public static class Name implements Converter<Owner, Owner2> {

		@Override
		public void convertModel(Owner source, Owner2 target) {
			target.setName(source.getFirstName() + " " + source.getLastName());
		}

		@Override
		public void convertPresentation(Owner2 source, Owner target) {
			target.setFirstName(substringBeforeFirst(source.getName(), " "));
			target.setLastName(substringAfterLast(source.getName(), " "));
		}
	}
}
