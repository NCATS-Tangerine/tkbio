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
package bio.knowledge.model.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.NodeEntity;

import bio.knowledge.model.Predicate;
import bio.knowledge.model.core.neo4j.Neo4jAbstractIdentifiedEntity;

/**
 * @author Richard
 *
 */
@NodeEntity(label="Predicate")
public class Neo4jPredicate extends Neo4jAbstractIdentifiedEntity implements Predicate {
	
	public Neo4jPredicate() { }
	
	public Neo4jPredicate( String name ) {
		super(name) ;
	}
	
	public Neo4jPredicate( String name, String description ) {
		super(name,description) ;
	}
	
	public Neo4jPredicate( String predicateId, String name, String description ) {
		super( predicateId, name, description ) ;
	}
	
	public class Neo4jPredicateBeacon implements Predicate.PredicateBeacon {

		private Integer beaconId;
		private String edgeLabel;
		private String relation;
		private String description;
		private Boolean negated;
		
		public Neo4jPredicateBeacon(
				Integer beaconId,
				String edgeLabel,
				String relation,
				String description,
				Boolean negated
		) {
			this.beaconId = beaconId;
			this.edgeLabel = edgeLabel;
			this.relation = relation;
			this.description = description;
			this.negated = negated;
		}
		
		@Override
		public Integer getBeaconId() {
			return beaconId;
		}

		@Override
		public String getEdgeLabel() {
			return edgeLabel;
		}

		@Override
		public String getRelation() {
			return relation;
		}

		@Override
		public String getDescription() {
			return description;
		}

		@Override
		public Boolean getNegated() {
			return negated;
		}
		
	}

	private List<PredicateBeacon> beacons = new ArrayList<PredicateBeacon>();
	
	@Override
	public void setPredicatesByBeacons(List<PredicateBeacon> beacons) {
		this.beacons.addAll(beacons);
	}

	@Override
	public List<PredicateBeacon> getPredicatesByBeacons() {
		return beacons;
	}

	@Override
	public void addPredicatesByBeacon(PredicateBeacon beacon) {
		beacons.add(beacon);
	}
	
}
