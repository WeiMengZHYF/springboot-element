package cloud.simple.service.conf;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.autoconfigure.MapperCacheDisabler;

/**
 * <p>
 *
 * @author yangqi
 * @Description </p>
 * @email yangqi@ywwl.com
 * @since 2019/1/2 10:51
 **/
@Configuration
@ConditionalOnProperty(prefix = "spring.devtools.restart", name = "enabled", matchIfMissing = true)
public class RestartConfiguration {

    @Bean
    public MapperCacheDisabler mapperCacheDisabler() {
        return new MapperCacheDisabler();
    }

}