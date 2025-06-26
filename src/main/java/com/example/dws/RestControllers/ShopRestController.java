
package com.example.dws.RestControllers;
import com.example.dws.DTOs.CommentDTO;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.DTOs.ShopExDTO;
import com.example.dws.Entities.Shop;
import com.example.dws.Service.*;
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

    @Autowired
    private FileStorageService fileStorageService;

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


    @PostMapping(value = "/{shopID}/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addProductToShopFromFormData(
            @RequestParam("productName") String productName,
            @RequestParam("productPrize") Double productPrize,
            @RequestParam("file") MultipartFile file,
            @PathVariable long shopID) {

        Optional<ShopDTO> shopOpt = shopService.findById(shopID);
        if (shopOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La tienda seleccionada no existe");
        }

        if (productName == null || productName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto no puede estar vacío");
        }

        if (productPrize == null || productPrize <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El precio debe ser positivo");
        }


        ProductDTO productDTO = new ProductDTO(
                null,
                productName,
                productPrize,
                null,
                null,
                null
        );

        ProductDTO savedProduct = productService.saveProductInShop(productDTO, shopOpt.get());

        try {
            String storedFileName = fileStorageService.storeFile(file, savedProduct.productId());
            String originalFileName = file.getOriginalFilename();
            productService.updateProductFileInfo(savedProduct.productId(), originalFileName, storedFileName);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Producto añadido correctamente con archivo");
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
