package org.example.largent.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.largent.API.ApiResponse;
import org.example.largent.Model.Product;
import org.example.largent.Service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/get")
    public ResponseEntity getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/add")
    public ResponseEntity addProduct(@RequestBody @Valid Product product, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }

        productService.addProduct(product);
        return ResponseEntity.ok().body(new ApiResponse("Product has been added"));
    }


    @PutMapping("/update/{id}")
    public ResponseEntity updatePurchase(@PathVariable Integer id, @RequestBody @Valid Product product, Errors errors){
        if (errors.hasErrors()){
            String message=errors.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        productService.updateProduct(id, product);
        return ResponseEntity.ok().body(new ApiResponse("Product has been updated"));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteProduct(@PathVariable Integer id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().body(new ApiResponse("Product has been deleted"));

    }


    //                    EXTRA
    @PostMapping("/buy-product/{userId}/{productId}/{amount}")
    public ResponseEntity buyProduct(@PathVariable Integer userId, @PathVariable Integer productId, @PathVariable Integer amount){
        productService.buyProduct(userId, productId, amount);
        return ResponseEntity.ok().body(new ApiResponse("Thank you for your purchase"));

    }

}
