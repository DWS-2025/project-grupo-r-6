
package com.example.dws.RestControllers;
import com.example.dws.DTOs.CommentDTO;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.DTOs.ShopExDTO;
import com.example.dws.Entities.Shop;
import com.example.dws.Service.CommentService;
import com.example.dws.Service.ProductService;
import com.example.dws.Service.ShopService;
import com.example.dws.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shops")
public class ShopRestController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<ShopExDTO>> getAllShops() {
        List<ShopExDTO> shopsDTO = shopService.findAllShops();
        return ResponseEntity.ok(shopsDTO);
    }

    @GetMapping("/{shopID}")
    public ResponseEntity<ShopExDTO> getShopById(@PathVariable long shopID) {
        Optional<ShopExDTO> shopDTO = shopService.findShopById(shopID);
        if (shopDTO.isPresent()){
            return ResponseEntity.ok(shopDTO.get());
        } else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
    }
    // REVISAR PORQUE HACE FALTA ESTAR AUTORIZADO
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveShop(@RequestParam String shopName,
                                           @RequestParam String imageName,
                                           @RequestParam MultipartFile image) throws IOException {
        if (shopName == null || shopName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la tienda no puede ser vacío");
        }
        if (!image.isEmpty() && imageName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Si quieres añadir una imagen el nombre de la imagen no puede ser vacío");
        }

        Shop shop = new Shop(shopName, imageName);
        shopService.save(shopService.shopToShopDTO(Optional.of(shop)), image);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tienda creada correctamente");
    }

    @GetMapping(value = "/{shopID}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getShopImage(@PathVariable Long shopID, HttpServletResponse response) throws SQLException, IOException {
        Blob imageBlob = shopService.getShopImage(shopID);
        if (imageBlob != null) {
            response.setContentType("image/jpeg");
            response.getOutputStream().write(imageBlob.getBytes(1, (int) imageBlob.length()));
            response.getOutputStream().flush();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe o no tiene imagen");
        }
    }


    // REVISAR
    @DeleteMapping("/{shopID}")
    public ResponseEntity<String> deleteShop(@PathVariable long shopID) {
        Optional<ShopDTO> shop = shopService.findById(shopID);
        if (shop.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        productService.removeShopFromAllProducts(shop.get());
        shopService.removeAllProductsFromShop(shopID);
        shopService.removeAllCommentsFromShop(shopID);
        shopService.deleteById(shopID);
        return ResponseEntity.ok("Tienda eliminada correctamente");
    }

    // REVISAR
    @PostMapping("/{shopID}/products")
    public ResponseEntity<String> addProductToShop(@RequestBody ProductDTO productDTO, @PathVariable long shopID) {
        Optional<ShopDTO> shop = shopService.findById(shopID);
        if (shop.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        if (productDTO.productName() == null || productDTO.productName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío");
        }

        productService.saveProductInShop(productDTO, shop.get());
        return ResponseEntity.status(HttpStatus.CREATED).body("Producto añadido correctamente");
    }

    // REVISAR
    @PostMapping("/comments/{shopID}")
    public ResponseEntity<String> addCommentToShop(@RequestBody CommentDTO commentDTO, @PathVariable long shopID) {
        Optional<ShopDTO> shopDTO = shopService.findById(shopID);
        if (shopDTO.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }
        shopService.saveComment(shopDTO.get(), commentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Comentario añadido correctamente");
    }
}
