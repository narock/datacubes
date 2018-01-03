This repository supports work done by <a href="http://orcid.org/0000-0003-4486-9448">Adam Shepherd</a> and myself in the paper: <a href="http://www.tandfonline.com/doi/full/10.1080/20964471.2017.1397408">Semantics all the way down: the Semantic Web and open science in big earth data, Big Earth Data, Volume 1, 2017, Issue 1-2</a>

The file src/test/java/TestWorkflow.java provides an example of retrieving data using the ontology instances, reading that data into a datacube, and executing a query over the datacube. SPARQL queries are implemented via Java methods and can be found in src/main/java/cubeQueries. 

The ontology instances can be found in ontologies/owl/BCO-DMO_examples/ and are descriptions of these two datasets:
https://www.bco-dmo.org/dataset/552076
https://www.bco-dmo.org/dataset/2333

