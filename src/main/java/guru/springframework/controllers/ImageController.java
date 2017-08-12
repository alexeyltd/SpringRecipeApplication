package guru.springframework.controllers;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.services.ImageService;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class ImageController {

	private final ImageService imageService;
	private final RecipeService recipeService;

	public ImageController(final ImageService imageService, final RecipeService recipeService) {
		this.imageService = imageService;
		this.recipeService = recipeService;
	}

	@GetMapping("/recipe/{id}/image")
	public String showUploadedForm(@PathVariable String id, Model model) {
		model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(id)));
		return "recipe/imageuploadform";
	}

	@PostMapping("/recipe/{id}/image")
	public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile multipartfile) {
		imageService.saveImageFile(Long.valueOf(id), multipartfile);
		return "redirect:/recipe/" + id + "/show";
	}

	@GetMapping("recipe/{id}/recipeimage")
	public void renderImageFromDB(@PathVariable String id, HttpServletResponse httpServletResponse) throws IOException {
		RecipeCommand commandById = recipeService.findCommandById(Long.valueOf(id));

		byte[] bytes = new byte[commandById.getImage().length];

		int i = 0;

		for (Byte b : commandById.getImage()) {
			bytes[i++] = b;
		}

		httpServletResponse.setContentType("image/jpeg");
		InputStream inputStream	 = new ByteArrayInputStream(bytes);
		IOUtils.copy(inputStream, httpServletResponse.getOutputStream());
	}

}
