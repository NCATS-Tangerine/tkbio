# License

The MIT License (MIT)

Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
                      STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 
This is the Knowledge.Bio web application front end GUI presentation layer, 
built with the Java Vaadin (Designer) User Interface framework.
 
Authors: Spencer Joel, Ryan Stoppler & Dr. Richard Bruskiewich

## DESCRIPTION

A “datasource” is an API wrapper for the nicknames, gene annotations, or other relevant information that can tag a Concept in TKBio.

Currently these are accessed inline in the front-end, separate from Knowledge Beacons. It is awaiting deprecation until the Beacons replace them.

## EXISTING DATASOURCES

* **MyGene.info.** An open source REST API that provides annotated information about genes.

* **Wikidata.** A wiki dedicated to collecting raw datasets from a variety of scientific endeavours, including biomedical science.

There is a partially implemented wrapper for SPARQL queries.

## REQUIREMENTS
Please refer to the main README.md file in the TKBio root folder.

## TESTING
Testing was done using JUnit tests, to ensure the correct/expected information was being returned from MyGene.info and handled accordingly

## THANKS
Spencer Joel, Ryan Stoppler and Dr. Richard Bruskiewich.