package webFrame.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;



public class LargeXML {
	
	private File file ;
	
	private OutputFormat format ;
	
	private XMLWriter  writer;
	
	private OutputStream out;
	
	
	public LargeXML() {
	}
	
	public LargeXML(File file) {
		this.file = file;
	}
	
	public LargeXML(File file, OutputFormat format) {
		this.file = file;
		this.format = format;
	}
	
	public LargeXML(String file, OutputFormat format) {
		this.file = new File(file);
		this.format = format;
	}
	
	public LargeXML(OutputStream out, OutputFormat format) {
		this.out = out;
		this.format = format;
	}
	
	public void createXML() throws IOException{
		Document dom = DocumentHelper.createDocument();
		if(format == null){
			if(file == null){
				writer = new XMLWriter(new OutputStreamWriter(out));
			}else{
				writer = new XMLWriter(new FileWriter(file));
			}
		}else{
			if(file == null){
				writer = new XMLWriter(new OutputStreamWriter(out, format.getEncoding()), format);
			}else{
				writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(file), format.getEncoding()), format);
			}
		}
		writer.setEscapeText(false);  //设置不解析"<>"等特殊符号
		writer.write(dom);
	}
	
	public void write(String element) throws IOException{
		writer.write(element);
	}
	
	public void write(Object obj) throws IOException{
		if(obj instanceof Element){
			writer.write((Element) obj);
		}else if(obj instanceof ProcessingInstruction){
			writer.write((ProcessingInstruction) obj);
		}else if(obj instanceof Namespace){
			writer.write((Namespace) obj);
		}else if(obj instanceof Attribute){
			writer.write((Attribute) obj);
		}else if(obj instanceof CDATA){
			writer.write((CDATA) obj);
		}else if(obj instanceof Entity){
			writer.write((Entity) obj);
		}else if(obj instanceof Comment){
			writer.write((Comment) obj);
		}else if(obj instanceof Text){
			writer.write((Text) obj);
		}else if(obj instanceof Node){
			writer.write((Node) obj);
		}else {
			writer.write(obj);
		}
	}
	
	public void readXML(String elementPath,ElementHandler handler) throws Exception{
		SAXReader reader = new SAXReader();
		reader.addHandler(elementPath, handler);
		reader.read(new FileReader(file));
	}
	
	public void complete(){
		if(out != null){
			try {
				out.close();
			} catch (IOException e) {
				out = null;
			}
		}
		if(writer != null){
			try {
				writer.close();
			} catch (IOException e) {
				writer = null;
			}
		}
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public OutputFormat getFormat() {
		return format;
	}

	public void setFormat(OutputFormat format) {
		this.format = format;
	}

	public XMLWriter getWriter() {
		return writer;
	}

	public void setWriter(XMLWriter writer) {
		this.writer = writer;
	}

	public static void main(String [] args) throws Exception {
		long a = System.currentTimeMillis();
		 OutputFormat format = OutputFormat.createPrettyPrint();
		 format.setEncoding("GBK");
		 format.getEncoding();
		LargeXML xml = new LargeXML("d:\\aa.xml", format);
		xml.createXML();
		xml.write("<students>");
		for(int i=0; i<10; i++){
//			Element e =  DocumentHelper.createElement("student").addAttribute("name", "panmg").addText("你好"); //100w  2312ms
			String e = "<student sex=\"男\">panmg</student>"; //   100w  1422ms
			xml.write(e);
		}
		xml.write("</students>");
		xml.complete();
		
		
		/*LargeXML xml = new LargeXML(new File("d:\\aa.xml"));
		xml.readXML("/students/student", new ElementHandler() {

			@Override
			public void onStart(ElementPath arg0) {
			}

			@Override
			public void onEnd(ElementPath arg0) {
				Element row = arg0.getCurrent();
				System.out.println(row.getTextTrim()+ "-->"+row.attributeValue("sex")+"-->"+ row.getName());
				row.detach();
				row=null;

			}
		});*/
		
		System.out.println(System.currentTimeMillis()-a);
		
	}

}
