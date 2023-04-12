package cn.sen.middleware.mybatis.boot.mybatis;

/**
 * @Author caosen
 * @Date 2023/4/10 11:10
 */
public interface SqlSessionFactory {
    SqlSession openSession();
}
