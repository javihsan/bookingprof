	<section id="rangeSelect" class="splash">
		<footer>
        	<nav class="">
               <a href="#" data-action="cancel" data-icon="undo"><abbr data-lnt-id="form.cancel"></abbr></a>
               <a href="#" data-action="save"   data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>
              
 		<article id="range-form" class="list indented scroll">
        	<ul class="form">
        		<li class="dark"><span data-lnt-id="report.range.cabText"></span></li>
    			<li>
	      			<label data-lnt-id="report.range.yearDate"></label>
	      			<label class="select">
     					<select id="yearDate" class="custom">
     						<option value=""></option>
     					</select>
  					</label>
	            </li>
	            <li>
	      			<label id="error_monthDate" style="color:red;"></label> <label data-lnt-id="report.range.monthDate"></label>
	      			<label class="select">
     					<select id="monthMonthDate" class="custom">
     						<option value=""></option>
     					</select>
  					</label>
  					<label class="select">
     					<select id="monthYearDate" class="custom">
     						<option value=""></option>
     					</select>
  					</label>
	            </li>
    			<li>
	      			<label id="error_startDate" style="color:red;"></label> <label data-lnt-id="report.range.startDate"></label> yyyy-mm-dd
	            	<input id="startDate" size="5" type="date" value="" required>
	            	<label id="error_endDate" style="color:red;"></label> <label data-lnt-id="report.range.endDate"></label> yyyy-mm-dd
	            	<input id="endDate" size="5" type="date" value="" required>
	            </li>
            </ul>
        </article>
        
    </section>

    <script src="/app/controller/rangeSelectCtrl.js"></script>
    
