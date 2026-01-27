package BOOT-INF.classes.com.qatraining.repository;

import com.qatraining.entity.Inventory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  List<Inventory> findByPlantId(Long paramLong);
}


/* Location:              /Users/chamith/Downloads/QA Training App/qa-training-app.jar!/BOOT-INF/classes/com/qatraining/repository/InventoryRepository.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */