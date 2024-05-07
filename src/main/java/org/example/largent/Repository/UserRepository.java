package org.example.largent.Repository;

import org.example.largent.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserByUserId(Integer userId);

    List<User> findUserByParentId(Integer parentId);

    @Query("select user from User user where user.username=?1 and user.password=?2")
    User logIn(String username, String password);

}
