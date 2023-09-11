package com.Banco.caixaEletronico.service;

import com.Banco.caixaEletronico.models.Associates;
import com.Banco.caixaEletronico.repository.AssociateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AssociateService {

    private final AssociateRepository associateRepository;


    public AssociateService(AssociateRepository associateRepository) {
        this.associateRepository = associateRepository;
    }

    public ResponseEntity<Object> registerAssociate(Associates associates) {
        this.validateAllInfos(associates);
        if (this.associateRepository.validateAssociateExistsByDocumentNumber(associates.getDocumentNumber())) {
            throw new RuntimeException("Esse associado já foi cadastrado!");
        }
        return ResponseEntity.ok(this.associateRepository.save(associates));
    }


    public List<Associates> findAllAssociates() {
        if (this.associateRepository.findAll().isEmpty()) {
            throw new RuntimeException("Não existe nenhum associado cadastrado!");
        }
        return this.associateRepository.findAll();
    }

    public Associates findAssociateById(Integer id) {
        return this.validateAssociateById(id);
    }

    public Object replaceAssociate(Integer id, Associates associates) {
        this.validateAllInfos(associates);
        if (! this.associateRepository.validateAssociateExistsByDocumentNumber(associates.getDocumentNumber())) {
            throw new RuntimeException("Esse documento não foi cadastrado!");
        }
        Associates renameAssociate = this.validateAssociateById(id);
        renameAssociate.setName(associates.getName());
        renameAssociate.setPhoneNumber(associates.getPhoneNumber());
        renameAssociate.setSalary(associates.getSalary());
        return ResponseEntity.ok(this.associateRepository.save(renameAssociate));
    }

    public Object deleteAssociate(Integer id) {
        this.associateRepository.deleteById(id);
        return ResponseEntity.ok("Associado deletado com sucesso!");
    }
    public Associates validateAssociateById(Integer id) {
        return this.associateRepository.findById(id).orElseThrow( () -> {
            return new RuntimeException("Esse associado não foi cadastrado!");
        });
    }

    private void validateAllInfos(Associates associates) {
        String message = "Preencha o(s) campo(s):";
        if (Objects.isNull(associates.getName())) {
            message += " 'nome'";
        }
        if (Objects.isNull(associates.getSalary())) {
            message += " 'salário'";
        }
        if (Objects.isNull(associates.getDocumentNumber())) {
            message += " 'CPF'";
        }
        if (Objects.isNull(associates.getPhoneNumber())) {
            message += " 'número (celular)";
        }
        if (! message.equals("Preencha o(s) campo(s):")) {
            throw new RuntimeException(message);
        }
    }
}
