/*
 * This file is part of WebSiren.
 *
 *  WebSiren is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  WebSiren is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with WebSiren.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.swas.explorer.servlet.search;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.swas.explorer.ecf.Entity;
import net.swas.explorer.oh.handler.Fetcher;
import net.swas.explorer.oh.handler.RuleHandler;
import net.swas.explorer.oh.lo.OntologyLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet is responsible for retrieving the rules based on selected facets from ontology.
 */
@WebServlet("/searchByFacets")
public class SearchByFacets extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;

	private final static Logger log = LoggerFactory.getLogger(SearchByFacets.class);
	private RuleHandler handler = null;
	private OntologyLoader loader = null;      
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchByFacets() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		this.loader = OntologyLoader.getOntLoader(getServletContext());
		this.handler = new RuleHandler(loader);
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		log.info( "searchByFacets' GET called .... " );
		
		String userID = request.getParameter( "userID" );
		String value = request.getParameter( "value");
		List<Entity> ruleList = handler.getRuleByFacets( 
				Fetcher.getConceptElementMap(value), userID);
		
		for(Entity entity:ruleList){
			log.info( "Entity : " + entity.toString() );
		}
		
		request.setAttribute( "ruleList", ruleList);
		RequestDispatcher rd = request.getRequestDispatcher("/ruleList.jsp");
        rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
