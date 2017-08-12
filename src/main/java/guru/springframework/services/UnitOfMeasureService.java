package guru.springframework.services;

import java.util.*;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;

public interface UnitOfMeasureService {
	Set<UnitOfMeasureCommand> listAllUoms();
}
