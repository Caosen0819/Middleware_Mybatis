package cn.sen.middleware.mybatis.boot.spring;

import cn.sen.middleware.mybatis.boot.mybatis.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.io.IOException;

/**
 * MapperScannerConfigurer是MyBatis-Spring团队提供的一种用于以自动扫描形式来配置MyBatis中映射器的类，
 * 可以通过配置包路径来自动扫描包接口生成映射器，这使得开发人员可以在编写很少代码的情况下，完成对映射器的配置，从而提高开发效率
 * 在配置MapperScannerConfigurer中主要是加载dao包中的所有dao接口，通过sqlsessionFactory获取sqlsession，然后创建所有dao接口对象，存储在spring容器
 * @Author caosen
 * @Date 2023/4/10 14:47
 */
public class MapperScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    private String basePackage;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        try {// classpath*:cn/bugstack/**/dao/**/*.class
            String packageSearchPath = "classpath*:" + basePackage.replace('.', '/') + "/**/*.class";

            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

            for (Resource resource : resources) {

                MetadataReader metadataReader = new SimpleMetadataReader(resource, ClassUtils.getDefaultClassLoader());

                ScannedGenericBeanDefinition beanDefinition = new ScannedGenericBeanDefinition(metadataReader);
                String beanName = Introspector.decapitalize(ClassUtils.getShortName(beanDefinition.getBeanClassName()));

                beanDefinition.setResource(resource);
                beanDefinition.setSource(resource);
                beanDefinition.setScope("singleton");
                beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
                beanDefinition.setBeanClass(MapperFactoryBean.class);

                BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
                registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }


}
