package guru.springframework.services;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

	private final RecipeRepository recipeRepository;
	private final IngredientCommandToIngredient ingredientCommandToIngredient;
	private final IngredientToIngredientCommand ingredientToIngredientCommand;
	private final UnitOfMeasureRepository unitOfMeasureRepository;

	public IngredientServiceImpl(final RecipeRepository recipeRepository, final IngredientToIngredientCommand ingredientToIngredientCommand, final IngredientCommandToIngredient ingredientCommandToIngredient, final UnitOfMeasureRepository unitOfMeasureRepository) {
		this.recipeRepository = recipeRepository;
		this.ingredientToIngredientCommand = ingredientToIngredientCommand;
		this.ingredientCommandToIngredient = ingredientCommandToIngredient;
		this.unitOfMeasureRepository = unitOfMeasureRepository;
	}

	@Override
	public IngredientCommand findByRecipeIdAndIngredientId(final long recipeId, final long ingredientId) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

		if (!recipeOptional.isPresent()) {
//			todo impl
			log.error("Recipe is not found :" + recipeId);
		}

		Recipe recipe = recipeOptional.get();

		Optional<IngredientCommand> ingredientCommandOptional = recipe.getIngredients().stream()
				.filter(ingredient -> ingredient.getId().equals(ingredientId))
				.map(ingredient -> ingredientToIngredientCommand.convert(ingredient)).findFirst();

		if (!ingredientCommandOptional.isPresent()) {
//			todo impl
			log.error("Ingredient is not found :" + ingredientId);
		}

		return ingredientCommandOptional.get();
	}

	@Override
	@Transactional
	public IngredientCommand saveIngredientCommand(final IngredientCommand ingredientCommand) {

		Optional<Recipe> recipeOptional = recipeRepository.findById(ingredientCommand.getRecipeId());

		if (!recipeOptional.isPresent()) {
//			todo error not found
			log.error("Recipe not found for id :" + ingredientCommand.getRecipeId());
			return new IngredientCommand();
		} else {
			Recipe recipe = recipeOptional.get();

			Optional<Ingredient> ingredientOptional = recipe.getIngredients()
					.stream()
					.filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
					.findFirst();

			if (ingredientOptional.isPresent()) {
				Ingredient ingredient = ingredientOptional.get();
				ingredient.setDescription(ingredientCommand.getDescription());
				ingredient.setAmount(ingredientCommand.getAmount());
				ingredient.setUom(unitOfMeasureRepository
							.findById(ingredientCommand.getUom().getId())
							.orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); // todo address this
			} else {
//				add new Ingredient
				recipe.addIngredient(ingredientCommandToIngredient.convert(ingredientCommand));
			}

			Recipe savedRecipe = recipeRepository.save(recipe);

			return ingredientToIngredientCommand.convert(savedRecipe.getIngredients().stream()
							.filter(ingredient -> ingredient.getId().equals(ingredientCommand.getId()))
							.findFirst()
							.get());
		}

	}
}
