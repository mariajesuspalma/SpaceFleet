package com.primeit.spacefleet;

import com.primeit.spacefleet.domain.SpaceShip;
import com.primeit.spacefleet.service.SpaceShipService;
import org.junit.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SpaceShipServiceTest extends SpacefleetApplicationTests {
    @Autowired
    private SpaceShipService spaceShipService;

    @Test
    public void testCreate() {
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setName("USS Enterprise");

        SpaceShip newSpaceShip = spaceShipService.create(spaceShip);
        assertThat(newSpaceShip).isNotNull();
        assertThat(newSpaceShip.getId()).isNotNull();
    }

    @Test(expected = TransactionSystemException.class)
    public void testCreateKo() {
        SpaceShip spaceShip = new SpaceShip();

        SpaceShip newSpaceShip = spaceShipService.create(spaceShip);
    }

    @Test
    public void testFindById() {
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setName("USS Enterprise");

        SpaceShip newSpaceShip = spaceShipService.create(spaceShip);

        Optional<SpaceShip> optionalSpaceShip = spaceShipService.findById(newSpaceShip.getId());
        assertThat(optionalSpaceShip.isPresent()).isTrue();
        assertThat(optionalSpaceShip.get().getId()).isEqualTo(newSpaceShip.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdKo() {
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setName("USS Enterprise");

        SpaceShip newSpaceShip = spaceShipService.create(spaceShip);

        Optional<SpaceShip> optionalSpaceShip = spaceShipService.findById(null);
        assertThat(optionalSpaceShip.isPresent()).isTrue();
        assertThat(optionalSpaceShip.get().getId()).isEqualTo(newSpaceShip.getId());
    }

    @Test
    public void testFindByName() {
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setName("USS Enterprise");

        spaceShipService.create(spaceShip);

        List<SpaceShip> spaceShipByName = spaceShipService.findByName("Enter");
        assertThat(spaceShipByName.isEmpty()).isFalse();
        assertThat(spaceShipByName.get(0).getName()).contains("Enter");
    }

    @Test(expected = AssertionFailedError.class)
    public void testFindByNameKo() {
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setName("USS Enterprise");

        spaceShipService.create(spaceShip);

        List<SpaceShip> spaceShipByName = spaceShipService.findByName(null);
        assertThat(spaceShipByName.isEmpty()).isFalse();
    }

    @Test
    public void testFindAll() {
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setName("USS Enterprise");

        SpaceShip spaceShip1 = new SpaceShip();
        spaceShip1.setName("Halcón Milenario");

        spaceShipService.create(spaceShip);
        spaceShipService.create(spaceShip1);

        Page<SpaceShip> spaceShipPage = spaceShipService.findAll(PageRequest.of(1, 10));
        assertThat(spaceShipPage.getTotalElements()).isGreaterThan(0l);
    }
}
