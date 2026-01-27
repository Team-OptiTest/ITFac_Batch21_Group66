package BOOT-INF.classes.com.qatraining.repository;

import com.qatraining.entity.Category;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  List<Category> findByParentIsNull();
  
  List<Category> findByNameContainingIgnoreCase(String paramString);
  
  List<Category> findByParent_NameContainingIgnoreCase(String paramString);
  
  List<Category> findByParent_Id(Long paramLong);
  
  List<Category> findByNameContainingIgnoreCaseAndParent_NameContainingIgnoreCase(String paramString1, String paramString2);
  
  boolean existsByParent_Id(Long paramLong);
  
  boolean existsByNameIgnoreCaseAndParentIsNull(String paramString);
  
  boolean existsByNameIgnoreCaseAndParent_Id(String paramString, Long paramLong);
  
  List<Category> findByNameContainingIgnoreCaseAndParentId(String paramString, Long paramLong);
  
  Page<Category> findByNameContainingIgnoreCase(String paramString, Pageable paramPageable);
  
  Page<Category> findByParentId(Long paramLong, Pageable paramPageable);
  
  Page<Category> findByNameContainingIgnoreCaseAndParentId(String paramString, Long paramLong, Pageable paramPageable);
  
  long countByParentIsNull();
  
  long countByParentIsNotNull();
  
  List<Category> findByParentIsNotNull();
}


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/repository/CategoryRepository.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */