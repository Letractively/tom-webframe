<%@ page contentType="text/html; charset=gbk"%>
<%@page import="yk.frame.util.StrUtils"%>
<form action="" name="pagerForm" method="post" target="_self">
<%	
java.util.Map map=request.getParameterMap();
java.util.Iterator it=map.keySet().iterator();
while(it.hasNext()){
	String key=(String)it.next();
	String value=((String[])map.get(key))[0];
	if (!"rowNum".equals(key) && !"pageNum".equals(key) && !"pageCount".equals(key) && !"rowsCount".equals(key)
		&& !"page".equals(key)) {
%>
		<input type="hidden" name="<%=key%>" value="<%=StrUtils.getStringEC(value)%>"/>
<%
	}
}
webFrame.util.PageController _page = (webFrame.util.PageController) request.getAttribute("page");
%>
<table border="0" align="right" cellspacing="0" cellpadding="0" width="100%">
<tr>
	<td align="right" height='20' style="color:#FFF">
		��<%=_page.totalPageCount %>ҳ&nbsp;&nbsp;
		��¼����<%=_page.totalRowCount %>��&nbsp;&nbsp;
		ÿҳ<input name="rowNum" type="text" size="5" value='<%=_page.maxPageRowCount %>' onkeypress="keypress()" style="text-align: center">��	&nbsp;&nbsp;
       	��ת��<input name="pageNum" type="text" size="3" value='<%=_page.currPageNum %>' onkeypress="keypress()" style="text-align: center">ҳ
		<input type="button" class="button" value=" GO " onclick="pagerFormSubmit()">&nbsp;&nbsp;&nbsp;&nbsp;
		<%if (_page.currPageNum > 1) {%>
			<span onclick="firstPage()" style="cursor: hand"><u>��ҳ</u></span>&nbsp;
			<span onclick="prevPage()" style="cursor: hand"><u>��һҳ</u></span>&nbsp;
		<%}%>
		<%if (_page.currPageNum < _page.totalPageCount) {%>
			<span onclick="nextPage()" style="cursor: hand"><u>��һҳ</u></span>&nbsp;
			<span onclick="lastPage()" style="cursor: hand"><u>βҳ</u></span>&nbsp;
		<%}%>
	</td>
</tr>
</table>
</form>
<script language="Javascript">
var form = document.pagerForm;

function pagerFormSubmit() {
	var pageNum = form.pageNum.value;
	if (isNaN(parseInt(pageNum))) {
		alert('ҳ�����!');
		form.pageNum.focus();
		return;
	}
	//alert(form.action);
	form.submit();
}

function keypress() {
	if (window.event.keyCode==13) { 
		pagerFormSubmit();
	}
}

function nextPage() {
	form.pageNum.value = parseInt(form.pageNum.value) + 1;
	pagerFormSubmit();
}

function prevPage() {
	form.pageNum.value = parseInt(form.pageNum.value) - 1;
	pagerFormSubmit();
}

function firstPage() {
	form.pageNum.value = 1;
	pagerFormSubmit();
}

function lastPage() {
	form.pageNum.value = -1;
	pagerFormSubmit();
}
</script>