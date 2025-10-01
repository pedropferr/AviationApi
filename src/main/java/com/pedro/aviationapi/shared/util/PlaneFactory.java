package com.pedro.aviationapi.shared.util;

import com.pedro.aviationapi.infrastructure.persistence.entities.PlaneEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlaneFactory {

    private static final String[] MANUFACTURERS = {
            "Boeing", "Airbus", "Embraer", "Bombardier", "Cessna"
    };

    private static final String[] MODELS = {
            "737", "A320", "E195", "CRJ900", "Citation X"
    };

    private static final Random random = new Random();

    public static List<PlaneEntity> generatePlanes() {
        List<PlaneEntity> planes = new ArrayList<>();

        int quantity = random.nextInt(5);

        for (int i = 0; i < quantity; i++) {

            PlaneEntity plane = PlaneEntity.builder()
                    .registration(generateRegistration())
                    .model(MODELS[random.nextInt(MODELS.length)])
                    .manufacturer(MANUFACTURERS[random.nextInt(MANUFACTURERS.length)])
                    .yearManufacture(1980 + random.nextInt(45))
                    .build();

            planes.add(plane);
        }

        return planes;
    }

    private static String generateRegistration() {
        String prefix = "PR-";
        char letter1 = (char) ('A' + random.nextInt(26));
        char letter2 = (char) ('A' + random.nextInt(26));
        char letter3 = (char) ('A' + random.nextInt(26));
        return prefix + letter1 + letter2 + letter3;
    }
}
