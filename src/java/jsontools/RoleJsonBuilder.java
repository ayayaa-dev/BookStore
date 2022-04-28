/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontools;

import entity.Role;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Melnikov
 */
public class RoleJsonBuilder {
    public JsonArray getRolesJsonArray(List<Role> listRoles){
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for(int i=0;i<listRoles.size();i++){
            jab.add(getRoleJsonObject(listRoles.get(i)));
        }
        return jab.build();
    }
    public JsonObject getRoleJsonObject(Role role){
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", role.getId());
        job.add("roleName", role.getRoleName());
        return job.build();
    }
}
