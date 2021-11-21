import model.Product;
import service.ReceiptService;

import java.util.Scanner;

public class GroceryStoreApplication {

    public static void main(String[] args) {

        ReceiptService receiptService = new ReceiptService();

        System.out.println("Hello, welcome to Charlene's Coffee Corner!");
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;
        while (!exit) {
            System.out.println("Please enter the products you wish to shop one by one and press enter after each:");
            String input = scanner.nextLine();
            Product product = receiptService.generateProductFromString(input);
            while (product == null) {
                System.out.println("We couldn't detect your product, please input a product again:");
                input = scanner.nextLine();
                product = receiptService.generateProductFromString(input);
            }
            System.out.println("Your product: " + product);
            receiptService.add(product);

            System.out.println("Would you like to add something else? Y/N");
            String endInput = scanner.nextLine();
            if (endInput.equalsIgnoreCase("N")) {
                exit = true;
                System.out.println("Your Product List:");
                for (Product prod : receiptService.getProducts()) {
                    System.out.println(prod.toString());
                }
                System.out.println("Your Receipt:");
                System.out.println(receiptService.generateReceipt());
            }
        }
    }
}
