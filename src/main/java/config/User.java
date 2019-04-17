package config;

import com.google.common.collect.BiMap;
import net.librec.common.LibrecException;
import net.librec.data.DataModel;
import net.librec.data.model.TextDataModel;
import net.librec.filter.GenericRecommendedFilter;
import net.librec.recommender.Recommender;
import net.librec.recommender.RecommenderContext;
import net.librec.recommender.cf.ItemKNNRecommender;
import net.librec.recommender.cf.UserKNNRecommender;
import net.librec.recommender.item.RecommendedItem;
import net.librec.similarity.CosineSimilarity;
import net.librec.similarity.RecommenderSimilarity;

import java.util.ArrayList;
import java.util.List;

public class User {
    public static void main(String[] args) {
        ConfTest confTest = new ConfTest();
        confTest.conf.set("dfs.data.dir", "F:\\毕业设计\\librectest\\src\\main\\resources\\data");
        confTest.conf.set("data.input.path", "u.data");
        confTest.conf.set("rec.neighbors.knn.number","500");
        confTest.conf.set("rec.recommender.ranking.topn","10");
        confTest.conf.set("rec.recommender.isranking","true");
        //这是每一行对应的属性 user item rating time
        confTest.conf.set("data.column.format", "UIRT");
        DataModel dataModel = new TextDataModel(confTest.conf);
        try {
            dataModel.buildDataModel();
        } catch (LibrecException e) {
            System.out.println(e.getMessage());
        }
        BiMap<String, Integer> userIds = dataModel.getUserMappingData();
        BiMap<String, Integer> itemIds = dataModel.getItemMappingData();
//        Set<String> itemKeys = itemIds.keySet();
//        Iterator<String> iterator = itemKeys.iterator();
//        for (Map.Entry<String, Integer> userId : userIds.entrySet()) {
//            for (Map.Entry<String, Integer> itemId : itemIds.entrySet()) {
//                System.out.println("userid:" + userId.getKey()+","+userId.getValue());
//                System.out.println("itemid:" + itemId.getKey()+","+itemId.getValue());
//            }
//        }
        Recommender itemRecommender = new UserKNNRecommender();
        //这个是设置相似度的，也就是说这个是用来评判两个user相似度高低的键
        confTest.conf.set("rec.recommender.similarity.key","user");
        confTest.conf.set("rec.recommender.similarity.class","user");
        RecommenderSimilarity similarity = new CosineSimilarity();
        similarity.buildSimilarityMatrix(dataModel);

        // build recommender context
        RecommenderContext context = new RecommenderContext(confTest.conf, dataModel, similarity);
        try {
            itemRecommender.recommend(context);
        } catch (LibrecException e) {
            e.printStackTrace();
        }
        //对于user临近的计算返回值可能不是recommendeditem
        List<RecommendedItem> recommendedItems = itemRecommender.getRecommendedList();
        for(RecommendedItem r:recommendedItems){
            System.out.println(r.getUserId()+","+r.getUserId()+","+r.getValue());
        }
        System.out.println("-----------------------------------------------------");
        //得到某个用户的？？
//        GenericRecommendedFilter filter1 = new GenericRecommendedFilter();
//        List<String> userIdList1 = new ArrayList<String>();
//        userIdList1.add("196");
//        filter1.setUserIdList(userIdList1);
//        List<RecommendedItem> recommendedItemList1 = filter1.filter(recommendedItems);
//        for(RecommendedItem r:recommendedItemList1){
//            System.out.println(r.getItemId()+","+r.getUserId()+","+r.getValue());
//        }
//        System.out.println("-----------------------------------------------------");
//        //就TM过滤了一个，太他妈秀了
//        GenericRecommendedFilter filter = new GenericRecommendedFilter();
//        List<String> userIdList = new ArrayList<String>();
//        userIdList.add("196");
//        filter.setUserIdList(userIdList);
//        List<RecommendedItem> recommendedItemList = filter.filter(recommendedItems);
//        for (RecommendedItem item:recommendedItemList) {
//            if (item.getItemId().equals("1070")) continue;
//            System.out.println(item.getItemId()+","+item.getUserId()+","+item.getValue());
//        }
    }

}
