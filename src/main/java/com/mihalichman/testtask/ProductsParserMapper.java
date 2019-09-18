package com.mihalichman.testtask;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsParserMapper implements ProductsParser{
    private List<Product> productList;

    public ProductsParserMapper(){}

    /** Csv parser with using Jackson library
     * @param file csv file
     * */
    private ProductsParserMapper(File file) {
        try {

            ArrayList<Product> products = new ArrayList<>();

            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();

            ObjectReader oReader = csvMapper.readerFor(Product.class).with(schema);
            try (Reader reader = new FileReader(file)) {
                MappingIterator<Product> mi = oReader.readValues(reader);
                while (mi.hasNext()) {
                    Product current = mi.next();
                    products.add(current);
                }
            }


            System.out.println("");

            this.productList = products;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ProductsParser parseFile(File file) {
        return  new ProductsParserMapper(file);
    }


    public List<Product> getProductList() {
        return this.productList;
    }
}
