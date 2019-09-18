package com.mihalichman.testtask;

import java.io.*;
import java.util.*;

interface ProductsParser {
    ProductsParser parseFile(File file);
    List<Product> getProductList();
}
