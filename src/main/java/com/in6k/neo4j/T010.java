package com.in6k.neo4j;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.intent.OIntentMassiveInsert;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OSchema;
import com.orientechnologies.orient.graph.batch.OGraphBatchInsertBasic;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

/**
 * orientdb example
 * Created by DmitriyS on 02/07/2015.
 */
public class T010 {
    public static void main(String[] args) {
        StopWatch watch = new StopWatch();

        OrientGraphFactory factory = new OrientGraphFactory("plocal:/test_db7");
        factory.declareIntent(new OIntentMassiveInsert());
        OrientGraph txGraph = factory.getTx();

        try {
            Vertex v = txGraph.addVertex(null);
            v.setProperty("name", "God");
        } finally {

        }
        watch.stop();

        if (!watch.isRunning()) return;


        ODatabaseDocumentTx db = new ODatabaseDocumentTx("plocal:/test_db7");
        db.open("admin", "admin");
        final OSchema schema = db.getMetadata().getSchema();
        OClass v = schema.getClass("V");
        OClass e = schema.getClass("E");
        schema.createClass("Person", v);
        schema.createClass("Created", e);
        db.close();
//        OGraphBatchInsert inserter = new OGraphBatchInsert("remote:localhost/test_db", "root", "root");
        OGraphBatchInsertBasic inserter = new OGraphBatchInsertBasic("plocal:/test_db7", "admin", "admin");

        inserter.setParallel(2);

        inserter.begin();

        Map<String, Object> edgeProps = new HashMap<>();

        inserter.setVertexClass("Person");
        inserter.setEdgeClass("Created");

        inserter.createVertex(0L);

        edgeProps.put("name", "God");
//        inserter.setVertexProperties(0l, edgeProps);


        final long count = 1_000_000;
        watch.start("insert " + count + " users and add relationship with God");
        LongStream.rangeClosed(1L, count).forEach(userId -> {
//            edgeProps.put("name", "user #" + userId);
            inserter.createVertex(userId);
//            inserter.setVertexProperties(userId, edgeProps);
            inserter.createEdge(0l, userId);
        });

        watch.stop();

        watch.start("push data into db");
        inserter.end();
        watch.stop();

        System.out.println(watch.prettyPrint());

        if (!watch.isRunning()) return;

        TransactionalGraph graph = new OrientGraph("remote:localhost/test_db", "root", "root");

        try {
            Vertex vPerson = graph.addVertex("class:Person");
            vPerson.setProperty("name", "root");

            watch.start("insert God user");
            Vertex vGod = graph.addVertex("class:Person");
            vPerson.setProperty("name", "God");
            watch.stop();

            Edge edge = graph.addEdge(null, vGod, vPerson, "Created");

            //final long count = 10_000_000;
            watch.start("insert " + count + " users and add relationship with God");
            LongStream.rangeClosed(1L, count).forEach(userId -> {
                if (userId % 1000 == 0) {
                    graph.commit();
                }
                Vertex vUser = graph.addVertex("class:Person");
                vUser.setProperty("name", "user #" + userId);

                Edge edge1 = graph.addEdge(null, vUser, vGod, "Created");
            });
            watch.stop();
            graph.commit();
        } finally {
            watch.start("shutdown");
            graph.shutdown();
            watch.stop();
        }

        System.out.println(watch.prettyPrint());
    }
}
