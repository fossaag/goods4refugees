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

import java.util.List;

import org.fossa.goods4refugees.core.Hilfsgut;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;

import io.dropwizard.views.View;

public class HilfsgutView extends View {
	
	private final ImmutableMap<String,String> environmentConfig;
	private final List<Hilfsgut> hilfsgueter;
	private final String hilfsgutName;
	private final String ortname;
    
    public enum Template{
    	MUSTACHE("mustache/hilfsgut.mustache");
    	
    	private String templateName;
    	private Template(String templateName){
    		this.templateName = templateName;
    	}
    	
    	public String getTemplateName(){
    		return templateName;
    	}
    }

    public HilfsgutView(HilfsgutView.Template template, ImmutableMap<String,String> environmentConfig, String ortname, List<Hilfsgut> hilfsgueter, String hilfsgutName) {
        super(template.getTemplateName(), Charsets.UTF_8);
        this.environmentConfig = environmentConfig;
        this.hilfsgueter = hilfsgueter;
        this.ortname = ortname;
        this.hilfsgutName = hilfsgutName;
    }
    
    public List<Hilfsgut> getHilfsgueter() {
    	return hilfsgueter;
    }
    
    public String getOrtname() {
    	return ortname;
    }
    
    public String getHilfsgutName() {
		return hilfsgutName;
	}

	public String getUrlStylesheet() {
		return environmentConfig.get("urlStylesheet");
	}
}