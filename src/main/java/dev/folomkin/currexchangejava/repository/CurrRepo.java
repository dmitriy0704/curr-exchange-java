package dev.folomkin.currexchangejava.repository;

import dev.folomkin.currexchangejava.domain.entity.CurrEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrRepo extends JpaRepository<CurrEntity, Long> {

    Optional<CurrEntity> findByCharCode(String charCode);

    // Возвращает true, если в таблице есть хотя бы одна запись
    boolean existsBy();
}
