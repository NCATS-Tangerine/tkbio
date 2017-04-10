/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Scripps Institute (USA) - Dr. Benjamin Good
 *                   Delphinai Corporation (Canada) / MedgenInformatics - Dr. Richard Bruskiewich
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
package bio.knowledge.test.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import bio.knowledge.database.repository.organization.PersonRepository;
import bio.knowledge.model.organization.Person;
import bio.knowledge.test.database.TestConfiguration;

/**
 * @author Richard
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
@Transactional
public class OrganizationTests {
	
	@Autowired 
	PersonRepository personRepository;

	@Test
	@Transactional
	public void testPersonModels() {

		Person richard = new Person("Richard");
		Person ben     = new Person("Ben");
		Person kenneth = new Person("Kenneth");
		Person farzin  = new Person("Farzin");

		System.out.println("Before linking up with Neo4j...");
		for (Person person : new Person[] { richard, ben, kenneth, farzin }) {
			System.out.println(person);
		}
		
		personRepository.save(richard);
		personRepository.save(ben);
		personRepository.save(kenneth);
		personRepository.save(farzin);

		richard = personRepository.findByName(richard.getName());
		richard.worksWith(ben);
		richard.worksWith(kenneth);
		richard.worksWith(farzin);
		personRepository.save(richard);

		kenneth = personRepository.findByName(kenneth.getName());
		kenneth.worksWith(farzin);
		// We already know that Kenneth works with Richard
		personRepository.save(kenneth);

		// We already know Farzin works with Richard and Kenneth

		System.out.println("Lookup each person by name...");
		for (String name : new String[] { richard.getName(), kenneth.getName(), farzin.getName() }) {
			System.out.println(personRepository.findByName(name));
		}

		System.out.println("Looking up who works with Richard...");
		for (Person person : personRepository.findTeammatesByName("Richard")) {
			System.out.println(person.getName() + " works with Richard.");
		}

	}
}
