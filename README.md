# Policy Database on [Google Datastore](https://cloud.google.com/datastore)


This represents a port of a section of an existing application which used [Neo4j](https://neo4j.com/) as data persistence and an attempt to replicate the functionality of a graph database. The rewrite was part of an investigation into using [Google App Engine](https://cloud.google.com/appengine/) to host the application in place of a dedicated Java server instance. It is hosted here as an Eclipse project using Maven so it can be imported and verified by running the [tests](https://github.com/srbaird/PolicyComponentsForDataStore/blob/master/src/test/java/com/bac/policydsentitycomponent/entity/AllTests.java).

The example represented here is the ability to dynamically specify an application level schema on top of a [NoSQL](https://en.wikipedia.org/wiki/NoSQL) instance. 

___

## Background

The aim of the original application was to represent data as a set of simple components that could then be composed to form larger data items in the manner of the [composite](https://en.wikipedia.org/wiki/Composite_pattern) pattern. Each component has a minimal set of common fields, principally an identity and a 'data type'. Further attributes are specified in subclasses of the basic component. An example would be a image which might further specify an image type, size and the binary representation of the image. Another type might be a section of text. These then could be combined into a document type and so on.

 A [graph](https://en.wikipedia.org/wiki/Graph_database) database uses nodes and edges to represent data where the edges in this instance are the relationships between the individual data components. The relationships are primarily composition in this instance though that is a matter for the application designer. 

#### Example

Given a simple document composed of Sections which have Text and Images 

<p align="center">
<img src="https://github.com/srbaird/PolicyComponentsForDataStore/blob/master/documents/Document Structure Example.jpg" alt="Document Example"  >
</p>

This might be represented in a graph database as a series of nodes where the relationship between each node indicates how an action applied to a node might also be applied to related nodes. For instance, deleting a Section might require that all the In-line text nodes are deleted but not the Image. If the document was an invoice composed of sections and order lines then applying a 10% discount to a Section might then apply that function to Order Lines within the Section.

 <p align="center">
<img src="https://github.com/srbaird/PolicyComponentsForDataStore/blob/master/documents/Document Graph Example.jpg" alt="Graph Example"  >

## Policy

The concept of a 'Policy' is to control the types of relationships permitted between data types in an application. The 'Datatype' is the fundamental description of a each component and a simple set of examples is [provided](https://github.com/srbaird/PolicyComponentsForDataStore/blob/master/src/main/java/com/bac/policydsentitycomponent/external/EntityComponentDataType.java). For this part of the application each of these Datatypes are wrapped in a component (which itself has its own particular Datatype) and relationships are then created between them. This metadata is then used in the body of the application to control which relationships are permitted.

 <p align="center">
<img src="https://github.com/srbaird/PolicyComponentsForDataStore/blob/master/documents/Policy Graph Example.jpg" alt="Graph Example"  >

In the above example the policy dictates that an Image may be assigned a Tag and a Tag may be composed of Tags, in other words they can form a hierarchy. This then informs all decisions made in the larger application context.

## Implementation

#### Project

Most of the classes originate from other projects but are included here to enable packaging. All classes in the [external](https://github.com/srbaird/PolicyComponentsForDataStore/tree/master/src/main/java/com/bac/policydsentitycomponent/external) package have been sourced from a larger project. 

#### Data Access

The [data accessor](https://github.com/srbaird/PolicyComponentsForDataStore/blob/master/src/main/java/com/bac/policydsentitycomponent/external/PolicyComponentDAO.java) requirements for this example are a subset of a larger project functionality and are implemented as an adapter. There is no requirement to deal with issues such as reverse relationships, truncating recursive relationships and cascading deletes which are handled in the the main [class](https://github.com/srbaird/PolicyComponentsForDataStore/blob/master/src/main/java/com/bac/policydsentitycomponent/access/AbstractDataAccessor.java).

#### Nodes

Data access is implemented using [Objectify](https://github.com/objectify/objectify). Nodes store their relationships as collections of references to other nodes and these are resolved with the [@Load](https://github.com/objectify/objectify/wiki/Entities) annotation.

#### Java

Initially written in Java 7 and subsequently enhanced to use some Java 8 features.

___



