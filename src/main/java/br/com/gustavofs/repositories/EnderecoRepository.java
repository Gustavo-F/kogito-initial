package br.com.gustavofs.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gustavofs.models.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, UUID> {

    // Additional query methods can be defined here if needed

}
