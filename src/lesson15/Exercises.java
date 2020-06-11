package lesson15;

public class Exercises {
	public static final Human[]		list = {
											new Human("ivanov","ivan","ivanovich"),
											new Human("petrova","darya","michailovna"),
											new Human("sidorov","polycarp","amvrosievich")
										};
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Print all women using streams. Woman is a human with family ended with 'a'
	}

}

class Human {
	final String	family;
	final String	name;
	final String	patroName;
	
	public Human(String family, String name, String patroName) {
		super();
		this.family = family;
		this.name = name;
		this.patroName = patroName;
	}

	@Override
	public String toString() {
		return "Human [family=" + family + ", name=" + name + ", patroName=" + patroName + "]";
	}
}