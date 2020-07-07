package org.jeecg.modules.zzj.common;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import java.io.File;

/**
 * 定时任务
 */
@Component
@Configurable
public class Timedtasks {

//    @Scheduled(cron = "0 0 23 L * ?")
//    public void omit() {
//        File file = new File("D:\\img\\");//放pdf的地址路径
//        delete(file);
//    }
    private static void delete(File f) {
        File[] fi = f.listFiles();
        for (File file : fi) {
            if (file.isDirectory()) {
                delete(file);
            } else if (file.getName().substring(file.getName().lastIndexOf(".") + 1).equals("pdf")) {
                System.out.println("成功删除文件:" + file.getName());
                file.delete();
            }
        }
    }






}
