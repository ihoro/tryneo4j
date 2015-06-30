package com.in6k.neo4j;

import com.arangodb.ArangoConfigure;
import com.arangodb.ArangoDriver;
import com.arangodb.ArangoException;
import com.arangodb.blueprints.ArangoDBGraph;
import com.arangodb.blueprints.ArangoDBGraphException;
import com.arangodb.blueprints.batch.ArangoDBBatchGraph;
import com.arangodb.blueprints.client.ArangoDBConfiguration;
import com.arangodb.entity.EdgeDefinitionEntity;
import com.arangodb.entity.GraphEntity;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

/**
 * try arangodb
 * Created by DmitriyS on 26/06/2015.
 */
public class T009 {

    public static void main(String[] args) {
        ArangoConfigure configure = new ArangoConfigure();
        configure.init();
        ArangoDriver arangoDriver = new ArangoDriver(configure);

        String dbName = "graph_test_db";
        try {
            arangoDriver.deleteDatabase(dbName);
        } catch (Exception e) {
            System.out.println("Failed to drop database " + dbName + "; " + e.getMessage());
        }
        try {
            arangoDriver.createDatabase(dbName);
            System.out.println("Database created: " + dbName);
        } catch (Exception e) {
            System.out.println("Failed to create database " + dbName + "; " + e.getMessage());
        }
        arangoDriver.setDefaultDatabase(dbName);

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



        ArangoDBConfiguration config = new ArangoDBConfiguration();
        config.setBatchSize(1000);

        config.setConnectionTimeout(5000); // connection timeout in ms (default: 3000)
        config.setConnectionTimeout(120000); // socket timeout in ms (default: 30000)
        config.setStaleConnectionCheck(true); // whether or not to turn on stale connection checking (default: false)

        String graphName = "Space";
        String vertices = "Person";
        String edges = "CREATED";
        ArangoDBBatchGraph graph = null;
        try {
            graph = new ArangoDBBatchGraph(config, graphName, vertices, edges);
        } catch (ArangoDBGraphException e) {
            e.printStackTrace();
        }


        final long count = 1_0_000;
        StopWatch watch = new StopWatch();


        watch.start("insert God user");
        Vertex a = graph.addVertex(null);
        a.setProperty("name","God");
        watch.stop();

        watch.start("insert " + count + " users and add relationship with God");
        final ArangoDBBatchGraph graph1 = graph;
        LongStream.rangeClosed(1L, count).forEach(userId -> {
            Vertex b = graph1.addVertex(null);
            b.setProperty("name", "user #" + userId);
            Edge e = graph1.addEdge("e"+userId, a, b, "CREATED");
            System.out.println("test");
        });
        watch.stop();

        System.out.println(watch.prettyPrint());

        graph.shutdown();
    }
}
