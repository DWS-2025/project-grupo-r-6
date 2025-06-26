package com.example.dws.Service;

import com.example.dws.DTOs.CommentDTO;
import com.example.dws.DTOs.ProductDTO;
import com.example.dws.DTOs.ShopDTO;
import com.example.dws.DTOs.UserDTO;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

@Service
public class SanitizationService {

    //---------------------------------PRINCIPAL METHODS FOR SANITIZATION---------------------------------

    // Use jsoup to sanitize inputs forms
    public String sanitizeBasic(String input) {
        if (input == null) {
            return null;
        }

        return Jsoup.clean(input, Safelist.basic());
    }


    // Use JSOUP to sanitize inputs forms with none
    public String sanitizeNone(String input) {
        if (input == null) {
            return null;
        }
        return Jsoup.clean(input, Safelist.none());
    }

    // Jsoup to sanitize custom text using QuillJS - made for comments
    public String sanitizeQuill(String input) {
        if (input == null) {
            return null;
        }

        // Custom whitelist quilljs
        Safelist quillSafelist = new Safelist()
                .addTags("p", "br", "strong", "em", "ul", "ol", "li", "a", "u", "h1", "h2", "h3")
                .addAttributes("a", "href")
                .addProtocols("a", "href", "http", "https")
                .addEnforcedAttribute("a", "target", "_blank");

        return Jsoup.clean(input, quillSafelist);
    }

    // Use jsoup method for imgs
    public String sanitizeImg(String input) {
        if (input == null) {
            return null;
        }

        // Define a custom Safelist for images
        Safelist imgSafelist = new Safelist()
                .addTags("img")
                .addAttributes("img", "src", "alt")
                .addProtocols("img", "src", "http", "https");

        return Jsoup.clean(input, imgSafelist);
    }
    // ------------ USER ------------
    public UserDTO sanitizeUserDTO(UserDTO userDTO) {
        if (userDTO == null) return null;

        return new UserDTO(
                userDTO.id(),
                sanitizeNone(userDTO.userName()),
                userDTO.password(),
                userDTO.confirmPassword(),
                sanitizeNone(userDTO.email()),
                userDTO.roles(),
        );
    }

    // ------------ PRODUCT ------------
    public ProductDTO sanitizeProductDTO(ProductDTO productDTO) {
        if (productDTO == null) return null;

        return new ProductDTO(
                productDTO.productId(),
                sanitizeNone(productDTO.productName()),
                productDTO.productPrize(),
                productDTO.shops(),
                productDTO.originalFileName(),
                productDTO.storedFileName()
        );
    }

    // ------------ COMMENT ------------
    public CommentDTO sanitizeCommentDTO(CommentDTO commentDTO) {
        if (commentDTO == null) return null;

        return new CommentDTO(
                commentDTO.commentId(),
                commentDTO.user(),
                sanitizeBasic(commentDTO.issue()),
                sanitizeQuill(commentDTO.message()),
                commentDTO.shopID()
        );
    }

    // ------------ SHOP ------------
    public ShopDTO sanitizeShopDTO(ShopDTO shopDTO) {
        if (shopDTO == null) return null;

        return new ShopDTO(
                shopDTO.shopID(),
                sanitizeNone(shopDTO.shopName()),
                shopDTO.products(),
                shopDTO.comments(),
                sanitizeImg(shopDTO.imageName()),
                sanitizeBasic(shopDTO.imageUrl())
        );
    }
}

