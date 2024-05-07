package org.example.largent.Service;

import lombok.RequiredArgsConstructor;
import org.example.largent.API.ApiException;
import org.example.largent.Model.Product;
import org.example.largent.Model.PurchaseHistory;
import org.example.largent.Model.User;
import org.example.largent.Repository.ProductRepository;
import org.example.largent.Repository.PurchaseHistoryRepository;
import org.example.largent.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository products;
    private final UserRepository userRepository;
    private final PurchaseHistoryService purchaseHistoryService;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public List<Product> getAllProducts(){
        return products.findAll();
    }

    public void addProduct(Product product){
        products.save(product);
    }

    public void updateProduct(Integer id, Product product){
        Product existingProduct = products.findProductByProductId(id);
        if (existingProduct == null) {
            throw new ApiException("ID not found");
        }

        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setStoreName(product.getStoreName());
        products.save(existingProduct);
    }

    public void deleteProduct(Integer id){
        Product product = products.findProductByProductId(id);
        if (product == null){
            throw new ApiException("ID cannot be found");
        }
        products.delete(product);
    }

    public void buyProduct(Integer userId, Integer productId, Integer amount) {
        User user = userRepository.findUserByUserId(userId);
        if (user == null || !user.getIsLogin()) {
            throw new ApiException("User not found or user not logged in");
        }

        Product product = products.findProductByProductId(productId);
        if (product == null) {
            throw new ApiException("Product not found");
        }

        double productTotal = product.getPrice() * amount;
        if (user.getBalance() < productTotal) {
            throw new ApiException("Insufficient balance");
        }

        user.setBalance(user.getBalance() - productTotal);
        addPointsUser(user, productTotal);

        product.setPurchaseId(product.getProductId());
        product.setTotal(productTotal);

        userRepository.save(user);
        products.save(product);

        PurchaseHistory purchaseHistory = new PurchaseHistory();
        purchaseHistory.setUserId(userId);
        purchaseHistory.setCategory(product.getCategory());
        purchaseHistory.setTotalAmount(productTotal);
        purchaseHistory.setPurchaseDate(LocalDate.now());
        purchaseHistoryService.addPurchHistory(purchaseHistory);
    }


    private void addPointsUser(User user, double productTotal) {
        int addPoints = (int) (productTotal / 10);
        if (user.getPoints() == null) {
            user.setPoints(addPoints);
        } else {
            user.setPoints(user.getPoints() + addPoints);
        }
        userRepository.save(user);
    }

}

