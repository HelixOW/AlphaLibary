/*
 *     Copyright (C) <2016>  <AlphaHelixDev>
 *
 *     This program is free software: you can redistribute it under the
 *     terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.alphahelix.alphalibary.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Util {
	
	public static void unzip(String zipPath, String outputFolder) {
		try {
			ZipFile zipFile = new ZipFile(zipPath);
			Enumeration<?> enu = zipFile.entries();
			
			File folder = new File(outputFolder);
			if(!folder.exists()) {
				folder.mkdir();
			}
			
			while(enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				
				String name = zipEntry.getName();
				
				File file = new File(outputFolder + File.separator + name);
				if(name.endsWith("/")) {
					file.mkdirs();
					continue;
				}
				
				File parent = file.getParentFile();
				if(parent != null) {
					parent.mkdirs();
				}
				
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int length;
				while((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				
				is.close();
				fos.close();
			}
			zipFile.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
