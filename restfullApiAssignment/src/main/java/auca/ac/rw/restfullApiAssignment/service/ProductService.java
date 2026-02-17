package auca.ac.rw.restfullApiAssignment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.rw.restfullApiAssignment.modal.ecommerce.Product;
import auca.ac.rw.restfullApiAssignment.repository.ProductRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    public String addNewProduct(Product product) {
        Optional<Product> existProduct = productRepository.findById(product.getId());
        if(existProduct.isPresent()) {
            return "Product with id " + product.getId() + " already exists";
        }
        productRepository.save(product);
        return "Product added successfully";
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public Product updateProduct(Long id, Product product) {
        Product existing = getProductById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());
        existing.setStockQuantity(product.getStockQuantity());
        existing.setBrand(product.getBrand());
        return productRepository.save(existing);
    }

    public String deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
            return "Product not found with id: " + id;
        }
        productRepository.deleteById(id);
        return "Product deleted successfully";
    }
public List<Product> searchByCategory(String categoryName){
        List<Product> products = productRepository.findByCategory(categoryName);

        if(products != null && !products.isEmpty()){
            return products;
        }else{
            return null;
        }
    }

    public List<Product> searchByPriceAndBrand(Double price, String brand){
        List<Product> products = productRepository.findByPriceAndBrand(price, brand);

        if(products != null && !products.isEmpty()){
            return products;
        }else{
            return null;
        }
    }
}