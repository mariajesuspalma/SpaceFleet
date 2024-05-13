package com.primeit.spacefleet.controller;

import com.primeit.spacefleet.domain.SpaceShip;
import com.primeit.spacefleet.exception.NegativeIdException;
import com.primeit.spacefleet.service.SpaceShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/fleet")
public class SpaceFleetController {
    @Autowired
    private SpaceShipService spaceShipService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getShips(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {

        Map<String, Object> result = new HashMap<>();

        Page<SpaceShip> pageResult = spaceShipService.findAll(PageRequest.of(page, size));

        result.put("spaceShips", CollectionUtils.isEmpty(pageResult.getContent()) ? new ArrayList<>() : pageResult.getContent());
        result.put("currentPage", pageResult.getNumber());
        result.put("totalPages", pageResult.getTotalPages());
        result.put("totalItems", pageResult.getTotalElements());

        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceShip> getById(@PathVariable long id) throws NegativeIdException, ChangeSetPersister.NotFoundException {
        if (id < 0)
            throw new NegativeIdException();

        Optional<SpaceShip> spaceShip = spaceShipService.findById(id);
        if (spaceShip.isPresent()) {
            return new ResponseEntity<>(spaceShip.get(), HttpStatus.OK);
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }

    }

    @GetMapping(value = "/getByName/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SpaceShip>> getByName(@PathVariable String name) {
        List<SpaceShip> spaceShips = spaceShipService.findByName(name);
        if (!CollectionUtils.isEmpty(spaceShips)) {
            return new ResponseEntity<>(spaceShips, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }


    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceShip> create(@RequestBody SpaceShip spaceShip) {
        SpaceShip newSpaceShip = spaceShipService.create(spaceShip);
        return new ResponseEntity<>(newSpaceShip, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceShip> update(@PathVariable long id, @RequestBody SpaceShip modifiedSpaceShip) throws ChangeSetPersister.NotFoundException, NegativeIdException {
        if (id < 0)
            throw new NegativeIdException();

        Optional<SpaceShip> previousSpaceShip = spaceShipService.findById(id);

        if (previousSpaceShip.isPresent()) {
            SpaceShip spaceShip = previousSpaceShip.get();
            spaceShip.setName(modifiedSpaceShip.getName());

            return new ResponseEntity<>(spaceShipService.update(id, spaceShip), HttpStatus.OK);
        } else {
            throw new ChangeSetPersister.NotFoundException();
        }
    }


    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceShip> delete(@PathVariable long id) throws NegativeIdException {
        if (id < 0)
            throw new NegativeIdException();

        spaceShipService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
