import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.data.DataModel;
import net.librec.data.model.TextDataModel;
import net.librec.eval.RecommenderEvaluator;
import net.librec.eval.rating.RMSEEvaluator;
import net.librec.recommender.Recommender;
import net.librec.recommender.RecommenderContext;
import net.librec.recommender.cf.UserKNNRecommender;
import net.librec.recommender.item.RecommendedItem;
import net.librec.similarity.PCCSimilarity;
import net.librec.similarity.RecommenderSimilarity;

import java.util.List;

public class UserKNN {
    public static void main(String[] args) {

        Configuration configuration = new Configuration();
        configuration.set("data.input.path","u.data");
        configuration.set("dfs.data.dir", "C:\\Users\\caona\\Desktop\\librec\\src\\main\\resources\\data");
        configuration.set("data.column.format", "UIRT");
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
        configuration.set("rec.recommender.similarity.key" ,"user");
        RecommenderSimilarity similarity = new PCCSimilarity();
        similarity.buildSimilarityMatrix(dataModel);
        context.setSimilarity(similarity);

        //定义推荐算法
        configuration.set("rec.neighbors.knn.number", "5");
        Recommender recommender = new UserKNNRecommender();
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
