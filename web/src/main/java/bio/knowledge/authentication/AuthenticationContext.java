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

package bio.knowledge.authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.api.ApiKey;
import com.stormpath.sdk.api.ApiKeyBuilder;
import com.stormpath.sdk.api.ApiKeys;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.application.Applications;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.ClientBuilder;
import com.stormpath.sdk.client.Clients;
import com.stormpath.sdk.resource.ResourceException;
import com.stormpath.sdk.tenant.Tenant;

import bio.knowledge.authentication.exceptions.AccountDoesNotExistException;

/**
 * @author Richard
 *
 */
@Component
public class AuthenticationContext {

	// I need a spring component for this.
	@Value("${application.rooturl:}")
	private String ROOT_URL;
	
	@Value("${stormpath.application.name:Knowledge.Bio}")
	private String APPLICATION_NAME;

	@Value("${stormpath.client.apiKey.id:}")
	private String APIKEY_ID;

	@Value("${stormpath.client.apiKey.secret:}")
	private String APIKEY_SECRET;

	private Application application;

	Application getApplication() {
		return application;
	}

	private Client client;

	protected Client getClient() {
		return client;
	}
	
	protected final static String HREF_STUB = "https://api.stormpath.com/v1/accounts/";

	private final static long MILLISECONDS_TO_KEEP_CACHE = (long) 1.8e+6;

	private Map<String, Pair<UserProfile, Long>> cachedProfiles = new HashMap<String, Pair<UserProfile, Long>>();

	public AuthenticationContext() {
	}

	@PostConstruct
	public void initialize() {

		ClientBuilder builder = Clients.builder();

		if (!(APIKEY_ID.isEmpty() || APIKEY_SECRET.isEmpty())) {
			ApiKeyBuilder apiKeyBuilder = ApiKeys.builder();
			apiKeyBuilder.setId(APIKEY_ID);
			apiKeyBuilder.setSecret(APIKEY_SECRET);
			ApiKey apiKey = apiKeyBuilder.build();
			builder.setApiKey(apiKey);
		}

		client = builder.build();

		Tenant tenant = client.getCurrentTenant();
		ApplicationList applications = tenant
				.getApplications(Applications.where(Applications.name().eqIgnoreCase(APPLICATION_NAME)));

		// TODO - verify that ApplicationList always only contains the single
		// desired application(?)
		application = applications.iterator().next();

	}

	/**
	 * Gets a UserProfile object that encapsulates the account corresponding to
	 * the provided identification.
	 * 
	 * @param href
	 *            The id of the account that we wish to retrieve.
	 * @return A UserProfile object that encapsulates the account we wish to
	 *         retrieve.
	 * @throws AccountDoesNotExistException
	 */
	public UserProfile getUserProfile(String href) {
		UserProfile userProfile;

		if (cachedProfiles.containsKey(href)) {
			Pair<UserProfile, Long> pair = cachedProfiles.get(href);

			userProfile = pair.getFirst();

			Long timeCached = pair.getSecond();
			Long elapsedTime = System.currentTimeMillis() - timeCached;

			if (elapsedTime >= MILLISECONDS_TO_KEEP_CACHE) {
				cacheUserProfile(href, userProfile);
			}
		} else {

			try {

				Account account = client.getResource(href, Account.class);
				userProfile = new UserProfile("no@dummy.com", "dummy", "password", new ArrayList<>(), null);
				cachedProfiles.put(href, Pair.of(userProfile, System.currentTimeMillis()));

			} catch (ResourceException e) {
				if (e.getCode() == 404) {
					throw new AccountDoesNotExistException("There is no "
							+ "account with href \"" + href + "\" in the database.");
				} else {
					throw e;
				}
			}
		}

		return userProfile;
	}

	private void cacheUserProfile(String id, UserProfile account) {
		cachedProfiles.put(id, Pair.of(account, System.currentTimeMillis()));
	}
	
	public String getRootURL() {
		return this.ROOT_URL;
	}
}
