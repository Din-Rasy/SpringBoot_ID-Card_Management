package net.orderzone.idcard.repository;

import net.orderzone.idcard.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT t FROM Template t WHERE " +
           "LOWER(t.code) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Template> search(@Param("keyword") String keyword);
}
