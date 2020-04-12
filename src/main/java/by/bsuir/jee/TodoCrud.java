package by.bsuir.jee;

import by.bsuir.jee.model.Todo;
import java.util.List;

public interface TodoCrud {

  Todo create(Todo todo);

  List<Todo> read();

  Todo read(Long id);

  Todo update(Todo todo);

  Boolean delete(Long id);
}
