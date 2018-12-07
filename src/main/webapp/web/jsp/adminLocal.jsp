	
    <section id="newLocal" class="splash">
               
        <footer>
        	<nav class="">
               <a href="#" data-action="cancel" data-icon="undo"><abbr data-lnt-id="form.cancel"></abbr></a>
               <a href="#" data-action="save"   data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>
              
 		<article id="local-form" class="list indented scroll active">
        	<ul class="form">
           		<li class="dark"><span data-lnt-id="local.cabText"></span></li>
               	<li>
        			<label data-lnt-id="local.name"></label>:
        			<input type="text" data-lnt-id="local.name" placeholder="" id="locName" value="" />
        		</li>
				<li>
					<label data-lnt-id="where.adddress"></label>:
        			<input type="text" data-lnt-id="where.adddress" placeholder="" id="locAddress" value="" />
        		</li>
        		<li>
					<label data-lnt-id="where.city"></label>:
        			<input type="text" data-lnt-id="where.city" placeholder="" id=locCity value="" />
        		</li>
        		<li>
					<label data-lnt-id="where.state"></label>:
        			<input type="text" data-lnt-id="where.state" placeholder="" id="locState" value="" />
        		</li>
        		<li>
					<label data-lnt-id="where.cp"></label>:
        			<input type="text" data-lnt-id="where.cp" placeholder="" id="locCP" value="" />
        		</li>
        		<!-- <li>
					<label data-lnt-id="where.country"></label>:
        			<input type="text" data-lnt-id="where.country" placeholder="" id="locCountry" value="" />
        		</li> -->
        		<li data-icon="user">
        			<label data-lnt-id="professional.name"></label>:
        			<input type="text" data-lnt-id="professional.name" placeholder="" id="locResponName" value="" />
        			<label data-lnt-id="professional.surname"></label>:
        			<input type="text" data-lnt-id="professional.surname" placeholder="" id="locResponSurname" value="" />
        			<label data-lnt-id="professional.email"></label>:
        			<input type="text" data-lnt-id="professional.email" placeholder="" id="locResponEmail" value="" />
        			<label data-lnt-id="professional.telf"></label>:
        			<input type="text" data-lnt-id="professional.telf" placeholder="" id="locResponTelf1" value="" />
        		</li>
        		<li>
        			<label data-lnt-id="local.apoDuration"></label>:
        			<input type="number" id=locApoDuration value="20" pattern="\d{1,2}" required/>
        		</li>
        		<li>
        			<label data-lnt-id="local.restricted"></label>:
        			<input type="number" id="locTimeRestricted" value="40" pattern="\d{1,4}" required />
        		</li>
				<li>
					<label data-lnt-id="local.openDays"></label>:
        			<input type="number" id="locOpenDays" value="20" pattern="\d{1,2}" required />
        		</li>
				<li>
					<label data-lnt-id="local.peopleBook"></label>:
        			<input type="number" id="locNumPersonsApo" value="3" pattern="\d{1,2}" required />
        		</li>
        		<li>
	   				<label data-lnt-id="local.mulServices"></label>:
	   				<label class="select">
     					<select id="locMulServices" class="custom">
    	 					<option value="0" data-lnt-id="general.no"></option>
	    					<option value="1" data-lnt-id="general.yes"></option>
     					</select>
  					</label>
       			</li>
        		<li>
	   				<label data-lnt-id="local.selectCalendar"></label>:
	   				<label class="select">
     					<select id="locSelCalendar" class="custom">
    	 					<option value="0" data-lnt-id="general.no"></option>
	    					<option value="1" data-lnt-id="general.yes"></option>
     					</select>
  					</label>
       			</li>
        		<li>
					<label data-lnt-id="local.lockBook"></label>:
        			<input type="number" id="locNumUsuDays" value="7" pattern="\d{1,2}" required />
        		</li>
        		<li>
	   				<label data-lnt-id="local.cacheTasks"></label>:
	   				<label class="select">
     					<select id="locCacheTasks" class="custom">
    	 					<option value="0" data-lnt-id="general.no"></option>
	    					<option value="1" data-lnt-id="general.yes"></option>
     					</select>
  					</label>
       			</li>
        	</ul>
        </article>
   
       </section>
 
    <script src="/web/controller/localNewAdminCtrl.js"></script>
  
 