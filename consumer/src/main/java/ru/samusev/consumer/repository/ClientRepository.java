package ru.samusev.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.samusev.consumer.entity.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
