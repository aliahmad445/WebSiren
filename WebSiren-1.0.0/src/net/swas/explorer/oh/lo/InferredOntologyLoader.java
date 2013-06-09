package net.swas.explorer.oh.lo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;

public class InferredOntologyLoader {
	
	private static InferredOntologyLoader infOntLoader = null;
	private Logger log = LoggerFactory.getLogger(InferredOntologyLoader.class);
	private KBConfiguration configuration = null;
	private InfModel inferredModel = null;
	Reasoner reasoner = null;
	
	/**
	 * Loads the Inferred Ontology Model from the Knowledge Base.
	 * 
	 * @param context  used to read configurations from
	 *  configuration file
	 */
	private InferredOntologyLoader(ServletContext context) {

		File file = null;
		FileInputStream ins;
		reasoner = ReasonerRegistry.getOWLReasoner();		
		log.info("Loading Inferred ontology model");
		this.configuration = KBConfiguration.getInstance(context);

		try {
			file = new File(configuration.getOntologyPath());
			ins = new FileInputStream(file);

			Model schema = ModelFactory.createDefaultModel();
			schema.read(ins, configuration.getRuleEngineNameSpace());
			reasoner= reasoner.bindSchema(schema);
			this.inferredModel = ModelFactory.createInfModel(reasoner, schema);
			
			log.info("Inferred Ontology model loaded");

		} catch (FileNotFoundException e) {
			log.info("OWL File not Found");
			e.printStackTrace();
		}

	}
	
	/**
	 * To get instance of Inferred Ontology Loader. It will initialize object if it does not exists already.
	 * 
	 * @param context  used to read configurations from
	 *  configuration file.
	 */
	public synchronized static InferredOntologyLoader getOntLoader(
			ServletContext context) {
		if (infOntLoader == null) {
			infOntLoader = new InferredOntologyLoader(context);
		}
		return infOntLoader;
	}
	
	/**
	 * To get Knowledge Configurations
	 */
	
	public KBConfiguration getConfiguration() {
		return configuration;
	}

	/**
	 * To get ontology Model
	 */
	public InfModel getInfModel() {
		return inferredModel;
	}

}


