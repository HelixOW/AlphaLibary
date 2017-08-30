/*
 * Copyright (C) <2017>  <AlphaHelixDev>
 *
 *       This program is free software: you can redistribute it under the
 *       terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.alphahelix.alphalibary.nms.enums;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

import java.io.Serializable;

public enum REnumEquipSlot implements Serializable {

    HELMET(4, 5),
    CHESTPLATE(3, 4),
    LEGGINGS(2, 3),
    BOOTS(1, 2),
    OFF_HAND(0, 1),
    HAND(0, 0);

    private int nmsSlot;
    private int past;

    REnumEquipSlot(int nmsSlot, int past) {
        this.nmsSlot = nmsSlot;
        this.past = past;
    }

    public Object getNmsSlot() {
        try {
            return ReflectionUtil.getNmsClass("EnumItemSlot").getEnumConstants()[past];
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() {
        return "REnumEquipSlot{" +
                "nmsSlot=" + nmsSlot +
                ", past=" + past +
                '}';
    }
}
