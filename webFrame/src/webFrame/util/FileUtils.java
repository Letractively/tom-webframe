package webFrame.util;

import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import webFrame.app.control.RequestContext;
import webFrame.report.Log;

/**
 * File 工具包,已测试
 * 
 * @author Administrator
 */
public class FileUtils {

	public static String readLine(File _file, String encode) {
		StringBuffer buff = new StringBuffer();
		String s = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(_file), encode));
			while ((s = reader.readLine()) != null) {
				buff.append(s);
			}
		} catch (Exception e) {
			Log.writeLog("FileUtils.readLine", e);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				reader = null;
			}
		}
		return buff.toString();
	}

	public static String streamToStr(InputStream in, String encode) {
		StringBuffer buff = new StringBuffer();
		String s = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, encode));
			while ((s = reader.readLine()) != null) {
				buff.append(s);
			}
		} catch (Exception e) {
			Log.writeLog("FileUtils.readLine", e);
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				reader = null;
			}
		}
		return buff.toString();
	}

	public static InputStream strToStream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}

	public static InputStream ToInStream(ByteArrayOutputStream out) {
		return new ByteArrayInputStream(out.toByteArray());
	}

	/**
	 * 在使用表单传送数据的时候，如果form 加了enctype="multipart/form-data" 这个属性
	 * 不能用request.getParameter()来获取到各个表单元素的值的。
	 * 可以通过XX.do?&exec=upload&name=tomsun的方式来给值 支持单个文件上传,判断上传的inputname
	 * FileUtils.upload(req,"file" ,"D:\Program Files\Tomcat
	 * 6.0\webapps\webFrame\ZTree.rar",10*1024*1024);
	 */
	@Deprecated
	public static int upload(HttpServletRequest _req, String _inputName, String _savePath, int _maxSize) throws Exception {
		int result = 1;

		int i = _savePath.lastIndexOf(File.separator);
		String dir = _savePath.substring(0, i);

		File f = new File(dir);
		f.mkdirs();
		f = null;

		DiskFileUpload du = new DiskFileUpload();
		du.setSizeThreshold(1024);
		du.setRepositoryPath(dir);
		du.setSizeMax(_maxSize);

		List<?> fileList = du.parseRequest(_req);

		if ((fileList == null) || (fileList.size() == 0)) {
			return result; // 文件按大小为0,或无
		}

		for (i = 0; i < fileList.size(); i++) {
			FileItem fileItem = (FileItem) fileList.get(i);
			if (fileItem == null)
				continue;

			if (!fileItem.getFieldName().equals(_inputName))
				continue;

			long size = fileItem.getSize();
			if (size > _maxSize) {
				result = 2; // 过大
				break;
			}

			fileItem.write(new File(_savePath));
			result = 0;

			break;
		}

		return result;
	}

	/**
	 * 支持多文件上传
	 * 
	 * @param _req
	 * @param _upPath
	 * @param _maxSize
	 * @param _suffix
	 *            后缀
	 * @param _encoding
	 *            文件编码
	 * @return 1--没有文件,2--文件过大,0--上传成功
	 * @throws Exception
	 */
	public static List<Map<String, String>> upload(HttpServletRequest _req, String _upPath, int _maxSize, String _suffix, String _encoding) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (!ServletFileUpload.isMultipartContent(_req)) {
			throw new RuntimeException("not multipart/form-data"); // 判断是否是上传文件
		}

		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(20 * 1024); // 设置内存中允许存储的字节数
		factory.setRepository(factory.getRepository()); // 设置存放临时文件的目录
		ServletFileUpload upload = new ServletFileUpload(factory); // 创建新的上传文件句柄
		String edcoding = _encoding.isEmpty() ? "UTF-8" : _encoding;
		upload.setHeaderEncoding(edcoding); // 设置编码
		List<?> formlists = upload.parseRequest(_req); // 获取上传文件集合
		Iterator<?> iter = formlists.iterator(); // 获取上传文件迭代器

		while (iter.hasNext()) {
			Map<String, String> map = new HashMap<String, String>();
			FileItem formitem = (FileItem) iter.next(); // 获取每个上传文件
			if (!formitem.isFormField()) { // 忽略不是上传文件的表单域
				long size = formitem.getSize();
				String name = formitem.getName(); // 获取上传文件的名称
				if (size > _maxSize) { // 如果上传文件大于规定的上传文件的大小
					map.put("result", "2");
					list.add(map);
					break; // 退出程序
				}
				if ((name == null) || (name.isEmpty()) && (size == 0L)) {// 如果上传文件为空
					map.put("result", "1");
					list.add(map);
					continue;
				}

				name = name.substring(name.lastIndexOf(File.separator) + 1);
				map.put("fileName", name);

				File file = new File(_upPath);
				if (!file.exists()) {
					file.mkdir();
				}

				String address = _upPath + File.separator + name; // 创建上传文件的保存地址
				if (!_suffix.isEmpty()) {
					address += "." + _suffix;
				}

				map.put("filePath", address);

				File saveFile = new File(address); // 根据文件保存地址，创建文件
				formitem.write(saveFile); // 向文件写数据

				map.put("result", "0");
				list.add(map);
			}
		}

		return list;
	}

	/**
	 * 读取图片显示在页面
	 * 
	 * @param resContext
	 * @return
	 */
	public static int readImage(RequestContext resContext, InputStream _in) {
		String contentType = "image/*";
		String header[] = { "Cache-Control", "no-cache" };
		try{
			return DownLoadComm(resContext, _in, contentType, header);
		}catch(Exception e){
			Log.writeLog("FileUtils.readImage", e);
			return 1;
		}
	}

	/**
	 * @param resContext
	 * @param _file
	 * @param _encoding
	 *            添加编码
	 * @param _suffix
	 *            添加后缀
	 * @return
	 * @throws Exception
	 */
	public static int downLoad(RequestContext resContext, File _file, String _encoding, String _suffix) {
		InputStream in = null;
		String encoding = _encoding.isEmpty() ? "UTF-8" : _encoding;
		String fileName = _suffix.isEmpty() ? _file.getName() : _file.getName() + "." + _suffix;

		try {
			fileName = URLEncoder.encode(fileName, encoding);
			String contentType = "APPLICATION/OCTET-STREAM";
			String[] header = { "Content-Disposition", "attachment; filename=" + fileName };
			in = new BufferedInputStream(new FileInputStream(_file));
			DownLoadComm(resContext, in, contentType, header);
		} catch (Exception e) {
			Log.writeLog("FileUtils.downLoad", e);
			return 1;
		}
		return 0;
	}

	/**
	 * 下载通用类
	 * 
	 * @param resContext
	 * @param _in
	 * @param _contentType
	 * @param _header
	 * @return
	 * @throws IOException 
	 * @throws IOException 
	 * @throws Exception
	 */
	public static int DownLoadComm(RequestContext resContext, InputStream _in, String _contentType, String _header[]) throws IOException{
		resContext.setContentType(_contentType);
		resContext.setHeader(_header[0], _header[1]);
		return copy(_in, resContext.getOutputStream());
	}

	/**
	 * IO流复制
	 * @param _out
	 * @param _in
	 * @return
	 * @throws Exception
	 */
	public static int copy(InputStream _in, OutputStream _out) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(_in);
			out = new BufferedOutputStream(_out);
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b, 0, b.length)) != -1) {
				out.write(b, 0, len);
			}
			out.flush();
		} catch (Exception e) {
			Log.writeLog("FileUtils.copy", e);
			return 1;
		} finally {
			close(in, out);
		}
		return 0;
	}

	/**
	 * 文件拷贝
	 * 
	 * @param _in
	 * @param _out
	 * @throws Exception
	 */
	public static void transfer(FileInputStream _in, FileOutputStream _out) {
		FileChannel source = null, target = null;
		try {
			source = _in.getChannel();
			target = _out.getChannel();
			long size = source.size();
			source.transferTo(0, size, target);
		} catch (IOException e) {
			Log.writeLog("FileUtils.copy", e);
		} finally {
			close(source, target);
		}
	}

	/**
	 * 文件复制
	 * 
	 * @throws IOException
	 */
	public static void copyFile(File source, File target) throws IOException {
		FileChannel inChannel = new FileInputStream(source).getChannel();
		FileChannel outChannel = new FileOutputStream(target).getChannel();
		ByteBuffer bb = ByteBuffer.allocate(10240);
		try {
			/* 从inChannel文件中读出count bytes ，并写入outChannel文件。 */
			while (inChannel.read(bb) != -1) {
				bb.flip();
				outChannel.write(bb);
				bb.clear(); // prepare for reading;清空缓冲区；
			}
		} catch (IOException e) {
			Log.writeLog("FileUtils.copyFile", e);
		} finally {
			close(inChannel, outChannel);
		}
	}

	/**
	 * 文件压缩,多文件目录压缩
	 * 
	 * @param out
	 *            压缩流,压缩后的文件名
	 * @param f
	 *            压缩文件目录或者压缩文件
	 * @param base
	 *            压缩文件里面的文件夹目录
	 * @throws Exception
	 */
	@Deprecated
	public static void makZip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            base = base.length() == 0 ? "" : base + File.separator;
            for (int i = 0; i < fl.length; i++) {
            	makZip(out, fl[i], base + fl[i].getName());
            }
        } else {
            if (f.isHidden() || f.lastModified() == 0)
                return;
            out.putNextEntry(new ZipEntry(base));

            FileInputStream in = new FileInputStream(f);
            int len = -1;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            in.close();
        }
    }
	

	/**
	 * @param zos
	 *            压缩后的文件
	 * @param file
	 *            压缩文件目录
	 * @throws IOException
	 */
	public static void makFileZip(ZipOutputStream zos, File file) {
		File[] files = file.listFiles();
		InputStream in = null;
		try {
			for (int i = 0; i < files.length; i++) {
				/* 将每个File作为压缩key,读取每个File流 */
				ZipEntry entryitem = new ZipEntry(files[i].getName());

				zos.putNextEntry(entryitem);
				in = new FileInputStream(files[i]);
				byte[] b = new byte[1024];
				int len = -1;
				while ((len = in.read(b)) != -1) {
					zos.write(b, 0, len);
				}
			}
		} catch (Exception e) {
			Log.writeLog("FileUtils.makFileZip", e);
		} finally {
			close(in, zos);
		}
	}
	

	/**
	 * 解压缩一个文件到指定文件夹 支持中文
	 * 
	 * @param zf
	 *            压缩文件
	 * @param folderPath
	 *            解压缩的目标目录
	 */
	public static void unZipFile(ZipFile zf, String folderPath) {
		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		for (Enumeration<?> entries = zf.getEntries(); entries.hasMoreElements();) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			String str = folderPath + File.separator + entry.getName();
			File desFile = new File(str);
			InputStream in = null;
			OutputStream out = null;
			try {
				if (!desFile.exists()) {
					File fileParentDir = desFile.getParentFile();
					if (!fileParentDir.exists()) {
						fileParentDir.mkdirs();
					}
					desFile.createNewFile();
				}
				in = zf.getInputStream(entry);
				out = new FileOutputStream(desFile);
				byte buffer[] = new byte[1024];
				int len;
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
			} catch (Exception e) {
				Log.writeLog("FileUtils.unZipFile", e);
			} finally {
				close(in, out);
			}
		}
	}
	
	/**
	 * 根据zipFile得到每个文件的输出流
	 * 
	 * @param _file
	 * @return
	 * @throws Exception
	 */
	public static Map<String, InputStream> unZipFile(File _file) throws Exception {
		Map<String, InputStream> map = new HashMap<String, InputStream>();
		ZipFile zip = new ZipFile(_file);
		Enumeration<?> zips = zip.getEntries();
		while (zips.hasMoreElements()) {
			ZipEntry entry = ((ZipEntry) zips.nextElement());
			InputStream in = zip.getInputStream(entry);
			String fileName = entry.getName();
			int index = fileName.lastIndexOf(File.separator);
			if (index != -1) {
				fileName = fileName.substring(index + 1);
			}
			map.put(fileName, in);
		}
		return map;
	}
	
	public static void close(InputStream in, OutputStream out) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				in = null;
			}
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				in = null;
			}
		}
	}

	public static void close(FileChannel source, FileChannel target) {

		if (source != null) {
			try {
				source.close();
			} catch (IOException e) {
				source = null;
			}
		}
		if (target != null) {
			try {
				target.close();
			} catch (IOException e) {
				target = null;
			}
		}

	}

	
	public static void main(String args[]) throws Exception {
		long a = System.currentTimeMillis();
		for(int i =0;i<100;i++){
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("d:\\aa.zip")); 
			makZip(zos, new File("d:/aa"), "");  //10 640 
			zos.close(); //只能在下面关闭,否则出错
//			 makFileZip(zos, new File("d:\\aa")); //已测试  594
		}

//		 makZip(zos, new File("d:\\aa1"), ""); //已测试
		
		System.out.println(System.currentTimeMillis()-a);
		
		// System.out.println(unZipFile(new File("d:\\aa.zip"))); //已测
//		 unZipFile(new ZipFile("d:\\aa.zip"), "D:\\aa1"); //已测

	}



}
