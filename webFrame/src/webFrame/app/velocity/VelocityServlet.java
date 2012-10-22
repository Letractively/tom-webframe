package webFrame.app.velocity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.util.SimplePool;

import webFrame.report.Log;


public class VelocityServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String CONTENT_TYPE = "default.contentType";

	public static final String DEFAULT_CONTENT_TYPE = "text/html";

	public static final String DEFAULT_OUTPUT_ENCODING = "ISO-8859-1";

	private static String defaultContentType;

	protected static final String INIT_PROPS_KEY = "org.apache.velocity.properties";

	private static final String OLD_INIT_PROPS_KEY = "properties";

	private static SimplePool writerPool = new SimplePool(40);

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		initVelocity(config);
		VelocityServlet.defaultContentType = RuntimeSingleton.getString(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
	}

	protected void initVelocity(ServletConfig config) throws ServletException {
		try {
			Properties props = loadConfiguration(config);
			Velocity.init(props);
		} catch (Exception e) {
			throw new ServletException("Error initializing Velocity: " + e, e);
		}
	}

	protected Properties loadConfiguration(ServletConfig config) throws IOException, FileNotFoundException {
		String propsFile = config.getInitParameter(INIT_PROPS_KEY);
		if (propsFile == null || propsFile.length() == 0) {
			ServletContext sc = config.getServletContext();
			propsFile = config.getInitParameter(OLD_INIT_PROPS_KEY);
			if (propsFile == null || propsFile.length() == 0) {
				propsFile = sc.getInitParameter(INIT_PROPS_KEY);
				if (propsFile == null || propsFile.length() == 0) {
					propsFile = sc.getInitParameter(OLD_INIT_PROPS_KEY);
					if (propsFile != null && propsFile.length() > 0) {
						sc.log("Use of the properties initialization " + "parameter '" + OLD_INIT_PROPS_KEY + "' has " + "been deprecated by '" + INIT_PROPS_KEY + '\'');
					}
				}
			} else {
				sc.log("Use of the properties initialization parameter '" + OLD_INIT_PROPS_KEY + "' has been deprecated by '" + INIT_PROPS_KEY + '\'');
			}
		}


		Properties p = new Properties();
		if(propsFile != null){
			p.load(getServletContext().getResourceAsStream(propsFile));
		}else{
			// 获取模板路径的绝对路径, 获取velocity默认配置
			p.load(getClass().getClassLoader().getResourceAsStream(Velocity.DEFAULT_RUNTIME_PROPERTIES));
			String velocityLoadPath = getServletContext().getRealPath("");
			// 重新设置模板路径
			p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, velocityLoadPath);
			p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
			p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		}
		
		return p;
	}

	public String getPath(HttpServletRequest request) {
		String path = request.getServletPath();
		String info = request.getPathInfo();
		if (info != null) {
			path = path + info;
		}
		return path;
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) {
		long a = System.currentTimeMillis();
		try{
			doRequest(req, resp);
             String vmName = req.getServletPath();
            vmName = vmName.substring(vmName.lastIndexOf("/")+1);
			StringBuffer msg = new StringBuffer("["+(System.currentTimeMillis()-a)).append("ms]")
			.append("VMCONTROL(VelocityServlet.parse:"+vmName+")")
			.append("->IP:").append(req.getRemoteHost());
			Log.writeLog(msg.toString());
		}catch(Exception e){
            e.printStackTrace();
			Log.writeLog("VelocityServlet", e);
		}finally{

		}
	}

	protected void doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Context context = null;
		try {
			/*
			 * first, get a context
			 */

			context = createContext(request, response);

			/*
			 * set the content type
			 */

			setContentType(request, response);

			/*
			 * let someone handle the request
			 */

			Template template = handleRequest(request, response);
			/*
			 * bail if we can't find the template
			 */

			if (template == null) {
				return;
			}

			/*
			 * now merge it
			 */

			mergeTemplate(template, context, response);
		} catch (Exception e) {
			/*
			 * call the error handler to let the derived class do something
			 * useful with this failure.
			 */

			error(request, response, e);
		} finally {
			/*
			 * call cleanup routine to let a derived class do some cleanup
			 */

			requestCleanup(request, response, context);
		}
	}

	protected void requestCleanup(HttpServletRequest request, HttpServletResponse response, Context context) {
        request = null;
        response = null;
        context = null;
	}

	protected void mergeTemplate(Template template, Context context, HttpServletResponse response) throws ResourceNotFoundException, ParseErrorException, MethodInvocationException, IOException,
			UnsupportedEncodingException, Exception {
		ServletOutputStream output = response.getOutputStream();
		VelocityWriter vw = null;
		// ASSUMPTION: response.setContentType() has been called.
		String encoding = response.getCharacterEncoding();

		try {
			vw = (VelocityWriter) writerPool.get();

			if (vw == null) {
				vw = new VelocityWriter(new OutputStreamWriter(output, encoding), 4 * 1024, true);
			} else {
				vw.recycle(new OutputStreamWriter(output, encoding));
			}

			template.merge(context, vw);
		} finally {
			if (vw != null) {
				try {
					/*
					 * flush and put back into the pool don't close to allow us
					 * to play nicely with others.
					 */
					vw.flush();
				} catch (Exception e) {}
				/*
				 * Clear the VelocityWriter's reference to its internal
				 * OutputStreamWriter to allow the latter to be GC'd while vw is
				 * pooled.
				 */
				vw.recycle(null);
				writerPool.put(vw);
			}
		}
	}

	/**
	 * Sets the content type of the response, defaulting to
	 * {@link #defaultContentType} if not overriden. Delegates to
	 * {@link #chooseCharacterEncoding(javax.servlet.http.HttpServletRequest)} to select the
	 * appropriate character encoding.
	 * 
	 * @param request
	 *            The servlet request from the client.
	 * @param response
	 *            The servlet reponse to the client.
	 */
	protected void setContentType(HttpServletRequest request, HttpServletResponse response) {
		String contentType = VelocityServlet.defaultContentType;
		int index = contentType.lastIndexOf(';') + 1;
		if (index <= 0 || (index < contentType.length() && contentType.indexOf("charset", index) == -1)) {
			// Append the character encoding which we'd like to use.
			String encoding = chooseCharacterEncoding(request);
			// RuntimeSingleton.debug("Chose output encoding of '" +
			// encoding + '\'');
			if (!DEFAULT_OUTPUT_ENCODING.equalsIgnoreCase(encoding)) {
				contentType += "; charset=" + encoding;
			}
		}
		response.setContentType(contentType);
		// RuntimeSingleton.debug("Response Content-Type set to '" +
		// contentType + '\'');
	}

	/**
	 * Chooses the output character encoding to be used as the value for the
	 * "charset=" portion of the HTTP Content-Type header (and thus returned by
	 * <code>response.getCharacterEncoding()</code>). Called by
	 * {@link #setContentType(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)} if an
	 * encoding isn't already specified by Content-Type. By default, chooses the
	 * value of RuntimeSingleton's <code>output.encoding</code> property.
	 * 
	 * @param request
	 *            The servlet request from the client.
	 * @return The chosen character encoding.
	 */
	protected String chooseCharacterEncoding(HttpServletRequest request) {
		return RuntimeSingleton.getString(RuntimeConstants.OUTPUT_ENCODING, DEFAULT_OUTPUT_ENCODING);
	}

	/**
	 * Returns a context suitable to pass to the handleRequest() method <br>
	 * <br>
	 * Default implementation will create a VelocityContext object, put the
	 * HttpServletRequest and HttpServletResponse into the context accessable
	 * via the keys VelocityServlet.REQUEST and VelocityServlet.RESPONSE,
	 * respectively.
	 * 
	 * @param request
	 *            servlet request from client
	 * @param response
	 *            servlet reponse to client
	 * 
	 * @return context
	 */
	protected Context createContext(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*
		 * create a new context
		 */

		VelocityContext context = new VelocityContext();
		
		extContext(context,request,response);

		return context;
	}

	protected void extContext(VelocityContext context,HttpServletRequest request, HttpServletResponse response) throws Exception {};

	public Template getTemplate(String name) throws ResourceNotFoundException, ParseErrorException, Exception {
		return RuntimeSingleton.getTemplate(name);
	}

	public Template getTemplate(String name, String encoding) throws ResourceNotFoundException, ParseErrorException, Exception {
		return RuntimeSingleton.getTemplate(name, encoding);
	}

	protected Template handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/*
		 * invoke handleRequest
		 */

		Template t = getTemplate(getPath(request));

		/*
		 * if it returns null, this is the 'old' deprecated way, and we want to
		 * mimic the behavior for a little while anyway
		 */

		if (t == null) {
			throw new Exception("handleRequest(Context) returned null - no template selected!");
		}

		return t;
	}

	protected void error(HttpServletRequest request, HttpServletResponse response, Exception cause) throws ServletException, IOException {
		StringBuffer html = new StringBuffer();
		html.append("<html>");
		html.append("<title>Error</title>");
		html.append("<body bgcolor=\"#ffffff\">");
		html.append("<h2>You know: Error processing the template</h2>");
		html.append("<xmp>");
		StringWriter sw = new StringWriter();
		cause.printStackTrace(new PrintWriter(sw));

		html.append(sw.toString());
		html.append("</xmp>");
		html.append("</body>");
		html.append("</html>");
		response.getOutputStream().print(html.toString());

	}
}
