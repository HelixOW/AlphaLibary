/*
 *
 *  * Copyright (C) <2017>  <AlphaHelixDev>
 *  *
 *  *       This program is free software: you can redistribute it under the
 *  *       terms of the GNU General Public License as published by
 *  *       the Free Software Foundation, either version 3 of the License.
 *  *
 *  *       This program is distributed in the hope that it will be useful,
 *  *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *       GNU General Public License for more details.
 *  *
 *  *       You should have received a copy of the GNU General Public License
 *  *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.mysql;

import de.alphahelix.alphalibary.utils.JSONUtil;

import java.util.ArrayList;

public class JSONDatabase {

    private UniqueIdentifier id;
    private MySQLDatabase database;

    public JSONDatabase(UniqueIdentifier id, String table, String database) {
        this.id = id;
        this.database = new MySQLDatabase(table, database);

        this.database.create(this.database.createColumn(id.name(), MySQLAPI.MySQLDataType.VARCHAR, 50), this.database.createColumn("val", MySQLAPI.MySQLDataType.TEXT, 5000));
    }

    public void setValue(String idValue, Object val) {
        if (this.database.contains(id.name(), idValue)) {
            this.database.update(id.name(), idValue, "val", JSONUtil.toJson(val));
        } else {
            this.database.insert(idValue, JSONUtil.toJson(val));
        }
    }

    public <T> T getValue(String idValue, Class<T> define) {
        if (this.database.contains(id.name(), idValue)) {
            return JSONUtil.getValue(this.database.getResult(id.name(), idValue, "val").toString(), define);
        }
        return null;
    }
    
    public <T> ArrayList<T> getValues (Class<T> define) {
        ArrayList<T> vals = new ArrayList<>();
        
        for(String json : this.database.getList("val")) {
            vals.add(JSONUtil.getValue(json, define));
        }
        
        return vals;
    }

    public boolean hasValue(String idValue) {
        return this.database.contains(id.name(), idValue);
    }

    public enum UniqueIdentifier {
        NAME, NUMBER, UUID
    }
}
