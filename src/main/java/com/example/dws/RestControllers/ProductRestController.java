package com.example.dws.RestControllers;

import com.example.dws.DTOs.GeneralMapper;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.Entities.Product;
import com.example.dws.Service.ProductService;
import com.example.dws.Service.ShopService;
import com.example.dws.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserService userService;
    @Autowired
    private GeneralMapper generalMapper;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productDTOS = productService.findAll();
        return ResponseEntity.ok(productDTOS);
    }

    // GET a product by ID
    @GetMapping("/{productID}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable long productID) {
        Optional<ProductDTO> productDTO = productService.findById(productID);
        if (productDTO.isPresent()) {
            return ResponseEntity.ok(productDTO.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
    }

    // POST create new product (standalone, no shop association)

    // PUT update existing product
    @PutMapping("/{productID}")
    public ResponseEntity<String> updateProduct(@PathVariable long productID, @RequestBody ProductDTO productDTO) {
        Optional<ProductDTO> existing = productService.findById(productID);
        if (existing.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
        if (productDTO.productName() == null || productDTO.productName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío");
        }
        if (productDTO.productPrize() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio del producto no puede ser nulo");
        }
        productService.update(productID, productDTO);
        return ResponseEntity.ok("Producto actualizado correctamente");
    }

    // DELETE product
    @DeleteMapping("/{productID}")
    public ResponseEntity<String> deleteProduct(@PathVariable long productID) {
        Optional<ProductDTO> productDTO = productService.findById(productID);
        if (productDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
        productService.deleteById(productID);
        return ResponseEntity.ok("Producto eliminado correctamente");
    }

    // POST add shop to product
    @PostMapping("/products/{productID}/shops/{shopID}")
    public ResponseEntity<String> addShopToProduct(
            @PathVariable long productID,
            @PathVariable long shopID
    ) {
        Optional<ProductDTO> productDTO = productService.findById(productID);
        if (productDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
        Optional<ShopDTO> shopDTO = shopService.findById(shopID);
        if (shopDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        productService.saveProductInShop(productDTO.get(), shopDTO.get());
        return ResponseEntity.status(HttpStatus.CREATED).body("Tienda añadida al producto correctamente");
    }


    // POST buy product (adds to logged‐in user's cart)
    @PostMapping("/users/me/products/{productID}")
    public ResponseEntity<String> buyProduct(@PathVariable long productID) {
        Optional<ProductDTO> productDTO = productService.findById(productID);
        if (productDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto seleccionado no existe");
        }
        userService.addProduct(productDTO.get());
        return ResponseEntity.ok("Producto añadido al carrito del usuario logueado");
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> searchProducts(
            @RequestParam(required = false) Integer from,
            @RequestParam(required = false) Integer to,
            @RequestParam(required = false) String name
    ) {
        List<ProductDTO> productDTOs;

        boolean hasFrom = from != null;
        boolean hasTo = to != null;
        boolean hasName = name != null && !name.trim().isEmpty();

        if ((hasFrom && hasTo) || hasName) {
            List<Product> products = productService.findProductsByNameAndPrice(from, to, name);
            productDTOs = products.stream()
                    .map(generalMapper::productToProductDTO)
                    .toList();

            if (!productDTOs.isEmpty()) {
                return ResponseEntity.ok(productDTOs);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
            }
        } else {
            productDTOs = productService.findAll();
            return ResponseEntity.ok(productDTOs);
        }
    }

}

