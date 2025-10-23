package studentsystem;

import java.util.List;

public interface CRUDOperations<T> {
    boolean add(T item);          // CREATE
    List<T> getAll();             // READ
    boolean update(T item);       // UPDATE  
    boolean delete(int id);       // DELETE
}