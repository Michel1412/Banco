package com.Banco.caixaEletronico.repository;

import com.Banco.caixaEletronico.models.Associates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociateRepository extends JpaRepository<Associates, Integer> {


}
