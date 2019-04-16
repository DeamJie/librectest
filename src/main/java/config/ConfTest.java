package config;

import com.google.common.collect.BiMap;
import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.data.DataModel;
import net.librec.data.model.TextDataModel;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ConfTest {
    public Configuration conf = new Configuration();


    public static void main(String[] args) {
        int b =22;
        System.out.println(b);
        ConfTest confTest = new ConfTest();
        confTest.conf.set("dfs.data.dir", "F:\\毕业设计\\librectest\\src\\main\\resources\\data");
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
        Iterator<String> iterator = itemKeys.iterator();
        for (Map.Entry<String, Integer> userId : userIds.entrySet()) {
            for (Map.Entry<String, Integer> itemId : itemIds.entrySet()) {
                System.out.println("userid:" + userId.getKey()+","+userId.getValue());
                System.out.println("itemid:" + itemId.getKey()+","+itemId.getValue());
            }
        }
    }
}
