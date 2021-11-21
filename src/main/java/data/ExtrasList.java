package data;

public enum ExtrasList {
    EXTRA_MILK("Extra Milk", 0.3),
    FOAMED_MILK("Foamed Milk", 0.5),
    SPECIAL_ROAST("Special Roast", 0.9);

    public final String extraName;
    public final double extraPrice;

    ExtrasList(String extraName, double extraPrice) {
        this.extraName = extraName;
        this.extraPrice = extraPrice;
    }
}
