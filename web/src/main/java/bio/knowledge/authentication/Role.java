/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
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
package bio.knowledge.authentication;

public enum Role {

    SYSADMIN("Systems Administrator"), 
    ADMIN("Administrator"), 
    LABORATORY("Certified Laboratory"), 
    CURATOR("Data Curator"), 
    REGISTERED_USER("Registered User")
    ;
    
    private final String name ;
    
    Role(String name) {
    	this.name = name ;
    }
    
    public String toString() {
    	return this.name ;
    }
    
    public String getSpringRole() {
    	return "ROLE_"+this.getRoleName() ;
    }
    
	public String getRoleName() { return name ; }

	public static Role[] getAll() {
    	return values();
    }
	
    public static Role lookUp(String roleName) throws UserAuthenticationException {
    	for(Role r: Role.values()) {
    		if(r.getRoleName().equals(roleName))
    			return r ;
    	}
    	throw new UserAuthenticationException("Invalid User Role: "+roleName) ;
    }
    
    public static Role lookUpSpringRole(String roleName) throws UserAuthenticationException {
    	for(Role r: Role.values()) {
    		if(r.getSpringRole().equals(roleName))
    			return r;
    	}
    	throw new UserAuthenticationException("Invalid User Role: "+roleName) ;
    }
}
