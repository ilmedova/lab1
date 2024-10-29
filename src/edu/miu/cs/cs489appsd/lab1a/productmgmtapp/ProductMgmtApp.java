package cs.cs489appsd.lab1a.productmgmtapp;

import cs.cs489appsd.lab1a.productmgmtapp.model.Product;

import java.util.Arrays;
import java.util.Comparator;

public class ProductMgmtApp {
    public static void main(String[] args) {
        // Array of products based on the company's data
        Product[] products = {
                new Product(3128874119L, "Banana", "2023-01-24", 124, 0.55),
                new Product(2927458265L, "Apple", "2022-12-09", 18, 1.09),
                new Product(9189927460L, "Carrot", "2023-03-31", 89, 2.99)
        };

        // Sort products by name
        Arrays.sort(products, Comparator.comparing(Product::getName));

        // Print products in different formats
        printProducts(products);
    }

    public static void printProducts(Product[] products) {
        // JSON format
        System.out.println("JSON-formatted list of all Products:");
        System.out.println("[");
        for (int i = 0; i < products.length; i++) {
            Product p = products[i];
            System.out.printf("  {\n    \"productId\": %d,\n    \"name\": \"%s\",\n    \"dateSupplied\": \"%s\",\n    \"quantityInStock\": %d,\n    \"unitPrice\": %.2f\n  }",
                    p.getProductId(), p.getName(), p.getDateSupplied(), p.getQuantityInStock(), p.getUnitPrice());
            if (i < products.length - 1) System.out.println(",");
        }
        System.out.println("\n]");

        // XML format
        System.out.println("\nXML-formatted list of all Products:");
        System.out.println("<Products>");
        for (Product p : products) {
            System.out.printf("  <Product>\n    <productId>%d</productId>\n    <name>%s</name>\n    <dateSupplied>%s</dateSupplied>\n    <quantityInStock>%d</quantityInStock>\n    <unitPrice>%.2f</unitPrice>\n  </Product>\n",
                    p.getProductId(), p.getName(), p.getDateSupplied(), p.getQuantityInStock(), p.getUnitPrice());
        }
        System.out.println("</Products>");

        // CSV format
        System.out.println("\nComma-Separated Values (CSV)-formatted list of all Products:");
        System.out.println("productId,name,dateSupplied,quantityInStock,unitPrice");
        for (Product p : products) {
            System.out.printf("%d,%s,%s,%d,%.2f\n", p.getProductId(), p.getName(), p.getDateSupplied(), p.getQuantityInStock(), p.getUnitPrice());
        }
    }
}
