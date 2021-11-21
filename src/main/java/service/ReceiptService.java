package service;

import data.ExtrasList;
import data.ProductTypesList;
import data.ProductsList;
import model.Product;
import model.ProductExtra;
import utils.StringUtils;

import java.text.DecimalFormat;
import java.util.*;

public class ReceiptService {

    private List<Product> products;
    private int freeBeverageCounter;
    private int freeExtraCounter;
    private static final DecimalFormat df = new DecimalFormat("0.000");


    public ReceiptService() {
        this.products = new ArrayList<>();
        this.freeBeverageCounter = 0;
        this.freeExtraCounter = 0;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> add(Product product) {

        for (Product pr : products) {
            if (pr.equals(product)) {
                pr.setQuantity(pr.getQuantity() + product.getQuantity());
                pr.setPrice(pr.getPrice() + product.getPrice());
                return this.products;
            }
        }

        this.products.add(product);
        return this.products;
    }

    public Product generateProductFromString(String input) {
        Product product = new Product();

        product.setQuantity(getQuantityFromString(input));
        product.setName(getProductNameFromString(input));
        if (product.getName() == null) {
            return null;
        }
        product.setType(getProductTypeFromProductName(product.getName()));
        if (product.getType().equals(ProductTypesList.COFFE.typeName)) {
            product.setExtras(getExtrasFromString(input));
        } else {
            product.setExtras(new ArrayList<>());
        }
        product.setPrice(generatePriceForProduct(product));

        return product;
    }

    private int getQuantityFromString(String input) {
        String quantityString = StringUtils.extractNumber(input);

        if (quantityString.isEmpty() || quantityString.equals("0")) {
            return 1;
        }

        return Integer.parseInt(quantityString);
    }

    private String getProductNameFromString(String productString) {
        for (ProductsList product : ProductsList.values()) {
            if (doesStringContainAllValuesFromList(productString, List.of(product.itemName.split(" ")))) {
                return product.itemName;
            }
            if (doesStringContainAtLeasOneValueFromList(productString, List.of(product.itemName.split(" ")))) {
                return product.itemName;
            }
        }
        return null;
    }

    private List<ProductExtra> getExtrasFromString(String input) {
        List<ProductExtra> extrasList = new ArrayList<>();

        if (doesStringContainAllValuesFromList(input, List.of(ExtrasList.EXTRA_MILK.extraName.split(" ")))) {
            extrasList.add(new ProductExtra(ExtrasList.EXTRA_MILK.extraName, ExtrasList.EXTRA_MILK.extraPrice));
        }

        if (doesStringContainAllValuesFromList(input, List.of(ExtrasList.FOAMED_MILK.extraName.split(" ")))) {
            extrasList.add(new ProductExtra(ExtrasList.FOAMED_MILK.extraName, ExtrasList.FOAMED_MILK.extraPrice));
        }

        if (doesStringContainAtLeasOneValueFromList(input, List.of(ExtrasList.SPECIAL_ROAST.extraName.split(" ")))) {
            extrasList.add(new ProductExtra(ExtrasList.SPECIAL_ROAST.extraName, ExtrasList.SPECIAL_ROAST.extraPrice));
        }

        return extrasList;
    }

    private boolean doesStringContainAtLeasOneValueFromList(String input, List<String> values) {
        for (String value : values) {
            // using toLowerCase to ignore case when giving input
            if (input.toLowerCase().contains(value.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean doesStringContainAllValuesFromList(String input, List<String> values) {
        for (String value : values) {
            if (!input.toLowerCase().contains(value.toLowerCase())) {
                return false;
            }
        }

        return true;
    }

    private String getProductTypeFromProductName(String productName) {
        if (productName == null) {
            return null;
        }

        productName = productName.toUpperCase();
        productName = productName.replaceAll(" ", "_");
        ProductsList product = ProductsList.valueOf(productName);

        if (product.itemName.equals(ProductsList.ORANGE_JUICE.itemName)) {
            return ProductTypesList.JUICE.typeName;
        }

        if (product.itemName.equals(ProductsList.BACON_ROLL.itemName)) {
            return ProductTypesList.SNACK.typeName;
        }

        return ProductTypesList.COFFE.typeName;
    }

    private double generatePriceForProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            return 0;
        }

        double price = 0;

        price = price + getPriceFromProductsListEnum(product.getName());

        for (ProductExtra extra : product.getExtras()) {
            price = price + getPriceFromExtrasListEnum(extra.getName());
        }

        return price * product.getQuantity();
    }

    private double getPriceFromProductsListEnum(String itemName) {
        Optional<ProductsList> first = Arrays.stream(ProductsList.values()).filter(p -> p.itemName.equals(itemName)).findFirst();
        return first.map(productsList -> productsList.itemPrice).orElse(0.0);
    }

    private double getPriceFromExtrasListEnum(String extraName) {
        Optional<ExtrasList> first = Arrays.stream(ExtrasList.values()).filter(e -> e.extraName.equals(extraName)).findFirst();
        return first.map(extrasList -> extrasList.extraPrice).orElse(0.0);
    }

    public String generateReceipt() {
        String receipt = "QTY - PRODUCT - PRICE\n";
        freeBeverageCounter = getFreeBeveragesCounter(this.products);
        freeExtraCounter = getFreeExtraCounter(this.products);

        double totalDiscountValue = getFreeBeveragesDiscount(this.products) + getFreeExtrasDiscount(this.products);

        for (Product product : this.products) {
            receipt = receipt + product.getQuantity() + " - " + product.getName();
            if (product.getExtras() != null && product.getExtras().size() > 0) {
                receipt = receipt + " with ";
                for (ProductExtra pe : product.getExtras()) {
                    receipt = receipt + pe.getName() + ", ";
                }
                receipt = receipt.substring(0, receipt.length() - 2);
            }
            receipt = receipt + " - " + df.format(product.getPrice()) + "\n";
        }

        double subTotalPrice = getTotalPrice(this.products);

        receipt = receipt + "..................................................\n";
        receipt = receipt + "Subtotal: " + df.format(subTotalPrice) + " CHF\n";
        receipt = receipt + "Discount: -" + df.format(totalDiscountValue) + " CHF\n";
        double totalPrice = subTotalPrice - totalDiscountValue;
        receipt = receipt + "Total: " + df.format(totalPrice) + " CHF";

        return receipt;
    }

    private double getTotalPrice(List<Product> products) {
        double totalPrice = 0;
        for (Product product : products) {
            totalPrice = totalPrice + product.getPrice();
        }
        return totalPrice;
    }

    private double getFreeBeveragesDiscount(List<Product> products) {
        double totalDiscount;
        HashMap<String, Integer> beveragesMap = new HashMap<>();
        beveragesMap.put(ProductsList.SMALL_COFFE.itemName, 0);
        beveragesMap.put(ProductsList.MEDIUM_COFFE.itemName, 0);
        beveragesMap.put(ProductsList.LARGE_COFFE.itemName, 0);
        beveragesMap.put(ProductsList.ORANGE_JUICE.itemName, 0);

        for (Product product : products) {
            if (product.getType().equals(ProductTypesList.COFFE.typeName) || product.getType().equals(ProductTypesList.JUICE.typeName)) {
                beveragesMap.put(product.getName(), beveragesMap.get(product.getName()) + product.getQuantity());
            }
        }

        int smallCoffesQuantity = beveragesMap.get(ProductsList.SMALL_COFFE.itemName);
        int mediumCoffesQuantity = beveragesMap.get(ProductsList.MEDIUM_COFFE.itemName);
        int largeCoffesQuantity = beveragesMap.get(ProductsList.LARGE_COFFE.itemName);
        int juiceQuantity = beveragesMap.get(ProductsList.ORANGE_JUICE.itemName);

        if (freeBeverageCounter > smallCoffesQuantity) {
            totalDiscount = smallCoffesQuantity * ProductsList.SMALL_COFFE.itemPrice;
            freeBeverageCounter = freeBeverageCounter - smallCoffesQuantity;
        } else {
            totalDiscount = freeBeverageCounter * ProductsList.SMALL_COFFE.itemPrice;
            freeBeverageCounter = 0;
            return totalDiscount;
        }

        if (freeBeverageCounter > mediumCoffesQuantity) {
            totalDiscount = totalDiscount + (mediumCoffesQuantity * ProductsList.MEDIUM_COFFE.itemPrice);
            freeBeverageCounter = freeBeverageCounter - mediumCoffesQuantity;
        } else {
            totalDiscount = totalDiscount + (freeBeverageCounter * ProductsList.MEDIUM_COFFE.itemPrice);
            freeBeverageCounter = 0;
            return totalDiscount;
        }

        if (freeBeverageCounter > largeCoffesQuantity) {
            totalDiscount = totalDiscount + (largeCoffesQuantity * ProductsList.LARGE_COFFE.itemPrice);
            freeBeverageCounter = freeBeverageCounter - largeCoffesQuantity;
        } else {
            totalDiscount = totalDiscount + (freeBeverageCounter * ProductsList.LARGE_COFFE.itemPrice);
            freeBeverageCounter = 0;
            return totalDiscount;
        }

        if (freeBeverageCounter > juiceQuantity) {
            totalDiscount = totalDiscount + (juiceQuantity * ProductsList.ORANGE_JUICE.itemPrice);
            freeBeverageCounter = freeBeverageCounter - juiceQuantity;
        } else {
            totalDiscount = totalDiscount + (freeBeverageCounter * ProductsList.ORANGE_JUICE.itemPrice);
            freeBeverageCounter = 0;
            return totalDiscount;
        }

        return totalDiscount;
    }

    private double getFreeExtrasDiscount(List<Product> products) {
        double totalDiscount;
        HashMap<String, Integer> extrasMap = new HashMap<>();

        extrasMap.put(ExtrasList.EXTRA_MILK.extraName, 0);
        extrasMap.put(ExtrasList.FOAMED_MILK.extraName, 0);
        extrasMap.put(ExtrasList.SPECIAL_ROAST.extraName, 0);

        for (Product product : products) {
            for (ProductExtra extra : product.getExtras()) {
                extrasMap.put(extra.getName(), extrasMap.get(extra.getName()) + product.getQuantity());
            }
        }

        int extraMilkQuantity = extrasMap.get(ExtrasList.EXTRA_MILK.extraName);
        int foamedMilkQuantity = extrasMap.get(ExtrasList.FOAMED_MILK.extraName);
        int specialRoastQuantity = extrasMap.get(ExtrasList.SPECIAL_ROAST.extraName);

        if (freeExtraCounter > extraMilkQuantity) {
            totalDiscount = extraMilkQuantity * ExtrasList.EXTRA_MILK.extraPrice;
            freeExtraCounter = freeExtraCounter - extraMilkQuantity;
        } else {
            totalDiscount = freeExtraCounter * ExtrasList.EXTRA_MILK.extraPrice;
            freeExtraCounter = 0;
            return totalDiscount;
        }

        if (freeExtraCounter > foamedMilkQuantity) {
            totalDiscount = totalDiscount + (foamedMilkQuantity * ExtrasList.FOAMED_MILK.extraPrice);
            freeExtraCounter = freeExtraCounter - foamedMilkQuantity;
        } else {
            totalDiscount = totalDiscount + (freeExtraCounter * ExtrasList.FOAMED_MILK.extraPrice);
            freeExtraCounter = 0;
            return totalDiscount;
        }

        if (freeExtraCounter > specialRoastQuantity) {
            totalDiscount = totalDiscount + (specialRoastQuantity * ExtrasList.FOAMED_MILK.extraPrice);
            freeExtraCounter = freeExtraCounter - specialRoastQuantity;
        } else {
            totalDiscount = totalDiscount + (freeExtraCounter * ExtrasList.FOAMED_MILK.extraPrice);
            freeExtraCounter = 0;
            return totalDiscount;
        }

        return totalDiscount;
    }

    private int getFreeBeveragesCounter(List<Product> products) {
        int counter = 0;

        for (Product product : products) {
            if (product.getType().equals(ProductTypesList.COFFE.typeName)
                    || product.getType().equals(ProductTypesList.JUICE.typeName)) {
                counter = counter + product.getQuantity();
            }
        }

        return counter / 5;
    }

    private int getFreeExtraCounter(List<Product> products) {
        int beveragesCounter = 0;
        int snacksCounter = 0;

        for (Product product : products) {
            if (product.getType().equals(ProductTypesList.COFFE.typeName)
                    || product.getType().equals(ProductTypesList.JUICE.typeName)) {
                beveragesCounter = beveragesCounter + product.getQuantity();
            } else {
                snacksCounter = snacksCounter + product.getQuantity();
            }
        }

        return Math.min(beveragesCounter, snacksCounter);
    }
}
