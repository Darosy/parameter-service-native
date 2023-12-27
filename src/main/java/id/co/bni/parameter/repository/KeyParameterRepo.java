package id.co.bni.parameter.repository;

import id.co.bni.parameter.entity.KeyParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KeyParameterRepo extends JpaRepository<KeyParameter, String> {
    @Query("select km from KEY_PARAMETER km where km.key = ?1")
    KeyParameter findByKey(String key);
}
