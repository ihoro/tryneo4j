package com.in6k.neo4j;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

/**
 * BatchInserter: many PersonS :CREATED by one God - http://neo4j.com/docs/stable/batchinsert.html
 *
 * It took 4.5 min on igoro's host.
 * $(du -hs data/graph.db) = 8.5 G
 *
 * Created by igoro on 24/06/2015.
 */
public class T004 {
    public static void main(String[] args) {
        StopWatch watch = new StopWatch();

        BatchInserter batchInserter = null;
        try {
            final long count = 10_000_000;

            batchInserter = BatchInserters.inserter("E:\\database\\neo4j");
            //batchInserter = BatchInserters.inserter("/Users/igoro/opt/neo4j/data/graph.db");
            final BatchInserter inserter = batchInserter;

            Label personLabel = DynamicLabel.label("Person");
            Label godLabel = DynamicLabel.label("God");
            RelationshipType created = DynamicRelationshipType.withName("CREATED");
            Map<String, Object> props = new HashMap<>();

            watch.start("insert God user");

            long god = inserter.createNode(props, godLabel);

            watch.stop();

            watch.start("insert " + count + " users and add relationship with God");

            LongStream.rangeClosed(1L, count).forEach(userId -> {
                props.put("name", "user #" + userId);
                long person = inserter.createNode(props, personLabel);

                inserter.createRelationship(god, person, created, new HashMap<>());
            });

            watch.stop();
        } finally {
            if (batchInserter != null) {
                watch.start("shutdown");
                batchInserter.shutdown();
                watch.stop();
            }
        }

        System.out.println(watch.prettyPrint());
    }
}
