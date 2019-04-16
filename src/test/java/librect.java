import net.librec.common.LibrecException;
import net.librec.conf.Configuration;
import net.librec.job.RecommenderJob;
import org.junit.Test;

import java.io.IOException;

public class librect {
    @Test
    public void helloWorldTest(){
        Configuration.Resource resource = new Configuration.Resource("rec/cf/itemknn-test.properties");
        Configuration conf = new Configuration();
        conf.set("dfs.data.dir", "../librec/data");
        conf.addResource(resource);
        RecommenderJob job = new RecommenderJob(conf);
        try {
            job.runJob();
        } catch (LibrecException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
