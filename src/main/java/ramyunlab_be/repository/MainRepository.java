package ramyunlab_be.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ramyunlab_be.entity.RamyunEntity;

@Repository
public interface MainRepository extends JpaRepository<RamyunEntity, Long> {

  Page<RamyunEntity> findAll (Pageable pageable);

}


