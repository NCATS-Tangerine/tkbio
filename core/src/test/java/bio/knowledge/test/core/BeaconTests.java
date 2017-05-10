/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Scripps Institute (USA) - Dr. Benjamin Good
 *                   Delphinai Corporation (Canada) / MedgenInformatics - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any concept obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit concepts to whom the Software is
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
package bio.knowledge.test.core;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import bio.knowledge.model.beacon.KnowledgeBeacon;
import bio.knowledge.model.beacon.neo4j.Neo4jKnowledgeBeacon;
import bio.knowledge.service.beacon.KnowledgeBeaconRegistry;

/**
 * @author Richard
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
@Transactional
public class BeaconTests {

	@Autowired 
	KnowledgeBeaconRegistry beaconRegistry;
	
	@Test
	@Transactional
	public void testAddAndRetrieveBeacons() {
		
		beaconRegistry.addKnowledgeBeacon( "Knowledge.Bio Beacon", "KB 3.0 reference implementation", "beacon.medgeninformatics.net" );
		beaconRegistry.addKnowledgeBeacon( "WikiData Beacon", "Garbanzo", "http://garbanzo.sulab.org/translator" );
		
		List<Neo4jKnowledgeBeacon> beacons = beaconRegistry.findAllBeacons();
		
		for(KnowledgeBeacon beacon : beacons) {
			System.out.println("\nBeacon\nId:"+beacon.getId().toString());
			System.out.println("Name:"+beacon.getName());
			System.out.println("Description:"+beacon.getDescription());
			System.out.println("URL:"+beacon.getUri());
		}
	}		

}
