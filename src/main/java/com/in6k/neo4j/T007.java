package com.in6k.neo4j;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.arangodb.*;
import com.arangodb.entity.*;
import com.arangodb.util.MapBuilder;

/**
 * try arangodb
 * Created by DmitriyS on 26/06/2015.
 */
public class T007 {

    static class Person {
        String name;
        String title;
        public Person(String name, String title) {
            this.name = name;
            this.title = title;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    static class Publication {
        String title;
        String isbn;
        int pages;
        public Publication(String title, String isbn, int pages) {
            this.title = title;
            this.isbn = isbn;
            this.pages = pages;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }
    }

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

        // Edge definitions of the graph
        List<EdgeDefinitionEntity> edgeDefinitions = new ArrayList<EdgeDefinitionEntity>();

// We start with one edge definition:
        EdgeDefinitionEntity edgeDefHasWritten = new EdgeDefinitionEntity();

// Define the edge collection...
        edgeDefHasWritten.setCollection("HasWritten");

// ... and the vertex collection(s) where an edge starts...
        List<String> from = new ArrayList<String>();
        from.add("Person");
        edgeDefHasWritten.setFrom(from);

// ... and ends.
        List<String> to = new ArrayList<String>();
        to.add("Publication");
        edgeDefHasWritten.setTo(to);

// add the edge definition to the list
        edgeDefinitions.add(edgeDefHasWritten);

// We do not need any orphan collections, so this is just an empty list
        List<String> orphanCollections = new ArrayList<String>();

// Create the graph:
        GraphEntity graphAcademical = null;
        try {
            graphAcademical = arangoDriver.createGraph("Academical", edgeDefinitions, orphanCollections, true);
        } catch (ArangoException e) {
            e.printStackTrace();
        }

        EdgeDefinitionEntity edgeDefHasCited = new EdgeDefinitionEntity();
        edgeDefHasCited.setCollection("HasCited");
        from.clear();
        from.add("Publication");
        edgeDefHasCited.setFrom(from);
        to.clear();
        to.add("Publication");
        edgeDefHasCited.setTo(to);

// add the new definition to the existing graph:
        try {
            arangoDriver.graphCreateEdgeDefinition(graphAcademical.getName(), edgeDefHasCited);
        } catch (ArangoException e) {
            e.printStackTrace();
        }


        try {
            DocumentEntity<Person> person1 = arangoDriver.graphCreateVertex("Academical", "Person", new Person("Bob", "Dr"), true);
            DocumentEntity<Person> person2 = arangoDriver.graphCreateVertex("Academical", "Person", new Person("Floyd", "master of arts"), true);
            DocumentEntity<Publication> publication1 = arangoDriver.graphCreateVertex("Academical", "Publication", new Publication("Surgery for dummies", "1-234-1", 42), true);
            DocumentEntity<Publication> publication2 = arangoDriver.graphCreateVertex("Academical", "Publication", new Publication("Relaxing while working", "5-678-x", 815), true);
            DocumentEntity<Publication> publication3 = arangoDriver.graphCreateVertex("Academical", "Publication", new Publication("Infrasound in art and science", "7-081-5", 60), true);

            arangoDriver.graphCreateEdge("Academical", "HasWritten", null, person1.getDocumentHandle(), publication1.getDocumentHandle());
            arangoDriver.graphCreateEdge("Academical", "HasWritten", null, person2.getDocumentHandle(), publication2.getDocumentHandle());
            arangoDriver.graphCreateEdge("Academical", "HasWritten", null, person2.getDocumentHandle(), publication3.getDocumentHandle());
            arangoDriver.graphCreateEdge("Academical", "HasCited", null, publication1.getDocumentHandle(), publication3.getDocumentHandle());
            arangoDriver.graphCreateEdge("Academical", "HasCited", null, publication1.getDocumentHandle(), publication2.getDocumentHandle());
        } catch (ArangoException e) {
            e.printStackTrace();
        }
    }
}
