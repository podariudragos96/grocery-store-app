package data;

public enum ProductTypesList {
    COFFE("Coffe"),
    JUICE("Juice"),
    SNACK("Snack");

    public final String typeName;

    ProductTypesList(String typeName) {
        this.typeName = typeName;
    }
}
