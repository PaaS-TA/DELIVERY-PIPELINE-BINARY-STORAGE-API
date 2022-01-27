package paasta.delivery.pipeline.storage.api.config;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AccountFactory;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by hrjin on 2017-06-12.
 */
@Configuration
public class SwiftOsConfig {

    @Autowired
    private Environment env;

    @Bean
    public AccountConfig accountConfig(){

        String tenantName = env.getRequiredProperty("objectStorage.tenantName");
        String username = env.getRequiredProperty("objectStorage.username");
        String password = env.getRequiredProperty("objectStorage.password");
        String authUrl = env.getRequiredProperty("objectStorage.authUrl");

        AccountConfig config = new AccountConfig();
        config.setTenantName(tenantName);
        config.setUsername(username);
        config.setPassword(password);
        config.setAuthUrl(authUrl + "/tokens");
        config.setAuthenticationMethod(AuthenticationMethod.KEYSTONE_V3);
        config.setPreferredRegion("paasta");

        return config;
    }

    @Bean
    public AccountFactory accountFactory(AccountConfig accountConfig){
        return new AccountFactory(accountConfig);
    }

    @Bean
    public Account account(AccountFactory accountFactory){
        return accountFactory.createAccount();
    }

    @Bean
    public Container container(Account account){
        String containerName = env.getRequiredProperty("objectStorage.container");

        Container container = account.getContainer(containerName);
        if(!container.exists()){
            container.create();
            container.makePublic();
        }
        return container;
    }
}
