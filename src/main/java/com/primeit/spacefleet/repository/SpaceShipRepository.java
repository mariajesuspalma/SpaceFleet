package com.primeit.spacefleet.repository;

import com.primeit.spacefleet.domain.SpaceShip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpaceShipRepository extends JpaRepository<SpaceShip, Long> {
    Page<SpaceShip> findAll(Pageable pageable);

    Optional<SpaceShip> findById(long id);

    List<SpaceShip> findByNameContaining(String name);
}
