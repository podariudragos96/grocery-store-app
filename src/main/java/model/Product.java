package model;

import utils.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Product
{
    private int quantity;
    private String name;
    private String type;
    private List<ProductExtra> extras;
    private double price;

    public Product() {}

    public Product(int quantity, String name, String type, List<ProductExtra> extras, double price) {
        this.quantity = quantity;
        this.name = name;
        this.type = type;
        this.extras = extras;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ProductExtra> getExtras() {
        return extras;
    }

    public void setExtras(List<ProductExtra> extras) {
        this.extras = extras;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        String productString = quantity + " X " + name;
        if (extras != null && extras.size() > 0) {
            productString = productString + " with ";
            for (ProductExtra extra: extras) {
                productString = productString + extra.getName() + ", ";
            }
            productString = productString.substring(0, productString.length() - 2);
        }
        return productString;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Product other)) {
            return false;
        }

        List<String> thisExtras = this.extras.stream().map(ProductExtra::getName).collect(Collectors.toList());
        List<String> otherExtras = other.extras.stream().map(ProductExtra::getName).collect(Collectors.toList());

        return this.name.equals(other.name) && StringUtils.doStringListsMatch(thisExtras, otherExtras);
    }

}
