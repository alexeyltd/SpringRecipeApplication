package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UnitOfMeasureServiceImplTest {

	private final UnitOfMeasureToUnitOfMeasureCommand unit = new UnitOfMeasureToUnitOfMeasureCommand();

	UnitOfMeasureService unitOfMeasureService;

	@Mock
	UnitOfMeasureRepository unitOfMeasureRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		unitOfMeasureService = new UnitOfMeasureServiceImpl(unitOfMeasureRepository, unit);
	}

	@Test
	public void listAllUoms() throws Exception {
//		given
		Set<UnitOfMeasure> unitOfMeasureCommandSet = new HashSet<>();
		UnitOfMeasure unitOfMeasureOne = new UnitOfMeasure();
		unitOfMeasureOne.setId(1L);

		unitOfMeasureCommandSet.add(unitOfMeasureOne);

		UnitOfMeasure unitOfMeasureTwo = new UnitOfMeasure();
		unitOfMeasureTwo.setId(2L);

		unitOfMeasureCommandSet.add(unitOfMeasureTwo);

		when(unitOfMeasureRepository.findAll()).thenReturn(unitOfMeasureCommandSet);

//		when
		Set<UnitOfMeasureCommand> allUoms = unitOfMeasureService.listAllUoms();

//		then
		assertEquals(2, allUoms.size());
		verify(unitOfMeasureRepository, times(1)).findAll();


	}

}