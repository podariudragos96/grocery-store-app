import data.ExtrasList;
import data.ProductsList;
import model.Product;
import org.junit.Assert;
import org.junit.Test;
import service.ReceiptService;

public class ReceiptServiceTest {

    @Test
    public void receiptTests() {
        ReceiptService receiptService = new ReceiptService();

        Product product1 = receiptService.generateProductFromString("5 small orange");
        Product product2 = receiptService.generateProductFromString("3 small extra milk");
        Product product3 = receiptService.generateProductFromString("1 lArGe special coffe");
        Product product4 = receiptService.generateProductFromString("2 abcdef ghi jkl");

        receiptService.add(product1);
        receiptService.add(product2);
        receiptService.add(product3);

        String receipt = receiptService.generateReceipt();

        Assert.assertEquals(product1.getName(), ProductsList.ORANGE_JUICE.itemName);
        Assert.assertEquals(product1.getPrice(), ProductsList.ORANGE_JUICE.itemPrice * 5, 0.0);
        Assert.assertEquals(product1.getQuantity(), 5);

        Assert.assertEquals(product2.getName(), ProductsList.SMALL_COFFE.itemName);
        Assert.assertEquals(product2.getPrice(), (ProductsList.SMALL_COFFE.itemPrice + ExtrasList.EXTRA_MILK.extraPrice) * 3, 0.0);
        Assert.assertEquals(product2.getQuantity(), 3);
        Assert.assertEquals(product2.toString(), 3 + " X " + ProductsList.SMALL_COFFE.itemName + " with " + ExtrasList.EXTRA_MILK.extraName);

        Assert.assertEquals(product3.getName(), ProductsList.LARGE_COFFE.itemName);
        Assert.assertEquals(product3.getPrice(), ProductsList.LARGE_COFFE.itemPrice + ExtrasList.SPECIAL_ROAST.extraPrice, 0.0);
        Assert.assertEquals(product3.getQuantity(), 1);
        Assert.assertEquals(product3.toString(), 1 + " X " + ProductsList.LARGE_COFFE.itemName + " with " + ExtrasList.SPECIAL_ROAST.extraName);

        Assert.assertNull(product4);

        Assert.assertEquals(receiptService.getProducts().size(), 3);
        Assert.assertEquals(receiptService.getProducts().get(0), product1);
        Assert.assertEquals(receiptService.getProducts().get(1), product2);
        Assert.assertEquals(receiptService.getProducts().get(2), product3);

        Assert.assertTrue(receipt.contains("QTY - PRODUCT - PRICE\n"));
        Assert.assertTrue(receipt.contains("5 - Orange Juice - 19.750"));
        Assert.assertTrue(receipt.contains("3 - Small Coffe with Extra Milk - 8.400"));
        Assert.assertTrue(receipt.contains("1 - Large Coffe with Special Roast - 4.400"));
        Assert.assertTrue(receipt.contains("Total: 30.050 CHF"));

    }
}
