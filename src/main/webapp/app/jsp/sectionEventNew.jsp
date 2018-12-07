	
	<section id="newEvent" class="splash">
		<footer>
        	<nav class="">
				<a href="#" data-action="cancel" data-icon="undo"><abbr data-lnt-id="form.cancel"></abbr></a>
                <a href="#" data-action="save"   data-icon="save"><abbr data-lnt-id="form.ok"></abbr></a>
           </nav>
        </footer>
              
 		<article id="event-form" class="list indented scroll active">
        	<ul class="form">
        		<li class="dark"><span data-lnt-id="event.newCabText"></span> <span id="event_hour"></span></li>
               	<li>
        			<label id="error_eveClientName" style="color:red;"></label> <label data-lnt-id="client.name"></label>:
        			<input type="text" data-lnt-id="client.name" placeholder="" id="eveClientName" autocomplete required/>
          		</li>
              	<li>
        			<label id="error_eveClientEmail" style="color:red;"></label> <label data-lnt-id="client.email"></label>:
        			<input type="email" data-lnt-id="client.email" placeholder="" id="eveClientEmail" autocomplete required/>
        		</li>
        		<li>
        			<label id="error_eveClientTelf" style="color:red;"></label> <label data-lnt-id="client.telf"></label>:
        			<input type="tel" placeholder="9.. / 6.." id="eveClientTelf" autocomplete required/>
        		</li>
        		<li>
        			<label data-lnt-id="event.com"></label>:
        			<textarea data-lnt-id="event.com" placeholder="" id="eveDescAlega" maxlength="300"></textarea>
        		</li>
            </ul>
        </article>
        
    </section>

    <script src="/app/model/eventModel.js"></script>
    <script src="/app/controller/eventNewCtrl.js"></script>
    <script src="/app/controller/eventListCtrl.js"></script>
    
