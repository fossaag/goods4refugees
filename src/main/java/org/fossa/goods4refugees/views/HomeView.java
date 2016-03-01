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

package org.fossa.goods4refugees.views;

import io.dropwizard.views.View;

import java.util.List;

import org.fossa.goods4refugees.core.Hilfsgut;
import org.fossa.goods4refugees.core.Ort;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;

public class HomeView extends View {
	
	private final ImmutableMap<String,String> environmentConfig;
	private final List<Ort> orte;
	private final List<Hilfsgut> lastFive;
	private final int countOrte;
	private final int countHilfsgut;
	private final int countAbgabestelle;
    
    public enum Template{
    	MUSTACHE("mustache/home.mustache");
    	
    	private String templateName;
    	private Template(String templateName){
    		this.templateName = templateName;
    	}
    	
    	public String getTemplateName(){
    		return templateName;
    	}
    }

    public HomeView(HomeView.Template template, ImmutableMap<String,String> environmentConfig, List<Ort> orte, List<Hilfsgut> lastFive, int countHilfsgut, int countAbgabestelle, int countOrte) {
        super(template.getTemplateName(), Charsets.UTF_8);
        this.environmentConfig = environmentConfig;
        this.orte = orte;
        this.lastFive = lastFive;
        this.countOrte = countOrte;
        this.countHilfsgut = countHilfsgut;
        this.countAbgabestelle = countAbgabestelle;
    }
    
	public List<Ort> getOrte() {
    	return orte;
    }
    
    public List<Hilfsgut> getLastFive() {
		return lastFive;
	}
    
    public int getCountOrte() {
    	return countOrte;
    }

	public String getUrlStylesheet() {
		return environmentConfig.get("urlStylesheet");
	}

	public int getCountHilfsgut() {
		return countHilfsgut;
	}

	public int getCountOrganisationen() {
		return countAbgabestelle;
	}
}