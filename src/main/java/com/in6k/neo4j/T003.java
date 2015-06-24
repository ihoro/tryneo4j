package com.in6k.neo4j;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

/**
 * Mass creation of nodes with BatchInserter - http://neo4j.com/docs/stable/batchinsert.html
 *
 * 100_000 nodes created in a moment (<=1s) on igoro's host.
 * 100_000_000 nodes created in 2 min on igoro's host.
 *
 * Created by igoro on 23/06/2015.
 */
public class T003 {
    public static void main(String[] args) {
        BatchInserter batchInserter = null;
        try {
            batchInserter = BatchInserters.inserter("/Users/igoro/opt/neo4j/data/graph.db");
            final BatchInserter inserter = batchInserter;

            Label personLabel = DynamicLabel.label("Person");
            RelationshipType knows = DynamicRelationshipType.withName("KNOWS");

            Map<String, Object> props = new HashMap<>();

            final long count = 100_000_000;
            LongStream.rangeClosed(1L, count).forEach(userId -> {
                props.put("name", "user #" + userId);
                long nodeId = inserter.createNode(props, personLabel);
            });

        } finally {
            if (batchInserter != null)
                batchInserter.shutdown();
        }
    }
}
