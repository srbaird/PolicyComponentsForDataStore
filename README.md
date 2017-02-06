# Policy Database on Google Datastore


This represents a port of part of an existing application which used [Neo4j](https://neo4j.com/) as data persistence and an attempt to replicate the functionality of a graph database. The rewrite was part of an investigation into using [Google App Engine](https://cloud.google.com/appengine/) to host the application in place of a dedicated Java server instance.  

___

## Background

The aim of the original application was to represent data as a set of simple components that could then be composed to form larger data items in the manner of the [composite](https://en.wikipedia.org/wiki/Composite_pattern) pattern. Each component has a minimal set of common fields, principally an identity and a 'data type'. Further attributes are specified in subclasses of the basic component. An example would be a image which might further specify an image type, size and the binary representation of the image. Another type might be a section of text. These then could be combined into a document type and so on.

 A graph](https://en.wikipedia.org/wiki/Graph_database) database uses nodes and edges to represent data where the edges in this instance are the relationships between the individual data components. The relationships are primarily composition in this instance though that is a matter for the application designer. 

#### Example

Given a simple document composed of Sections which have Text and Images 

<p align="center">
<img src="https://github.com/srbaird/PolicyComponentsForDataStore/blob/master/documents/Document Structure Example.jpg" alt="Document Example"  >
</p>

This might be represented in a graph database as a series of nodes where the relationship between each node indicates how an action applied to a node might also be applied to related nodes. For instance, deleting a Section might require that all the In-line text nodes are deleted but not the Image. If the document was an invoice composed of sections and order lines then applying a 10% discount to a Section might then apply that function to Order Lines within the Section.

 <p align="center">
<img src="https://github.com/srbaird/PolicyComponentsForDataStore/blob/master/documents/Document Graph Example.jpg" alt="Graph Example"  >

```
```

___



<p align="center">
<img src="https://github.com/srbaird/AccountServiceApp/blob/master/documents/datamodel.jpg" alt="Data model"  >
</p>


