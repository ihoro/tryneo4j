package com.in6k.neo4j;

import java.util.Iterator;
import java.util.Map;

import com.arangodb.*;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.CollectionEntity;
import com.arangodb.entity.DocumentEntity;
import com.arangodb.util.MapBuilder;

/**
 * try arangodb
 * Created by DmitriyS on 26/06/2015.
 */
public class T006 {

    public static void main(String[] args) {
        ArangoConfigure configure = new ArangoConfigure();
        configure.init();
        ArangoDriver arangoDriver = new ArangoDriver(configure);

        String dbName = "test_db";
        try {
            arangoDriver.createDatabase(dbName);
            System.out.println("Database created: " + dbName);
        } catch (Exception e) {
            System.out.println("Failed to create database " + dbName + "; " + e.getMessage());
        }
        arangoDriver.setDefaultDatabase(dbName);


        // create collection
        String collectionName = "firstCollection";
        try {
            CollectionEntity myArangoCollection = arangoDriver.createCollection(collectionName);
            System.out.println("Collection created: " + myArangoCollection.getName());
        } catch (Exception e) {
            System.out.println("Failed to create colleciton " + collectionName + "; " + e.getMessage());
        }


        //create doc
        BaseDocument myObject = new BaseDocument();
        myObject.setDocumentKey("myKey");
        myObject.addAttribute("a", "Foo");
        myObject.addAttribute("b", 42);
        try {
            arangoDriver.createDocument(collectionName, myObject);
            System.out.println("Document created");
        } catch (ArangoException e) {
            System.out.println("Failed to create document. " + e.getMessage());
        }


        // read
        DocumentEntity<BaseDocument> myDocument = null;
        BaseDocument myObject2 = null;
        try {
            myDocument = arangoDriver.getDocument(collectionName, "myKey", BaseDocument.class);
            myObject2 = myDocument.getEntity();
            System.out.println("Key: " + myObject2.getDocumentKey());
            System.out.println("Attribute 'a': " + myObject2.getProperties().get("a"));
            System.out.println("Attribute 'b': " + myObject2.getProperties().get("b"));
            System.out.println("Attribute 'c': " + myObject2.getProperties().get("c"));
        } catch (ArangoException e) {
            System.out.println("Failed to get document. " + e.getMessage());
        }

        // update
        try {
            myObject2.addAttribute("c", "Bar");
            arangoDriver.updateDocument(myDocument.getDocumentHandle(), myObject2);
        } catch (ArangoException e) {
            System.out.println("Failed to update document. " + e.getMessage());
        }


        // read the updated document
        try {
            myDocument = arangoDriver.getDocument(collectionName, "myKey", BaseDocument.class);
            System.out.println("Key: " + myObject2.getDocumentKey());
            System.out.println("Attribute 'a': " + myObject2.getProperties().get("a"));
            System.out.println("Attribute 'b': " + myObject2.getProperties().get("b"));
            System.out.println("Attribute 'c': " + myObject2.getProperties().get("c"));
        } catch (ArangoException e) {
            System.out.println("Failed to get document. " + e.getMessage());
        }


        // delete document
        try {
            arangoDriver.deleteDocument(myDocument.getDocumentHandle());
        } catch (ArangoException e) {
            System.out.println("Failed to delete document. " + e.getMessage());
        }

        // create AQL
        try {
            for (Integer i = 0; i < 10; i++) {
                BaseDocument baseDocument = new BaseDocument();
                baseDocument.setDocumentKey(i.toString());
                baseDocument.addAttribute("name", "Homer");
                baseDocument.addAttribute("b", i + 42);
                arangoDriver.createDocument(collectionName, baseDocument);
            }
        } catch (ArangoException e) {
            System.out.println("Failed to create document. " + e.getMessage());
        }


        // execute AQL queries
        try {
            String query = "FOR t IN firstCollection FILTER t.name == @name RETURN t";
            Map<String, Object> bindVars = new MapBuilder().put("name", "Homer").get();
//            CursorResultSet rs = arangoDriver.executeQueryWithResultSet(query, bindVars,
//                    BaseDocument.class, true, 20);

            DocumentCursor<BaseDocument> rs = arangoDriver.executeDocumentQuery(query, bindVars, null, BaseDocument.class);

            Iterator<DocumentEntity<BaseDocument>> iterator = rs.iterator();
            while (iterator.hasNext()) {
                DocumentEntity<BaseDocument> aDocument = iterator.next();
                System.out.println("Key: " + aDocument.getDocumentKey());
            }
        } catch (ArangoException e) {
            System.out.println("Failed to execute query. " + e.getMessage());
        }


        // delete documents with AQL query
        try {
            String query = "FOR t IN firstCollection FILTER t.name == @name "
                    + "REMOVE t IN firstCollection LET removed = OLD RETURN removed";
            Map<String, Object> bindVars = new MapBuilder().put("name", "Homer").get();
            DocumentCursor<BaseDocument> rs = arangoDriver.executeDocumentQuery(query, bindVars, null, BaseDocument.class);

            Iterator<DocumentEntity<BaseDocument>> iterator = rs.iterator();
            while (iterator.hasNext()) {
                DocumentEntity<BaseDocument> aDocument = iterator.next();
                System.out.println("Removed document: " + aDocument.getDocumentKey());
            }

        } catch (ArangoException e) {
            System.out.println("Failed to execute query. " + e.getMessage());
        }
    }
}
