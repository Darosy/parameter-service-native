package id.co.bni.parameter.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadinessService {
    @PersistenceContext
    private EntityManager entityManager;

    public boolean isDatabaseConnected() {
        String sqlQuery = "SELECT 1 FROM DUAL";
        Query query = entityManager.createNativeQuery(sqlQuery);
        List<?> resultList = query.getResultList();
        return !resultList.isEmpty();
    }
}
