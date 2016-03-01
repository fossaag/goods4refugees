/**
 * Copyright (c) 2015 Frank Kaddereit, Jens Vogel, http://www.fossa.de/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.fossa.goods4refugees.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.views.View;

import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.fossa.goods4refugees.views.DankeView;

import com.google.common.collect.ImmutableMap;

@Path("/danke")
@Produces(MediaType.APPLICATION_JSON)
public class DankeResource {
	
	@Context
	private UriInfo uriInfo;

	private final ImmutableMap<String, String> environmentConfig;
    
    public DankeResource(ImmutableMap<String, String> environmentConfig) {
    	this.environmentConfig = environmentConfig;
    }

    @GET
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public View danke() throws URISyntaxException {
    	return new DankeView(DankeView.Template.MUSTACHE, environmentConfig);
    }
    
}
