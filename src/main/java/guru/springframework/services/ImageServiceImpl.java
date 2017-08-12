package guru.springframework.services;

import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

	private final RecipeRepository recipeRepository;

	public ImageServiceImpl(final RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@Override
	@Transactional
	public void saveImageFile(final Long recipeId, final MultipartFile multipartfile) {
		try {
			Recipe recipe = recipeRepository.findById(recipeId).get();
			Byte[] bytes = new Byte[multipartfile.getBytes().length];
			int i = 0;
			for (byte b : multipartfile.getBytes()) {
				bytes[i++] = b;
			}
			recipe.setImage(bytes);
			recipeRepository.save(recipe);
		} catch (IOException e) {
			log.debug("Error occurred", e);
			e.printStackTrace();
		}

	}
}
