package com.example.mymessenger.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.mymessenger.models.User;

import java.util.List;
import java.util.Observable;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<User> userList);

    @Query("select * from users_table")
    Single<List<User>> getAllUsers();

    @Query("select * from users_table WHERE id != :myId")
    Single<List<User>> getAllUsersButMe(String myId);

    @Query("SELECT * FROM users_table WHERE id == :uId")
    Single<User> getUser(String uId);

    @Query("SELECT * FROM users_table WHERE name LIKE :input AND id != :myId")
    Single<List<User>> searchUser(String input, String myId);

    @Query("delete from users_table")
    void deleteAll();
}
