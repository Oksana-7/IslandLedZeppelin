package com.javarush.island.kalichinskaia.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.javarush.island.kalichinskaia.core.organism.Organism;
import com.javarush.island.kalichinskaia.core.organism.herbivores.*;
import com.javarush.island.kalichinskaia.core.organism.predators.*;

import lombok.*;

import java.net.URL;
import java.util.*;

@Getter
@Setter
@ToString
public class Config {
    public static final String SETTINGS_YAML = "/kalichinskaia/settings.yaml";
    public static final List<Class<? extends Organism>> TYPES = Arrays.asList(
            Wolf.class, Fox.class, Boa.class, Bear.class, Eagle.class, Horse.class,
            Deer.class, Rabbit.class, Mouse.class, Goat.class, Sheep.class, Boar.class,
            Buffalo.class, Duck.class, Caterpillar.class);

    private int period;
    private int rows;
    private int cols;
    private int percentProbably;
    private Map<String, Limit> organismLimits = new HashMap<>();
    private Map<String, Map<String, Integer>> foodMap = new HashMap<>();

    @SneakyThrows
    public static Config createConfigFromFile() {
        Config config = new Config();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ObjectReader readerForUpdating = mapper.readerForUpdating(config);
        URL resource = Config.class.getResource(SETTINGS_YAML);
        if (Objects.nonNull(resource)) {
            readerForUpdating.readValue(resource.openStream());
        }
        return config;
    }

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    public static class Limit {
        private int maxCountInArea;
        private double maxWeight;
        private int maxSpeed;
        private double maxFood;
    }

}
