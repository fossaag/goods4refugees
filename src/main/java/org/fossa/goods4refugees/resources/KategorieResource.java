/**
 * Copyright (c) 2015 Frank Kaddereit, Anne Lachnitt, http://www.fossa.de/
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
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.fossa.goods4refugees.core.Hilfsgut;
import org.fossa.goods4refugees.db.HilfsgutDAO;
import org.fossa.goods4refugees.views.KategorieView;

import com.google.common.collect.ImmutableMap;

@Path("/ort/{ortname}/kategorie/{kategoriename}")
@Produces(MediaType.APPLICATION_JSON)
public class KategorieResource {
	
	@Context
	private UriInfo uriInfo;

	private final ImmutableMap<String, String> environmentConfig;
	private HilfsgutDAO hilfsgutDAO;
    
    public KategorieResource(ImmutableMap<String, String> environmentConfig, HilfsgutDAO hilfsgutDAO) {
    	this.environmentConfig = environmentConfig;
    	this.hilfsgutDAO = hilfsgutDAO;
    }

    @GET
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public View kategorie(@PathParam("ortname") String ortname, @PathParam("kategoriename") String kategoriename) throws URISyntaxException {
    	List<Hilfsgut> hilfsgueter = new ArrayList<Hilfsgut>();
    	for (Hilfsgut hilfsgut : hilfsgutDAO.findAll()) {
    		if (hilfsgut.getKategorie().getName().equals(kategoriename) && hilfsgut.getAbgabestelle().getOrt().getName().equals(ortname)) {
    			hilfsgueter.add(hilfsgut);
    		}
    	}
    	if (hilfsgueter.isEmpty()) {
    		throw new WebApplicationException(404);
    	}
    	return new KategorieView(KategorieView.Template.MUSTACHE, environmentConfig, ortname, kategoriename, hilfsgueter);
    }
}
