package com.datatable.restApps;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author Ashok
 */
@ApplicationPath("/")
public class AnotherApplication extends Application {

	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<>();
		singletons.add(new AnotherRestService());
		return singletons;
	}
}
