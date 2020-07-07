package org.jeecg.modules.zzj.mapper;

        import com.baomidou.mybatisplus.core.mapper.BaseMapper;
        import org.jeecg.modules.zzj.entity.ProFile;

        import java.util.Map;

public interface ProFileMapper  extends BaseMapper<ProFile> {
    ProFile searchProfileByNameID(Map<String,String> map);
}
