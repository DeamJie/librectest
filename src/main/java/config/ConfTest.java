package config;

import com.google.common.collect.BiMap;
import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.data.DataModel;
import net.librec.data.model.TextDataModel;
import net.librec.recommender.Recommender;
import net.librec.recommender.RecommenderContext;
import net.librec.recommender.cf.ItemKNNRecommender;
import net.librec.recommender.item.RecommendedItem;
import net.librec.similarity.PCCSimilarity;
import net.librec.similarity.RecommenderSimilarity;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfTest {
    public Configuration conf = new Configuration();


    public static void main(String[] args) {
        int b =22;
        System.out.println(b);
        ConfTest confTest = new ConfTest();
        confTest.conf.set("dfs.data.dir", "C:\\Users\\caona\\Desktop\\librec\\src\\main\\resources\\data");
        confTest.conf.set("data.input.path", "u.data");
        confTest.conf.set("data.column.format", "UIRT");
        DataModel dataModel = new TextDataModel(confTest.conf);
        try {
            dataModel.buildDataModel();
        } catch (LibrecException e) {
            System.out.println(e.getMessage());
        }
        BiMap<String, Integer> userIds = dataModel.getUserMappingData();
        BiMap<String, Integer> itemIds = dataModel.getItemMappingData();
        Set<String> itemKeys = itemIds.keySet();
//        for (Map.Entry<String, Integer> userId : userIds.entrySet()) {
//            for (Map.Entry<String, Integer> itemId : itemIds.entrySet()) {
//                System.out.println("userid:" + userId.getKey()+","+userId.getValue());
//                System.out.println("itemid:" + itemId.getKey()+","+itemId.getValue());
//            }
//        }

        RecommenderSimilarity similarity = new PCCSimilarity();
        similarity.buildSimilarityMatrix(dataModel);
        confTest.conf.set("rec.recommender.similarity.key" ,"item");
        RecommenderContext context = new RecommenderContext(confTest.conf, dataModel, similarity);
        Recommender itemRecommender = new ItemKNNRecommender();
        try {
            itemRecommender.recommend(context);
        } catch (LibrecException e) {
            e.printStackTrace();
        }
        // 预测出每个用户对于某些电影的分数
        List<RecommendedItem> recommendedItemList = itemRecommender.getRecommendedList();

        System.out.println(recommendedItemList.size());
        for(RecommendedItem r:recommendedItemList){
            System.out.println(r.getUserId()+","+r.getItemId()+","+r.getValue());
        }



    }
}
