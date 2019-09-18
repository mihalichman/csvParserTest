package com.mihalichman.testtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class ProductsCheapestSelector {

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private enum ParserTypes{simple, jackson}

    public static void main(String[] args) {

        if (argumentsConsoleCheck(args)){

            ProductsParser productsParser = new ProductsParserSimple();

            if (args[2].equals(ParserTypes.jackson.name())){
                productsParser = new ProductsParserMapper();
            }

            try {
                ProductsList productsList = new ProductsList();
                // set thread numbers if need
                if (argumentCheckThreadNumber(args)){
                    productsList.setThreadNumber(Integer.parseInt(args[1]));
                }
                productsList.setRowLimit(3);
                productsList.parseFileToList(args[0], productsParser);



                printResult(productsList.getCheapestProducts());

            } catch (Exception e) {
                System.out.println("Error parsing: " + e.getMessage() + "\n try again with right parameters" );
                e.printStackTrace();
            }
        }else{
            System.out.println(" use util like : \""+ (ProductsCheapestSelector.class.getSimpleName())
                    +"\" [path (file or directory)] [threads number] [optional:parser method (" + Arrays.toString(ParserTypes.values()) + ")] " );
        }
    }


    private static void printResult(List<Product> productList) throws IOException {
        if (productList.isEmpty()){
            System.out.println(" ->list is empty, nothing to do (directory not contain csv files) ");
        }else{

            System.out.println("# : id : Name : price");

            final Integer[] count = {0};

            int firstViewRowLimit = 50;
            productList.stream()
                    .limit(firstViewRowLimit)
                    .forEach(product -> {
                        count[0]++;
                        System.out.println(productStringView(count[0], product));
                    });

            if (productList.size() > firstViewRowLimit){
                System.out.print("List have more then 50 rows. View all? (y,n) : ");
                String s = br.readLine();
                if (s.equals("y")){
                    productList.stream()
                            .skip(firstViewRowLimit)
                            .forEach(product -> {
                                count[0]++;
                                System.out.println(productStringView(count[0], product));
                            });
                }
            }
        }
    }

    private static String productStringView(Integer count, Product product){
        return count + " : " + product.getId() + " : " + product.getName() + " : " + product.getPrice();
    }

    private static boolean argumentsConsoleCheck(String[] args){
        return args.length == 3;
    }

    private static boolean argumentCheckThreadNumber(String[] args){
        try{
            Integer.parseInt(args[1]);
            return true;
        } catch (NumberFormatException e){
            System.out.println(" ->Second parameter not number, thread will be default");
            return false;
        }
    }

}
