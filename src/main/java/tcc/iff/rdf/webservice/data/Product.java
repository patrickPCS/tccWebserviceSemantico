package tcc.iff.rdf.webservice.data;

public class Product {
	
	private String Name;
	private String Description;
	private String Category;
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getCategory() {
		return Category;
	}
	public void setCategory(String category) {
		Category = category;
	}
	@Override
	public String toString() {
		return "Product [Name=" + Name + ", Description=" + Description + ", Category=" + Category + "]";
	}

}
