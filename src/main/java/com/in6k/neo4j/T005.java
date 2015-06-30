package com.in6k.neo4j;

import org.neo4j.graphdb.*;
import org.neo4j.rest.graphdb.entity.RestNode;
import org.springframework.data.neo4j.rest.SpringCypherRestGraphDatabase;

import java.util.HashMap;

/**
 * Created by DmitriyS on 25/06/2015.
 */
public class T005 {
    public static void main(String[] args) {
        SpringCypherRestGraphDatabase database = new SpringCypherRestGraphDatabase("http://localhost:7474/db/data/", "neo4j", "neo");

        try (Transaction transaction = database.beginTx()) {
            RestNode godNode = database.getRestAPI().getNodeById(0);
            RestNode user1Node = database.getRestAPI().getNodeById(1);
            System.out.println(user1Node.getProperty("name"));
            Label personLabel = DynamicLabel.label("Person");

//            Iterable<RestNode> nodes = database.getRestAPI().getNodesByLabel("Person");
//            nodes.forEach(restNode -> {
//                System.out.println(restNode.getProperty("name"));
//            });

            org.neo4j.rest.graphdb.query.CypherResult res = database.getRestAPI().query("match (n:Person) return n limit 5", new HashMap<>());

            res.getData().forEach(objects -> {
                System.out.println(objects);
            });

//            Result res = database.execute("match (n {name: 'God'}) return n.name");
//            while (nodes.hasNext()) {
//                Map<String, Object> row = res.next();
//                Node node = nodes.next();
//                System.out.println(node.getProperty("name"));
//                for (Map.Entry<String, Object> column : row.entrySet()) {
//                    System.out.println(column.getKey() + ": " + column.getValue() + "; ");
//                }
//            }
            transaction.success();
        } catch (TransactionTerminatedException ex) {
            ex.printStackTrace();
        } catch (TransactionFailureException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
