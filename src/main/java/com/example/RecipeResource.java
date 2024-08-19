package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/recipes")
public class RecipeResource {

    private final RecipeService recipeService;

    public RecipeResource(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("upload")
    public ResponseEntity<Void> addRecipeDocumentsForRag(@RequestParam("file") MultipartFile file,
                                                         @RequestParam(required = false, defaultValue = "0") int pageTopMargin,
                                                         @RequestParam(required = false, defaultValue = "0") int pageBottomMargin) {
        recipeService.addRecipeDocumentForRag(file.getResource(), pageTopMargin, pageBottomMargin);
        return ResponseEntity.noContent().build();
    }

}
