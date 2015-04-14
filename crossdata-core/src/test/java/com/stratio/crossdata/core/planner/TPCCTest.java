/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * License); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * AS IS BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/*
package com.stratio.crossdata.core.planner;

public class TPCCTest {

    //Query0 //Necesario reescribir
    select substring(c_state,1,1) as country, 
    count(*) as numcust, 
    sum(c_balance) as totacctbal 
    from	 ${schemaName}.customer
    where	 substring(c_phone,1,1) in ['1','2','3','4','5','6','7']
    and c_balance > (select avg(c_BALANCE) 
    from  ${schemaName}.customer 
    where  c_balance > 0.00 
    and substring(c_phone,1,1) in ['1','2','3','4','5','6','7'])
    and not exists (select * 
                    from	${schemaName}.orders
    where	o_w_id= ${random(0,tpcc.number.warehouses)}
    AND o_c_id = c_id 
    and o_w_id = c_w_id 
    and o_d_id = c_d_id) 
    group by substring(c_state,1,1) 
    order by substring(c_state,1,1)

    //Query reescrita
    select substring(c_state,1,1) as country,
    count(*) as numcust,
    sum(c_balance) as totacctbal from
    TPC30.customer
    inner join
    (select avg(sub.c_balance) as balance from  TPC30.customer sub where  sub.c_balance > 0.00 and substring(sub.c_phone,1,1) in ['1','2','3','4','5','6','7']) y
    where  substring(c_phone,1,1) in ['1','2','3','4','5','6','7']
    and c_balance > y.balance
    group by substring(c_state,1,1)
    order by country



     //Query1=
    select top 10 ol_o_id, ol_d_id,ol_w_id,sum(ol_quantity),avg(ol_quantity),sum(ol_amount),avg(ol_amount),count(*) 
    from ${schemaName}.order_line where ol_d_id=${random(district)} and ol_w_id=${random(0,tpcc.number.warehouses)} 
    group by ol_o_id, ol_d_id,ol_w_id order by  sum(ol_amount) desc


     //Query2=
    select OL.OL_w_id,OL.OL_D_id,OL.OL_O_id,AVG_Amoun,avg(OL.ol_amount) from (select d_id,d_w_id, avg(ol_amount) AVG_Amoun 
    from ${schemaName}.district D, ${schemaName}.order_line OL_A where D.d_id=OL_A.ol_d_id and D.d_w_id=OL_A.ol_w_id and 
    d_id=${random(district)} and d_w_id=${random(0,tpcc.number.warehouses)} group by d_id,d_w_id)A, ${schemaName}.order_line OL 
    where A.d_id=OL.ol_d_id and A.d_w_id=OL.ol_w_id and OL.ol_d_id=${random(district)} and OL.ol_w_id=${random(0,tpcc.number.warehouses)} 
    group by OL.OL_w_id,OL.OL_D_id,OL.OL_O_id,AVG_Amoun having avg(OL.ol_amount) > AVG_Amoun order by avg(OL.ol_amount) desc

     //Query3=
    select c_d_id,c_credit,count(o_id) from ${schemaName}.customer,${schemaName}.orders 
    where c_w_id=${random(0,tpcc.number.warehouses)} and c_d_id=o_d_id and c_w_id=o_w_id and o_c_id=c_id group by c_credit,c_d_id 
    order by c_d_id, c_credit


     //Query4=
    select  top 10 
    c.c_state,days_between(o.O_ENTRY_D,ol.OL_DELIVERY_D), sum(ol.ol_amount),avg(ol.ol_amount) 
    from ${schemaName}.order_line ol,${schemaName}.orders o,${schemaName}.customer c 
    where o.o_id=ol.ol_o_id  
    and o.o_d_id=ol.ol_d_id  
    and o.o_w_id=ol.ol_w_id and o.o_c_id=c.c_id 
    and o.o_d_id=c.c_d_id 
    and o.o_w_id=c.c_w_id 
    and c.c_w_id=${random(0,tpcc.number.warehouses)} 
    and c_since>= (select add_days(max(c_since),-7) from ${schemaName}.customer c ) 
    and days_between(o.O_ENTRY_D,ol.OL_DELIVERY_D)>30 
    group by c.c_state,days_between(o.O_ENTRY_D,ol.OL_DELIVERY_D) 
    order by count(*) desc


     //Query5=
    select top 100 s_i_id,i_price, s_quantity, i_name,count(*) numero_pedidos, sum(i_price*s_quantity) venta_total 
    from ${schemaName}.stock, ${schemaName}.item where i_id=s_i_id and i_name like 'a${random(char)}%' 
    group by  s_i_id,i_price, s_quantity, i_name order by sum(i_price*s_quantity) desc
  

     //Query6
     SELECT s_w_id,count(*) FROM ${schemaName}. stock where s_quantity>${random(0,100)} and s_quantity<=${random(0,100)+30} group by s_w_id
 

     //Query7
     select max(ol_amount) from ${schemaName}.order_line


     //Query8
     select max(ol_amount),max(ol_quantity) from ${schemaName}.order_line


     //Query9
     select extract(day from (h_date)), avg(h_amount) from ${schemaName}.history where h_c_w_id=${random(0,tpcc.number.warehouses)} group by extract(day from (h_date)) order by extract(day from (h_date))


     //Query10=
    select	sum(ol_amount) as revenue 
    from	${schemaName}.order_line, ${schemaName}.item 
    where	( 
                    ol_i_id = i_id 
                    and i_data like '%a' 
                    and ol_quantity >= ${random(1,5)} 
    and ol_quantity <= ${random(1,5)+5} 
    and i_price between 1 and 400000 
    and ol_w_id in (1,2,3) 
                    ) or ( 
                    ol_i_id = i_id 
                    and i_data like '%b' 
                    and ol_quantity >= ${random(1,5)} 
    and ol_quantity <= ${random(1,5)+5} 
    and i_price between 1 and 400000 
    and ol_w_id in (1,2,4) 
                    ) or ( 
                    ol_i_id = i_id 
                    and i_data like '%c' 
                    and ol_quantity >= ${random(1,5)} 
    and ol_quantity <= ${random(1,5)+5} 
    and i_price between 1 and 400000 
    and ol_w_id in (1,5,3) 
                    )


     //Query11=select   top 100 ol_number, 
    sum(ol_quantity) as sum_qty, 
    sum(ol_amount) as sum_amount, 
    avg(ol_quantity) as avg_qty, 
    avg(ol_amount) as avg_amount, 
    count(*) as count_order 
    from	 ${schemaName}.order_line 
    where	 ol_delivery_d > to_date(${randomDate(20130101,20131231)},'YYYY-MM-DD') 
    group by ol_number  order by sum(ol_quantity) desc


     //Query12=\select  top 100 ol_o_id, ol_w_id, ol_d_id,
    sum(ol_amount) as revenue, o_entry_d 
    from ${schemaName}.customer, ${schemaName}.new_order, ${schemaName}.orders, ${schemaName}.order_line 
    where 	 c_state like 'A%' 
    and c_id = o_c_id 
    and c_w_id = o_w_id 
    and c_d_id = o_d_id 
    and no_w_id = o_w_id 
    and no_d_id = o_d_id 
    and no_o_id = o_id 
    and ol_w_id = o_w_id 
    and ol_d_id = o_d_id 
    and ol_o_id = o_id 
    and o_entry_d >  to_date(${randomDate(20130101,20131231)},'YYYY-MM-DD') 
    group by ol_o_id, ol_w_id, ol_d_id, o_entry_d 
    order by revenue desc, o_entry_d
 

     //Query13=select	top 10 o_ol_cnt, 
    sum(case when o_carrier_id = 1 or o_carrier_id = 2 then 1 else 0 end) as high_line_count, 
    sum(case when o_carrier_id <> 1 and o_carrier_id <> 2 then 1 else 0 end) as low_line_count 
    from	 ${schemaName}.orders, ${schemaName}.order_line 
    where	 ol_w_id = o_w_id 
    and ol_d_id = o_d_id 
    and ol_o_id = o_id 
    and o_entry_d <= ol_delivery_d 
    and ol_delivery_d <  to_date(${randomDate(20130101,20131231)},'YYYY-MM-DD') 
    group by o_ol_cnt 
    order by sum(case when o_carrier_id = 1 or o_carrier_id = 2 then 1 else 0 end) desc, sum(case when o_carrier_id <> 1 and o_carrier_id <> 2 then 1 else 0 end)


     //Query14=select	 top 100 c_count, count(*) as custdist 
    from	 (select c_id, count(o_id) AS c_count 
    from ${schemaName}.customer left outer join ${schemaName}.orders on ( 
                    c_w_id = o_w_id 
                    and c_d_id = o_d_id 
                    and c_id = o_c_id 
                    and o_carrier_id > ${random(district)}) 
    group by c_id) as c_orders 
    group by c_count 
    order by custdist desc, c_count desc

     //Query15=select	100.00 * sum(case when i_data like 'a%' then ol_amount else 0 end) / (1+sum(ol_amount)) as promo_revenue 
    from	${schemaName}.order_line, ${schemaName}.item 
    where	ol_i_id = i_id  
    and ol_delivery_d >= to_date(${randomDate(20130101,20131031)},'YYYY-MM-DD') 
    and ol_delivery_d < to_date(${randomDate(20130101,20131031)+2months},'YYYY-MM-DD')
 

     //Query16=Select	sum(ol_amount) / 2.0 as avg_yearly 
    from	${schemaName}.order_line,  
                    (select   i_id, avg(ol_quantity) as a 
    from     ${schemaName}.item, ${schemaName}.order_line 
    where    i_data like '%b' 
    and ol_i_id = i_id 
    group by i_id) t 
    where	ol_i_id = t.i_id 
    and ol_quantity < t.a


     //Query17=select top 100 substring(i_name,1,3),i_price, s_quantity, 
    count(*) numero_pedidos, sum(i_price*s_quantity) venta_total  
    from ${schemaName}.stock, ${schemaName}.item 
    where i_id=s_i_id 
    and i_id=param1 
    group by substring(i_name,1,3), s_i_id,i_price, s_quantity 
    order by sum(i_price*s_quantity) desc

}
*/