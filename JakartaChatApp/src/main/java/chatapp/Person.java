package chatapp;

import java.io.Serializable;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Named("person")
@ApplicationScoped
public class Person implements Serializable{
	
	@NotEmpty
	@Size(max = 16, message = "Name length must be less than {max} symbols")
	private String name;
	
	public Person() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
