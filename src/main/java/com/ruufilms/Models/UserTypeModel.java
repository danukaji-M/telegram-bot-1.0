package com.ruufilms.Models;

import com.ruufilms.Beans.UserType;
import com.ruufilms.config.DatabaseConfig;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserTypeModel {
    public Map<Integer, UserType> getAllUserTypes(){
        try {
            Map<Integer, UserType> userTypeCache = new HashMap<>();

            ResultSet resultSet = DatabaseConfig.executeQuery("SELECT * FROM user_type");
            while (resultSet.next()) {
                int id = resultSet.getInt("type_id");
                String name = resultSet.getString("name");
                UserType userType = new UserType();
                userType.setTypeId(id);
                userType.setName(name);

                userTypeCache.put(id, userType);
            }

            return userTypeCache;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
