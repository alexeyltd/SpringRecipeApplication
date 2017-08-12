package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.services.IngredientService;
import guru.springframework.services.RecipeService;
import guru.springframework.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class IngredientController {

	private final RecipeService recipeService;
	private final IngredientService ingredientService;
	private final UnitOfMeasureService unitOfMeasureService;

	public IngredientController(final RecipeService recipeService, final IngredientService ingredientService, final UnitOfMeasureService unitOfMeasureService) {
		this.recipeService = recipeService;
		this.ingredientService = ingredientService;
		this.unitOfMeasureService = unitOfMeasureService;
	}

	@GetMapping
	@RequestMapping("/recipe/{recipeId}/ingredients")
	public String ListIngredients(@PathVariable String recipeId, Model model) {
		log.debug("Getting ingredient list for recipe id : " + recipeId);
		model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));
		return "recipe/ingredient/list";
	}

	@GetMapping
	@RequestMapping("/recipe/{recipeId}/ingredient/{id}/show")
	public String showRecipeIngredient(@PathVariable String recipeId,
									   @PathVariable String id,
									   Model model) {
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
		return "recipe/ingredient/show";
	}

	@GetMapping
	@RequestMapping("/recipe/{recipeId}/ingredient/{id}/update")
	public String updateRecipeIngredient(@PathVariable String recipeId,
										 @PathVariable String id,
										 Model model) {
		model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
		model.addAttribute("uomList", unitOfMeasureService.listAllUoms());
		return "recipe/ingredient/ingredientform";
	}

	@GetMapping
	@RequestMapping("/recipe/{redipeId}/ingredient/new")
	public String newRecipeIngredient() {
		return "recipe/ingredient/ingredientform";
	}

	@PostMapping
	@RequestMapping("/recipe/{recipeId}/ingredient")
	public String saveOrUpdate(@ModelAttribute IngredientCommand command) {
		IngredientCommand savedIngredientCommand =  ingredientService.saveIngredientCommand(command);
		log.debug("Saved recipe id " + savedIngredientCommand.getRecipeId());
		log.debug("Saved ingredient id " + savedIngredientCommand.getId());

		return "redirect:/recipe/" + savedIngredientCommand.getRecipeId() + "/ingredient/" + savedIngredientCommand.getId() + "/show";

	}

}
