/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-17 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */
package bio.knowledge.dataloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Lance Hannestead
 * @author Richard Bruskiewich
 */
@Service("Concepts")
public class CqlConceptsLoader extends TableLoader {
	
	protected static Logger _logger = LoggerFactory.getLogger(CqlConceptsLoader.class);

	protected CqlConceptsLoader() {	
		
		super("concepts.tsv") ;
		
		this.addQuery("CREATE CONSTRAINT ON (citation:Citation) ASSERT citation.pmid IS UNIQUE");
		this.addQuery(
				  "USING PERIODIC COMMIT 5000 LOAD CSV "
				+ "FROM \"dataFilePath\" AS row fieldterminator '|' "
				+ "WITH row, SPLIT(REPLACE(row[3],\" \",\"-\"), '-') AS date "
				+ "MERGE (citation:Citation:DatabaseEntity:IdentifiedEntity {pmid: row[0]}) "
				+ "ON CREATE SET citation.issn = CASE row[1] WHEN 'null' THEN \"\" ELSE row[1] END, "
				+ "citation.year  = TOINT(date[0]), citation.month = TOINT(date[1]), "
				+ "citation.day   = TOINT(date[2]), citation.name  = \"\", citation.description = \"\", "
				+ "citation.accessionId = \"PMID:\"+row[0], citation.versionDate = timestamp(), "
				+ "citation.version = TOINT('1')"
		);
	}
}
