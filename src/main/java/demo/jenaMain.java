package demo;

import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.GenericRuleReasonerFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.vocabulary.ReasonerVocabulary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class jenaMain {

    private static void initial() throws IOException {
        String readName = "src/main/resources/rawData/triple_data.txt";
        String writeName = "src/main/resources/triple_data.ttl";
        utils.transform_txt2ttl(readName, writeName);
    }

    private static void jena_infer() throws IOException {
        // 存在的元素标签以及对应的url需要在此声明
        PrintUtil.registerPrefix("entity", "http://jena_demo/entity#");
        PrintUtil.registerPrefix("relation", "http://jena_demo/relation#");

        // Create an (RDF) specification of a hybrid reasoner which
        // loads its data from an external file.
        Model m = ModelFactory.createDefaultModel();
        Resource configuration =  m.createResource();
        configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");
        configuration.addProperty(ReasonerVocabulary.PROPruleSet, "demo.rules");  // 读取规则

        // Create an instance of such a reasoner
        Reasoner reasoner = GenericRuleReasonerFactory.theInstance().create(configuration);

        // Load train data
        Model data = FileManager.get().loadModel("src/main/resources/triple_data.ttl");  // 加载ttl数据
        InfModel infmodel = ModelFactory.createInfModel(reasoner, data); // 生成推理机

        // jena output
        File out_file = new File("src/main/resources/query_result.txt");  // 输出文件
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(out_file), "UTF-8");

        // 获取所需要推理的实体名单
        List<String> entity_list = utils.read_test("src/main/resources/rawData/query.txt");
        String entityURI = "http://jena_demo/entity#";
        for (String entity: entity_list) {
            Resource carlos = data.getResource(entityURI + entity);
            write_inference(infmodel, carlos, writer); // 在推理即中进行实体查询
        }
        writer.close();
    }

    private static void write_inference(InfModel inf, Resource resource, OutputStreamWriter writer) throws IOException {
        StmtIterator stmts = inf.listStatements(resource,  null, (RDFNode) null);  // 获取查询内容
        while (stmts.hasNext()) {
            Statement stmt = stmts.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            writer.write("(" + subject.getLocalName());
            writer.write(", " + predicate.getLocalName() + ", ");
            if (object instanceof Resource) {
                writer.write(((Resource) object).getLocalName());
            } else {
                writer.write(" \"" + object.toString() + "\"");
            }
            writer.write(")\n");
            writer.flush();
        }
    }

    public static void main(String[] args) throws IOException {
        initial();  // 数据预处理，主要将txt转化为ttl格式，用于jena读写
        jena_infer();  // jena推理
    }

}
