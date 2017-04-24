/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
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
package bio.knowledge.model.core.neo4j;

import org.neo4j.ogm.annotation.NodeEntity;

import bio.knowledge.model.core.DataFile;
import bio.knowledge.model.core.IdentifiedEntity;

@NodeEntity(label="DataFile")
public class Neo4jAbstractDataFile 
	extends Neo4jAbstractIdentifiedEntity implements DataFile {

	public Neo4jAbstractDataFile() {
        super();
    }

    public Neo4jAbstractDataFile( String prefix, IdentifiedEntity entity ) {
		super(entity.getName());
		setDataType(entity) ;
		this.fileName = standardFileName(prefix, entity);
	}
    
	private String fileName;
	
    private String dataType;
  
	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.DataFile#getFileName()
	 */
	@Override
	public String getFileName() {
        return this.fileName;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.DataFile#setFileName(java.lang.String)
	 */
	@Override
	public void setFileName(String fileName) {
        this.fileName = fileName;
    }

	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.DataFile#setDataType(bio.knowledge.model.core.IdentifiedEntity)
	 */
	@Override
	public void setDataType(IdentifiedEntity entity){
		this.dataType = entity.getClass().getSimpleName() ;
	}
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.DataFile#setDataType(java.lang.String)
	 */
	@Override
	public void setDataType(String dataType) {
        this.dataType = dataType;
    }
	
	/* (non-Javadoc)
	 * @see bio.knowledge.model.core.DataFile#getDataType()
	 */
	@Override
	public String getDataType() {
        return this.dataType;
    }
	
    /* (non-Javadoc)
	 * @see bio.knowledge.model.core.DataFile#toString()
	 */
    @Override
	public String toString() { return getName() ; }

}
