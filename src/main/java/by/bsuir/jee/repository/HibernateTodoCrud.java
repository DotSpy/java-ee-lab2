package by.bsuir.jee.repository;

import by.bsuir.jee.TodoCrud;
import by.bsuir.jee.model.Todo;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

@Stateless
@Remote(TodoCrud.class)
public class HibernateTodoCrud implements TodoCrud {

  private Session sessionObj;
  private SessionFactory sessionFactoryObj;

  {
    Configuration configObj = new Configuration();
    configObj.configure("hibernate.cfg.xml")
        .addAnnotatedClass(Todo.class);
    ServiceRegistry serviceRegistryObj = new StandardServiceRegistryBuilder().applySettings(configObj.getProperties())
        .build();
    sessionFactoryObj = configObj.buildSessionFactory(serviceRegistryObj);
  }

  @Override
  public Todo create(Todo todo) {
    Long id = -1L;
    try {
      sessionObj = sessionFactoryObj.openSession();
      sessionObj.beginTransaction();
      id = (Long) sessionObj.save(todo);
      sessionObj.getTransaction().commit();
    } catch (Exception sqlException) {
      if (null != sessionObj.getTransaction()) {
        sessionObj.getTransaction().rollback();
      }
      sqlException.printStackTrace();
    } finally {
      if (sessionObj != null) {
        sessionObj.close();
      }
    }
    return new Todo(id, todo.getHeader(), todo.getDescription());
  }

  @Override
  public List<Todo> read() {
    List<Todo> todos = new ArrayList<>();
    try {
      sessionObj = sessionFactoryObj.openSession();
      CriteriaBuilder builder = sessionObj.getCriteriaBuilder();
      CriteriaQuery<Todo> criteria = builder.createQuery(Todo.class);
      criteria.from(Todo.class);
      List<Todo> data = sessionObj.createQuery(criteria).getResultList();
      return data;
    } catch (Exception sqlException) {
      if (null != sessionObj.getTransaction()) {
        sessionObj.getTransaction().rollback();
      }
      sqlException.printStackTrace();
    } finally {
      if (sessionObj != null) {
        sessionObj.close();
      }
    }
    return todos;
  }

  @Override
  public Todo read(Long id) {
    Todo result = null;
    try {
      // Getting Session Object From SessionFactory
      sessionObj = sessionFactoryObj.openSession();
      // Getting Transaction Object From Session Object
      sessionObj.beginTransaction();

      // Creating Transaction Entity
      result = sessionObj.get(Todo.class, id);
      sessionObj.getTransaction().commit();
    } catch (Exception sqlException) {
      if (null != sessionObj.getTransaction()) {
        sessionObj.getTransaction().rollback();
      }
    } finally {
      if (sessionObj != null) {
        sessionObj.close();
      }
    }
    return result;
  }

  @Override
  public Todo update(Todo todo) {
    Todo result = todo;
    try {
      // Getting Session Object From SessionFactory
      sessionObj = sessionFactoryObj.openSession();
      // Getting Transaction Object From Session Object
      sessionObj.beginTransaction();

      // Creating Transaction Entity
      Todo todoObj = sessionObj.get(Todo.class, todo.getId());
      todoObj.setHeader(todo.getHeader());
      todoObj.setDescription(todo.getDescription());
      result = todoObj;
      sessionObj.getTransaction().commit();
    } catch (Exception sqlException) {
      if (null != sessionObj.getTransaction()) {
        sessionObj.getTransaction().rollback();
      }
    } finally {
      if (sessionObj != null) {
        sessionObj.close();
      }
    }
    return result;
  }

  @Override
  public Boolean delete(Long id) {
    try {
      sessionObj = sessionFactoryObj.openSession();
      sessionObj.beginTransaction();
      Todo todoObj = sessionObj.load(Todo.class, id);
      sessionObj.delete(todoObj);
      sessionObj.getTransaction().commit();
    } catch (Exception sqlException) {
      if (null != sessionObj.getTransaction()) {
        sessionObj.getTransaction().rollback();
      }
      sqlException.printStackTrace();
      return false;
    } finally {
      if (sessionObj != null) {
        sessionObj.close();
      }
    }
    return true;
  }
}