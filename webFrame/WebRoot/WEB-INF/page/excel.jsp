<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%@include file="/inc/head.jsp" %>
<script type="text/javascript" src="<%=path %>/script/function.js"></script>
</head>
<body>
<%@include file="/inc/exportExcel.jsp"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="70%" valign="top">
		<div style="overflow:auto" id="exportexcel">
		<table width="100%" border="1" cellspacing="0" cellpadding="0" export="贷款">
			<tbody >
            <tr export="head">
                <th scope="col" rowspan="2" class="th2">机构简称</th>
                <th  colspan="1">详情1</th>
                <th colspan="2">详情2</th>
            </tr>
			<tr class="list_bg" export="head" height="25">

                <th scope="col" class="th2">贷款类型</th>
                <th scope="col" class="th2">贷款金额</th>
                <th scope="col" class="th2">贷款金额</th>
			</tr>
			</tbody>
			<tbody >
			<tr export="detail">
				<td align="left" colspan="4" >常州工学院</td>
			</tr>
			<tr export="detail">
				<td align="left" rowspan="3" >常州工学院</td>
				<td align="right" >对公贷款</td>
                <td align="right" exporttype='double'>100000</td>
                <td align="right" exporttype='double'>100000</td>
			</tr>
			<tr export="detail">
				<td align="right" >对公贷款</td>
                <td align="right" exporttype='double'>100000</td>
                <td align="right" exporttype='double'>100000</td>
			</tr>
			<tr export="detail">
				<td align="right" >对公贷款</td>
                <td align="right" exporttype='double'>100000</td>
                <td align="right" exporttype='double'>100000</td>
			</tr>
			<tr export="detail">
				<td align="left" >常州工学院</td>
				<td align="right" >对公贷款</td>
                <td align="right" exporttype='double'>100000</td>
                <td align="right" exporttype='double'>100000</td>
			</tr>
			<tr export="detail">
				<td align="left" >常州工学院</td>
				<td align="right" >对公贷款</td>
                <td align="right" exporttype='double'></td>
                <td align="right" exporttype='double'>100000</td>
			</tr>
			</tbody>
		</table>
		</div>
		</td>
	</tr>
</table>
</body>
</html>