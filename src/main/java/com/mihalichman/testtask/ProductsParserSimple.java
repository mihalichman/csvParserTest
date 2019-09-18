package com.mihalichman.testtask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductsParserSimple implements ProductsParser{

    private List<Product> productList;

    public ProductsParserSimple(){}

    private ProductsParserSimple(File csvFile){
        BufferedReader br;
        String line;
        String csvDecision = ",";

        List<Product> productList = new ArrayList<>();

        try {

            br = new BufferedReader(new FileReader(csvFile));

            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber >1){
                    // separate line by decision
                    String[] productStringArr = line.split(csvDecision);

                    // check valid line
                    if (productStringArr.length == 5){
                        Integer id = Integer.parseInt(productStringArr[0]);
                        float  price = Float.parseFloat(productStringArr[4]);
                        Product product = new Product(id, productStringArr[1], productStringArr[2], productStringArr[3], price);

                        productList.add(product);
                    }
                }
            }

        }catch (NumberFormatException e){
            System.out.println("some line format error in file : " + csvFile.getName()
                    + "\n " + e.getMessage());

        } catch (IOException e) {
            System.out.println(" reade file error or wrong row in file : " + csvFile.getName()
                    + "\n " + e.getMessage());
        }
        this.productList = productList;
    }

    public ProductsParserSimple parseFile(File file){
        return new ProductsParserSimple(file);
    }

    public List<Product> getProductList() {
        return productList;
    }
}
