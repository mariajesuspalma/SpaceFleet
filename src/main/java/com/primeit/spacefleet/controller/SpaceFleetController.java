package com.primeit.spacefleet.controller;

import com.primeit.spacefleet.controller.GeneralExceptionHandler.ErrorResponse;
import com.primeit.spacefleet.domain.SpaceShip;
import com.primeit.spacefleet.exception.NegativeIdException;
import com.primeit.spacefleet.service.SpaceShipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all the spaceships")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaceships",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SpaceShip.class)))}),
            @ApiResponse(responseCode = "204", description = "There are no spaceships"),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getShips(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {

        Map<String, Object> result = new HashMap<>();

        Page<SpaceShip> pageResult = spaceShipService.findAll(PageRequest.of(page, size));

        result.put("spaceShips", CollectionUtils.isEmpty(pageResult.getContent()) ? new ArrayList<>() : pageResult.getContent());
        result.put("currentPage", pageResult.getNumber());
        result.put("totalPages", pageResult.getTotalPages());
        result.put("totalItems", pageResult.getTotalElements());

        return new ResponseEntity<>(result, pageResult.getTotalElements() > 0 ? HttpStatus.OK : HttpStatus.NO_CONTENT);

    }

    @Operation(summary = "Get a spaceship by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaceship found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceShip.class))}),
            @ApiResponse(responseCode = "400", description = "Negative id supplied", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Spaceship not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
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

    @Operation(summary = "Get a spaceship list by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaceships found",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SpaceShip.class)))}),
            @ApiResponse(responseCode = "204", description = "There are not spaceships by that name"),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @GetMapping(value = "/getByName/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SpaceShip>> getByName(@PathVariable String name) {
        List<SpaceShip> spaceShips = spaceShipService.findByName(name);
        if (!CollectionUtils.isEmpty(spaceShips)) {
            return new ResponseEntity<>(spaceShips, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @Operation(summary = "Create a spaceship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaceship created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceShip.class))}),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceShip> create(@RequestBody SpaceShip spaceShip) {
        SpaceShip newSpaceShip = spaceShipService.create(spaceShip);
        return new ResponseEntity<>(newSpaceShip, HttpStatus.OK);
    }

    @Operation(summary = "Update a spaceship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaceship updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceShip.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
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

    @Operation(summary = "Delete a spaceship")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spaceship deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpaceShip.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class))})})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceShip> delete(@PathVariable long id) throws NegativeIdException, ChangeSetPersister.NotFoundException {
        if (id < 0)
            throw new NegativeIdException();

        Optional<SpaceShip> previousSpaceShip = spaceShipService.findById(id);

        if (!previousSpaceShip.isPresent()) {
            throw new ChangeSetPersister.NotFoundException();
        } else {
            spaceShipService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }


}
