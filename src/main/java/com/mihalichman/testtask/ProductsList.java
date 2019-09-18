package com.mihalichman.testtask;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class ProductsList {
    private Integer threadNumberDefault = 20;
    private Integer sameNumber = 20;
    private Integer rowLimit = 1000;
    private List<Product> productList = Collections.synchronizedList(new ArrayList<>());
    private ThreadPoolExecutor executors = new ThreadPoolExecutor(threadNumberDefault, threadNumberDefault,
                                      10L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    /** Main method for parsing csv path to List of Products
     * @param csvPath path to csv file or directory with files;
     * @return List of Products
     * */
    public void parseFileToList(String csvPath, ProductsParser parser) throws Exception {

        File f = new File(csvPath);

        if (f.isFile() && isCsvFile(f)){
            productList =  parser
                    .parseFile(f)
                    .getProductList();
        }else if (f.isDirectory()){
            // get only csv files from directory
            List<File> csvFiles = Arrays.stream(Objects.requireNonNull(f.listFiles()))
                    .filter(this::isCsvFile)
                    .collect(Collectors.toList());

            AtomicReference<Integer> count = new AtomicReference<>(0);

            for(File file:csvFiles){
                executors.submit(() -> {
                    productList.addAll(parser.parseFile(file).getProductList());

                     count.getAndSet(count.get() + 1);
                     System.out.println("read file : " + file.getName() + "["+count.get()+"/"+csvFiles.size()+"]");
                });
            }

            while (executors.getActiveCount() >0) {
                printProgress(Math.toIntExact(executors.getTaskCount()), executors.getActiveCount());
            }
            executors.shutdown();
            executors.shutdownNow();
            System.out.println("\n Done!");
        }else{
            throw  new Exception("Wrong path");
        }
    }

    /** Get limited by count and by same count*/
    private List<Product> getCheapestProducts(Integer rowNumbers){

        List<Product> productListLimited = new ArrayList<>();

        try{
            productList.parallelStream()
                    .sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toList())
                    .forEach(product -> {
                        if (productListLimited.stream()
                                .filter(productCheckCount -> productCheckCount.getId().equals(product.getId())).count() < sameNumber){
                            productListLimited.add(product);
                        }
                        if (productListLimited.size() >= rowNumbers)
                            throw new RuntimeException();
                    });
        }catch (RuntimeException ignored){ }
        return productListLimited;
    }

    private void printProgress(Integer taskCount, Integer activeTask){
//        System.out.print("\r process : [");
//        for(int i =0; i < taskCount - activeTask; i++){
//            System.out.print( "=" );
//        }
//        System.out.print(" ]");
    }



    /** Get product list by default parameters */
    List<Product> getCheapestProducts(){
        return getCheapestProducts(rowLimit);
    }


    void setThreadNumber(Integer threadNumber) {
        executors = new ThreadPoolExecutor(threadNumber, threadNumber,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    public void setSameNumber(Integer sameNumber){
        this.sameNumber = sameNumber;
    }

    private void setRowLimit(Integer rowLimit){
        this.rowLimit = rowLimit;
    }

    /** simple check about csv files */
    private boolean isCsvFile(File file){
        return file.getName().split("\\.")[file.getName().split("\\.").length - 1].equals("csv");
    }


}
