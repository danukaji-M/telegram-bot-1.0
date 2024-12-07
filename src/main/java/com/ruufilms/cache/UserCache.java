package com.ruufilms.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruufilms.Beans.User;
import com.ruufilms.DAO.UserDAO;
import com.ruufilms.config.RedisConfig;
import com.ruufilms.exception.RedisException;
import redis.clients.jedis.Jedis;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserCache {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Save User with Indexes
    public static void saveUser(User user) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            // Serialize User object to JSON
            String userJson = objectMapper.writeValueAsString(user);

            // Store user by ID
            jedis.set("user:" + user.getId(), userJson);

            // Create indexes
            jedis.sadd("user:index:username:" + user.getUsername().toLowerCase(), user.getId());
            jedis.sadd("user:index:type:" + user.getUser_type_type_id(), user.getId());

            // Index by groups
            if (user.getGroups() != null) {
                for (String group : user.getGroups()) {
                    jedis.sadd("user:index:group:" + group.toLowerCase(), user.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Search by Username
    public static List<User> searchByUsername(String username) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            Set<String> userIds = jedis.smembers("user:index:username:" + username.toLowerCase());
            return getUsersFromIds(userIds);
        }
    }

    // Search by User Type
    public static List<User> searchByUserType(int userType) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            Set<String> userIds = jedis.smembers("user:index:type:" + userType);
            return getUsersFromIds(userIds);
        }
    }

    // Search by Group
    public static List<User> searchByGroup(String group) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            Set<String> userIds = jedis.smembers("user:index:group:" + group.toLowerCase());
            return getUsersFromIds(userIds);
        }
    }

    // Helper: Fetch Users from Redis by IDs
    private static List<User> getUsersFromIds(Set<String> ids) {
        List<User> users = new ArrayList<>();
        try (Jedis jedis = RedisConfig.getJedis()) {
            for (String id : ids) {
                String userJson = jedis.get("user:" + id);
                if (userJson != null) {
                    users.add(objectMapper.readValue(userJson, User.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
    public static void main(String[] args) {
        // Create sample users
        User user1 = new User();
        user1.setId("1");
        user1.setUsername("Alice");
        user1.setCreate_time(new Timestamp(System.currentTimeMillis()));
        user1.setUser_type_type_id(1);
        user1.setGroups(List.of("admin", "editor"));

        User user2 = new User();
        user2.setId("2");
        user2.setUsername("Bob");
        user2.setCreate_time(new Timestamp(System.currentTimeMillis()));
        user2.setUser_type_type_id(2);
        user2.setGroups(List.of("editor", "viewer"));

        // Save users
        UserCache.saveUser(user1);
        UserCache.saveUser(user2);

        // Search users by username
        List<User> usersByUsername = UserCache.searchByUsername("Alice");
        for(User name : usersByUsername){
            System.out.println("Username is Alice" + name.getUsername());
        }

        // Search users by group
        List<User> usersByGroup = UserCache.searchByGroup("editor");
        for(User user : usersByGroup){
            System.out.println("Users in group 'editor': "+ user.getUsername());

        }
    }
    public static void updateUser(User updatedUser) {
        try (Jedis jedis = RedisConfig.getJedis()) {
            // Fetch the current user data
            String userJson = jedis.get("user:" + updatedUser.getId());
            if (userJson == null) {
                saveUser(updatedUser);
                System.out.println("User not found in cache");
                return; // Exit if the user doesn't exist in cache
            }

            // Deserialize current user data
            User currentUser = objectMapper.readValue(userJson, User.class);

            // Remove old indexes if indexed fields have changed
            if (!currentUser.getUsername().equalsIgnoreCase(updatedUser.getUsername())) {
                jedis.srem("user:index:username:" + currentUser.getUsername().toLowerCase(), currentUser.getId());
                jedis.sadd("user:index:username:" + updatedUser.getUsername().toLowerCase(), updatedUser.getId());
            }

            if (currentUser.getUser_type_type_id() != updatedUser.getUser_type_type_id()) {
                jedis.srem("user:index:type:" + currentUser.getUser_type_type_id(), currentUser.getId());
                jedis.sadd("user:index:type:" + updatedUser.getUser_type_type_id(), updatedUser.getId());
            }

            if (!currentUser.getGroups().equals(updatedUser.getGroups())) {
                // Remove old group indexes
                if (currentUser.getGroups() != null) {
                    for (String group : currentUser.getGroups()) {
                        jedis.srem("user:index:group:" + group.toLowerCase(), currentUser.getId());
                    }
                }
                // Add new group indexes
                if (updatedUser.getGroups() != null) {
                    for (String group : updatedUser.getGroups()) {
                        jedis.sadd("user:index:group:" + group.toLowerCase(), updatedUser.getId());
                    }
                }
            }

            // Save the updated user
            String updatedUserJson = objectMapper.writeValueAsString(updatedUser);
            jedis.set("user:" + updatedUser.getId(), updatedUserJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

