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

package bio.knowledge.service.core;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import bio.knowledge.model.core.IdentifiedEntity;

/**
 * @author Richard
 *
 * @param <T>
 */
public interface IdentifiedEntityService<T extends IdentifiedEntity> {
	
	/**
	 * @return a standard ListTablePager implementation for user in Registry mapping.
	 */
	ListTablePager<T> getPager();

	/**
	 * @return a standard ListTableEntryCounter implementation for user in Registry mapping.
	 */
	ListTableEntryCounter getEntryCounter();

	/**
	 * @return a standard ListTablePageCounter implementation for user in Registry mapping.
	 */
	ListTableFilteredHitCounter getHitCounter();

	/**
	 * @return a standard ListTablePageCounter implementation for user in Registry mapping.
	 */
	ListTablePageCounter getPageCounter();

	/**
	 * 
	 * @return
	 */
	List<T> getIdentifiers();

	/**
	 * 
	 * @param pageable
	 * @return
	 */
	Page<T> getIdentifiers(Pageable pageable);

}