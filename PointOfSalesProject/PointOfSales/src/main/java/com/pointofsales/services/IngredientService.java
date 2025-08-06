package com.pointofsales.services;

import com.pointofsales.entity.Ingredient;
import com.pointofsales.repository.IngredientRepository;
import com.pointofsales.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    public IngredientRepository ingredientRepository;

    @Autowired
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public void save(Ingredient ingredient) {
        ingredientRepository.save(ingredient);
    }
}
