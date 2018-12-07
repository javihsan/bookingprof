
              
 		<article id="local-general" class="list indented scroll active">
        	<ul class="form">
           		<li class="dark"><span data-lnt-id="local.cabText"></span></li>
               	<li>
        			<label id="error_locName" style="color:red;"></label> <label data-lnt-id="local.name"></label>:
        			<input type="text" id="locName" value="" required />
        		</li>
				<li>
					<label id="error_locAddress" style="color:red;"></label> <label data-lnt-id="where.adddress"></label>:
        			<input type="text" id="locAddress" value="" required />
        		</li>
        		<li>
					<label id="error_locCity" style="color:red;"></label> <label data-lnt-id="where.city"></label>:
        			<input type="text" id=locCity value="" required />
        		</li>
        		<li>
					<label id="error_locState" style="color:red;"></label> <label data-lnt-id="where.state"></label>:
        			<input type="text" id="locState" value="" required />
        		</li>
        		<li>
					<label id="error_locCP" style="color:red;"></label> <label data-lnt-id="where.cp"></label>:
        			<input type="text" id="locCP" value="" required />
        		</li>
        		<li data-icon="user">
        			<label id="error_locResponName" style="color:red;"></label> <label data-lnt-id="professional.name"></label>:
        			<input type="text" data-lnt-id="professional.name" placeholder="" id="locResponName" value="" required />
        			<label id="error_locResponSurname" style="color:red;"></label> <label data-lnt-id="professional.surname"></label>:
        			<input type="text" data-lnt-id="professional.surname" placeholder="" id="locResponSurname" value="" required />
        			<label id="error_locResponEmail" style="color:red;"></label> <label data-lnt-id="professional.email"></label>:
        			<input type="text" data-lnt-id="professional.email" placeholder="" id="locResponEmail" value="" required />
        			<label id="error_locResponTelf1" style="color:red;"></label> <label data-lnt-id="professional.telf"></label>:
        			<input type="text" data-lnt-id="professional.telf" placeholder="" id="locResponTelf1" value="" required />
        		</li>
        		<li>
        			<label data-lnt-id="local.bookingClient"></label>:
   	   				<label class="select">
     					<select id="locBookingClient" class="custom">
     						<option value="0" data-lnt-id="general.no"></option>
     						<option value="1" data-lnt-id="general.yes" selected></option>
         				</select>
  					</label>
        		</li>
        		<li>
	   				<label data-lnt-id="local.mailBookign"></label>:
	   				<label class="select">
     					<select id="locMailBookign" class="custom">
    	 					<option value="0" data-lnt-id="general.no"></option>
	    					<option value="1" data-lnt-id="general.yes"></option>
     					</select>
  					</label>
       			</li>
       			<li>
        			<label id="error_locApoDuration" style="color:red;"></label> <label data-lnt-id="local.apoDuration"></label>:
        			<input type="number" id=locApoDuration value="" pattern="\d{1,2}" required/>
        		</li>
           		<li>
        			<label id="error_locTimeRestricted" style="color:red;"></label> <label data-lnt-id="local.restricted"></label>:
        			<input type="number" id="locTimeRestricted" value="" pattern="\d{1,4}" required />
        		</li>
				<li>
					<label id="error_locOpenDays" style="color:red;"></label> <label data-lnt-id="local.openDays"></label>:
        			<input type="number" id="locOpenDays" value="" pattern="\d{1,3}" required />
        		</li>
				<li>
					<label id="error_locNumPersonsApo" style="color:red;"></label> <label data-lnt-id="local.peopleBook"></label>:
        			<input type="number" id="locNumPersonsApo" value="" pattern="\d{1}" required />
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
					<label id="error_locNumUsuDays" style="color:red;"></label> <label data-lnt-id="local.lockBook"></label>:
        			<input type="number" id="locNumUsuDays" value="" pattern="\d{1,2}" required />
        		</li>
        		<li>
        			<label data-lnt-id="local.newClientDefault"></label>:
   	   				<label class="select">
     					<select id="locNewClientDefault" class="custom">
     						<option value="0" data-lnt-id="general.no"></option>
     						<option value="1" data-lnt-id="general.yes"></option>
         				</select>
  					</label>
        		</li>
        	</ul>
        </article>
