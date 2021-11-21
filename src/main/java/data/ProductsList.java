package data;

public enum ProductsList {
    BACON_ROLL("Bacon Roll", 4.5),
    LARGE_COFFE("Large Coffe", 3.5),
    MEDIUM_COFFE("Medium Coffe", 3.0),
    ORANGE_JUICE("Orange Juice", 3.95),
    SMALL_COFFE("Small Coffe", 2.5);


    public final String itemName;
    public final double itemPrice;

    ProductsList(String itemName, double itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }
}
