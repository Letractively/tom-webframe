<html>
    <head>
    <title>Table Sort Example</title>
        <script type="text/javascript">
            
            //转换器，将列的字段类型转换为可以排序的类型：String,int,float
            function convert(sValue, sDataType) {
                switch(sDataType) {
                    case "int":
                        return parseInt(sValue);
                    case "float":
                        return parseFloat(sValue);
                    case "date":
                        return new Date(Date.parse(sValue));
                    default:
                        return sValue.toString();
                
                }
            }
            
            //排序函数产生器，iCol表示列索引，sDataType表示该列的数据类型
            function generateCompareTRs(iCol, sDataType) {
        
                return  function compareTRs(oTR1, oTR2) {
                            var vValue1 = convert(oTR1.cells[iCol].firstChild.nodeValue, sDataType);
                            var vValue2 = convert(oTR2.cells[iCol].firstChild.nodeValue, sDataType);
        
                            if (vValue1 < vValue2) {
                                return -1;
                            } else if (vValue1 > vValue2) {
                                return 1;
                            } else {
                                return 0;
                            }
                        };
            }
            
            //排序方法
            function sortTable(sTableID, iCol, sDataType) {
                var oTable = document.getElementById(sTableID);
                var oTBody = oTable.tBodies[0];
                var colDataRows = oTBody.rows;
                var aTRs = new Array;
                
                //将所有列放入数组
                for (var i=0; i < colDataRows.length; i++) {
                    aTRs[i] = colDataRows[i];
                }
                 
                //判断最后一次排序的列是否与现在要进行排序的列相同，是的话，直接使用reverse()逆序
                if (oTable.sortCol == iCol) {
                    aTRs.reverse();
                } else {
                    //使用数组的sort方法，传进排序函数
                    aTRs.sort(generateCompareTRs(iCol, sDataType));
                }
        
                var oFragment = document.createDocumentFragment();
                for (var i=0; i < aTRs.length; i++) {
                    oFragment.appendChild(aTRs[i]);
                }
       
                oTBody.appendChild(oFragment);
                //记录最后一次排序的列索引
                oTable.sortCol = iCol;
            }

        </script>
    </head>
    <body>
        <p>Click on the table header to sort in ascending order.</p>
        <table border="1" id="tblSort">
            <thead>
                <tr>
                    <th rowspan="2" onclick="sortTable('tblSort', 0)"
                        style="cursor:pointer">Last Name</th>
                    <th rowspan="2" onclick="sortTable('tblSort', 1)"
                        style="cursor:pointer">First Name</th>
                    <th rowspan="2" onclick="sortTable('tblSort', 2, 'date')"
                        style="cursor:pointer">Birthday</th>
                    <th
                        >Siblings</th>
                </tr>
            <tr><th style="cursor:pointer" onclick="sortTable('tblSort', 3, 'int')" >第二行</th> </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Smith</td>
                    <td>John</td>
                    <td>7/12/1978</td>
                    <td>2</td>
                </tr>
                <tr>
                    <td>Johnson</td>
                    <td>Betty</td>
                    <td>10/15/1977</td>
                    <td>4</td>
                </tr>
                <tr>
                    <td>Henderson</td>
                    <td>Nathan</td>
                    <td>2/25/1949</td>
                    <td>1</td>
                </tr>
                <tr>
                    <td>Williams</td>
                    <td>James</td>
                    <td>7/8/1980</td>
                    <td>4</td>
                </tr>
                <tr>
                    <td>Gilliam</td>
                    <td>Michael</td>
                    <td>7/22/1949</td>
                    <td>1</td>
                </tr>
                <tr>
                    <td>Walker</td>
                    <td>Matthew</td>
                    <td>1/14/2000</td>
                    <td>3</td>
                </tr>
            </tbody>
        </table>        
    </body>
</html>