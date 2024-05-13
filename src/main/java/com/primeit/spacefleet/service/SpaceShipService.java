package com.primeit.spacefleet.service;

import com.primeit.spacefleet.domain.SpaceShip;
import com.primeit.spacefleet.repository.SpaceShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaceShipService {
    @Autowired
    private SpaceShipRepository spaceShipRepository;

    @Cacheable(value = "spaceShips")
    public Page<SpaceShip> findAll(Pageable pageable) {
        return spaceShipRepository.findAll(pageable);
    }

    @Cacheable(value = "spaceShip", key = "#id")
    public Optional<SpaceShip> findById(long id) {
        return spaceShipRepository.findById(id);
    }

    public List<SpaceShip> findByName(String name) {
        return spaceShipRepository.findByNameContaining(name);
    }

    @CachePut(value = "spaceShip", key = "#id")
    public SpaceShip update(long id, SpaceShip spaceShip) {
        return spaceShipRepository.save(spaceShip);
    }

    @CachePut(value = "spaceShip")
    public SpaceShip create(SpaceShip spaceShip) {
        return spaceShipRepository.save(spaceShip);
    }

    @CacheEvict(value = "spaceShip", key = "#id")
    public void delete(long id) {
        Optional<SpaceShip> spaceShip = spaceShipRepository.findById(id);
        if (spaceShip.isPresent()) {
            spaceShipRepository.delete(spaceShip.get());
        }
    }

}
