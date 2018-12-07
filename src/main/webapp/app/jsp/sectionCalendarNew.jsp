
    <section id="newCalendar" class="splash">
               
        <footer>
            <nav class="">
               <a href="#" data-action="cancel" data-icon="undo"><abbr data-lnt-id="form.cancel"></abbr></a>
               <a href="#" data-action="save"   data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>
              
 		<article id="calendar-form" class="list scroll active">
        	<ul class="form">
        		<li class="dark"><span data-lnt-id="place.cabText"></span></li>
               	<li>
        			<label id="error_calName" style="color:red;"></label> <label data-lnt-id="place.name"></label>:
        			<input type="text" data-lnt-id="place.name" placeholder="" id="calName" value="" required/>
        		</li>
        		<li>
					<label id="error_calDesc" style="color:red;"></label> <label data-lnt-id="place.desc"></label>:       	
					<input type="text" data-lnt-id="place.desc" placeholder="" id="calDesc" value="" required/>	
        		</li>
        		<li id="liPlaceTasks">
					<label data-lnt-id="place.tasks"></label>: </br><span id="placeTasks"></span>       	
				</li>
	       		<li id="liPlaceHours">
					<label data-lnt-id="place.hours"></label>: </br><span id="placeHours"></span>	
        		</li>
        		<li id="liPlaceHoursMon">
					<label data-lnt-id="place.hoursMon"></label>: </br><span id="placeHoursMon"></span>
        		</li>
        		<li id="liPlaceHoursTue">
					<label data-lnt-id="place.hoursTue"></label>: </br><span id="placeHoursTue"></span>
        		</li>
        		<li id="liPlaceHoursWed">
					<label data-lnt-id="place.hoursWed"></label>: </br><span id="placeHoursWed"></span>
        		</li>
        		<li id="liPlaceHoursThu">
					<label data-lnt-id="place.hoursThu"></label>: </br><span id="placeHoursThu"></span>
        		</li>
        		<li id="liPlaceHoursFri">
					<label data-lnt-id="place.hoursFri"></label>: </br><span id="placeHoursFri"></span>
        		</li>
        		<li id="liPlaceHoursSat">
					<label data-lnt-id="place.hoursSat"></label>: </br><span id="placeHoursSat"></span>
        		</li>
        		<li id="liPlaceHoursSun">
					<label data-lnt-id="place.hoursSun"></label>: </br><span id="placeHoursSun"></span>
        		</li>
        	</ul>
        </article>
   
       </section>

    <script src="/app/controller/calendarNewCtrl.js"></script>
   
 