package demo;

import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.vocabulary.ReasonerVocabulary;

public class sanguo_demo {

    public static void main(String[] args) {
        // Register a namespace for use in the demo
        PrintUtil.registerPrefix("person", "http://sanguo/person#");
        PrintUtil.registerPrefix("relation", "http://sanguo/relation#");
        PrintUtil.registerPrefix("rdf", "http://sanguo/rdf#");
        PrintUtil.registerPrefix("gender", "http://sanguo/property#");  // 推理中存在的前缀需要在此声明

        // Create an (RDF) specification of a hybrid reasoner which
        // loads its data from an external file.
        Model m = ModelFactory.createDefaultModel();
        Resource configuration =  m.createResource();
        configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");
        configuration.addProperty(ReasonerVocabulary.PROPruleSet, "demo.rules");

        // Create an instance of such a reasoner
        Reasoner reasoner = GenericRuleReasonerFactory.theInstance().create(configuration);

        // Load test data

        Model data = FileManager.get().loadModel("jena_train.ttl");
        InfModel infmodel = ModelFactory.createInfModel(reasoner, data);

        String personURI = "http://sanguo/person#";
//        Resource carlos = data.getResource(personURI + "黄月英");
//        printInference(infmodel, carlos);

        Resource jose = data.getResource(personURI + "黄承彦");
        printInference(infmodel, jose);

        Resource temp = data.getResource(personURI + "李四");
        printInference(infmodel, temp);
    }

    private static void printInference(InfModel inf, Resource resource) {
        StmtIterator stmts = inf.listStatements(resource,  null, (RDFNode) null);

        while (stmts.hasNext()) {
            Statement stmt = stmts.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            System.out.print("(" + subject.getLocalName());
            System.out.print(", " + predicate.getLocalName() + ", ");
            if (object instanceof Resource) {
                System.out.print(((Resource) object).getLocalName());
            } else {
                System.out.print(" \"" + object.toString() + "\"");
            }

            System.out.print(")\n");
        }
    }

}
