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
package de.alphahelix.alphalibary.utils;

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SerializationUtil {

    public static <T> String encodeBase64(T instance) {
        return Base64Coder.encodeString(JSONUtil.toJson(instance));
    }

    public static <T> T decodeBase64(String base64, Class<T> identifier) {
	    return JSONUtil.getGson().fromJson(Base64Coder.decodeString(base64), identifier);
    }
}
