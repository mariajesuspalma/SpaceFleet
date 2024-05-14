package com.primeit.spacefleet.kafka;

import com.primeit.spacefleet.domain.SpaceShip;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String TOPIC = "spaceship";

    private final KafkaTemplate<String, KafkaEvent> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, KafkaEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(SpaceShip spaceShip) {
        String randomKey = RandomStringUtils.randomAlphabetic(10);

        KafkaEvent event = new KafkaEvent();
        event.setSpaceShip(spaceShip);
        event.setKey(randomKey);

        try {
            kafkaTemplate.send(TOPIC, randomKey, event);
            logger.info("Event generated {}", event);
        } catch (Exception exception) {
            logger.error("Send failed of the event {}", event);
        }
    }

    class KafkaEvent {
        private String key;
        private SpaceShip spaceShip;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public SpaceShip getSpaceShip() {
            return spaceShip;
        }

        public void setSpaceShip(SpaceShip spaceShip) {
            this.spaceShip = spaceShip;
        }

        public KafkaEvent() {
        }

        public KafkaEvent(String key, SpaceShip spaceShip) {
            this.key = key;
            this.spaceShip = spaceShip;
        }
    }
}