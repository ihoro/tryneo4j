package com.in6k.neo4j;

import com.arangodb.ArangoConfigure;
import com.arangodb.ArangoDriver;
import com.arangodb.ArangoException;
import com.arangodb.entity.DocumentEntity;
import com.arangodb.entity.EdgeDefinitionEntity;
import com.arangodb.entity.GraphEntity;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.LongStream;

/**
 * try arangodb
 * Created by DmitriyS on 26/06/2015.
 */
public class T008 {

    static class God {
        String name;
        public God(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class Person {
        String name;
        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        ArangoConfigure configure = new ArangoConfigure();
        configure.init();
        ArangoDriver arangoDriver = new ArangoDriver(configure);

        String dbName = "graph_test_db";
        try {
            arangoDriver.createDatabase(dbName);
            System.out.println("Database created: " + dbName);
        } catch (Exception e) {
            System.out.println("Failed to create database " + dbName + "; " + e.getMessage());
        }
        arangoDriver.setDefaultDatabase(dbName);

        try {
            arangoDriver.startBatchMode();
        } catch (ArangoException e) {
            e.printStackTrace();
        }

        List<EdgeDefinitionEntity> edgeDefinitions = new ArrayList<>();

        EdgeDefinitionEntity edgeDefHasWritten = new EdgeDefinitionEntity();
        edgeDefHasWritten.setCollection("CREATED");

        List<String> from = new ArrayList<>();
        from.add("God");
        edgeDefHasWritten.setFrom(from);

        List<String> to = new ArrayList<>();
        to.add("Person");
        edgeDefHasWritten.setTo(to);

        edgeDefinitions.add(edgeDefHasWritten);

        List<String> orphanCollections = new ArrayList<>();

        GraphEntity graphAcademical = null;
        try {
            graphAcademical = arangoDriver.createGraph("Space", edgeDefinitions, orphanCollections, true);
        } catch (ArangoException e) {
            e.printStackTrace();
        }

        final long count = 10_000_000;
        StopWatch watch = new StopWatch();

        try {
            watch.start("insert God user");
            DocumentEntity<God> god = arangoDriver.graphCreateVertex("Space", "God", new God("God"), true);
            watch.stop();

            watch.start("insert " + count + " users and add relationship with God");
            LongStream.rangeClosed(1L, count).forEach(userId -> {
                try {
                    DocumentEntity<Person> person = arangoDriver.graphCreateVertex("Space", "Person", new Person("user #" + userId), true);
                    arangoDriver.graphCreateEdge("Space", "CREATED", null, god.getDocumentHandle(), person.getDocumentHandle());
                } catch (ArangoException e) {
                    e.printStackTrace();
                }
            });
            watch.stop();
        } catch (ArangoException e) {
            e.printStackTrace();
        }


        try {
            arangoDriver.cancelBatchMode();
        } catch (ArangoException e) {
            e.printStackTrace();
        }

        System.out.println(watch.prettyPrint());
    }
}
