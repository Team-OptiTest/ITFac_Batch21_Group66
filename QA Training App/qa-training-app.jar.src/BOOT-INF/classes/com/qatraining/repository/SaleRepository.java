package BOOT-INF.classes.com.qatraining.repository;

import com.qatraining.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaleRepository extends JpaRepository<Sale, Long> {
  Page<Sale> findAll(Pageable paramPageable);
  
  @Query(value = "SELECT COUNT(*), COALESCE(SUM(total_price), 0) FROM sales", nativeQuery = true)
  Object[] getSalesSummary();
}


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/repository/SaleRepository.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */