package BOOT-INF.classes.com.qatraining.repository;

import com.qatraining.entity.Plant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
  List<Plant> findByCategoryId(Long paramLong);
  
  boolean existsByNameIgnoreCaseAndCategoryId(String paramString, Long paramLong);
  
  boolean existsByNameIgnoreCase(String paramString);
  
  Page<Plant> findByNameContainingIgnoreCase(String paramString, Pageable paramPageable);
  
  Page<Plant> findByCategory_Id(Long paramLong, Pageable paramPageable);
  
  Page<Plant> findByNameContainingIgnoreCaseAndCategory_Id(String paramString, Long paramLong, Pageable paramPageable);
  
  long countByQuantityLessThan(int paramInt);
}


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/repository/PlantRepository.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */