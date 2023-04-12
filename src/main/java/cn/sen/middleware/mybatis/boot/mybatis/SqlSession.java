package cn.sen.middleware.mybatis.boot.mybatis;

import java.util.List;

/**
 * @Author caosen
 * @Date 2023/4/10 11:10
 */
public interface SqlSession {

    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object parameter);

    <T> List<T> selectList(String statement);

    <T> List<T> selectList(String statement, Object parameter);

    void close();
}
