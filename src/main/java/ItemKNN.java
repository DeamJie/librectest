import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.data.DataModel;
import net.librec.data.model.TextDataModel;
import net.librec.eval.RecommenderEvaluator;
import net.librec.eval.rating.RMSEEvaluator;
import net.librec.recommender.Recommender;
import net.librec.recommender.RecommenderContext;
import net.librec.recommender.cf.ItemKNNRecommender;
import net.librec.recommender.item.RecommendedItem;
import net.librec.similarity.PCCSimilarity;
import net.librec.similarity.RecommenderSimilarity;

import java.util.List;

/**
 * confTest.conf.set("dfs.data.dir", "C:\\Users\\caona\\Desktop\\librec\\src\\main\\resources\\data");
 *         confTest.conf.set("data.input.path", "u.data");
 *         confTest.conf.set("data.column.format", "UIRT");
 *         confTest.conf.set("rec.recommender.similarity.key" ,"user");
 *         confTest.conf.set("rec.neighbors.knn.number","5");
 *         记得更改configuration现在每次每个用户只能有10个推荐
 */
public class ItemKNN {
    public static void main(String[] args) {

        Configuration configuration = new Configuration();
        configuration.set("data.input.path","u.data");
        configuration.set("dfs.data.dir", "C:\\Users\\caona\\Desktop\\librec\\src\\main\\resources\\data");
        configuration.set("data.column.format", "UIRT");
        //配置每个用户推荐的个数
        configuration.set("rec.recommender.ranking.topn","1");
        //创建datamodel
        DataModel dataModel = new TextDataModel(configuration);
        try {
            dataModel.buildDataModel();
        } catch (LibrecException e) {
            e.printStackTrace();
        }

        //创建context
        RecommenderContext context = new RecommenderContext(configuration,dataModel);

        //定义相似的算法
        configuration.set("rec.recommender.similarity.key" ,"item");
        RecommenderSimilarity similarity = new PCCSimilarity();
        similarity.buildSimilarityMatrix(dataModel);
        context.setSimilarity(similarity);

        //定义推荐算法
        configuration.set("rec.neighbors.knn.number", "5");
        Recommender recommender = new ItemKNNRecommender();
        recommender.setContext(context);
        RecommenderEvaluator evaluator = new RMSEEvaluator();


        //推荐并且得到resultList
        try {
            recommender.recommend(context);
        } catch (LibrecException e) {
            e.printStackTrace();
        }
        //这个必须要放在recommend的后面不然空指针
        try {
            recommender.evaluate(evaluator);
        } catch (LibrecException e) {
            e.printStackTrace();
        }
        List<RecommendedItem> resultList = recommender.getRecommendedList();
        for(RecommendedItem r : resultList){
            System.out.println(r.getUserId()+","+r.getItemId()+","+r.getValue());
        }

    }
}
