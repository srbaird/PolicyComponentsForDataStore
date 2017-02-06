# Policy Database on Google Datastore


This represents a port of a section of an existing application which used [Neo4j](https://neo4j.com/) as data persistence and an attempt to replicate the functionality of a graph database. The rewrite was part of an investigation into using [Google App Engine](https://cloud.google.com/appengine/) to host the application in place of a dedicated Java server instance.  

The section represented here is the ability to dynamically specify an application level schema on top of a [NoSQL](https://en.wikipedia.org/wiki/NoSQL) instance. 

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

In the above example the policy dictates that an Image may be assigned a Tag and a Tag may be composed of Tags, in other words they can form a hierarchy. This then informs all decisions made in the larger application context.

 <p align="center">
<img src="https://github.com/srbaird/PolicyComponentsForDataStore/blob/master/documents/Policy Graph Example.jpg" alt="Graph Example"  >

```
```

___



